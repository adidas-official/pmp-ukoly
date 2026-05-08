package com.example.ukol3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ShoppingList> shoppingLists;
    private Context context;

    public ListAdapter(Context context, List<ShoppingList> shoppingLists) {
        this.context = context;
        this.shoppingLists = shoppingLists;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_list_view, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ShoppingList currentList = shoppingLists.get(position);
        holder.tvListName.setText(currentList.getName());

        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("LIST_ID", shoppingLists.get(currentPos).getListId());
                intent.putExtra("LIST_NAME", shoppingLists.get(currentPos).getName());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            ShoppingList listToDelete = shoppingLists.get(currentPos);
            new Thread(() -> {
                AppDatabase.getInstance(context).shoppingDao().deleteShoppingList(listToDelete);
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        // Re-check position to avoid IndexOutOfBoundsException
                        int latestPos = holder.getBindingAdapterPosition();
                        if (latestPos != RecyclerView.NO_POSITION) {
                            shoppingLists.remove(latestPos);
                            notifyItemRemoved(latestPos);
                        }
                    });
                }
            }).start();
        });

        holder.btnEdit.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                showEditDialog(shoppingLists.get(currentPos), currentPos);
            }
        });
    }

    private void showEditDialog(ShoppingList list, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_list, null);

        EditText etName = dialogView.findViewById(R.id.etEditListName);
        etName.setText(list.getName());

        new AlertDialog.Builder(context)
                .setTitle(R.string.edit_list)
                .setView(dialogView)
                .setPositiveButton(R.string.add_item, (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        list.setName(newName);
                        new Thread(() -> {
                            AppDatabase.getInstance(context).shoppingDao().updateShoppingList(list);
                            if (context instanceof Activity) {
                                ((Activity) context).runOnUiThread(() -> notifyItemChanged(position));
                            }
                        }).start();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public int getItemCount() {
        return shoppingLists != null ? shoppingLists.size() : 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvListName;
        ImageButton btnDelete, btnEdit;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.tvListName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
