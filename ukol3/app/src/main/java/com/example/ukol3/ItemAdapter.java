package com.example.ukol3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemsViewHolder>{

    Context context;
    List<Item> items;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        Item item = items.get(position);

        try {
            holder.tvName.setText(item.getName());
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        updateStroke(holder, item.isCrossedOut());

        holder.itemView.setOnClickListener(v -> {
            item.setCrossedOut(!item.isCrossedOut());
            AppDatabase.getInstance(v.getContext()).shoppingDao().updateItem(item);
            notifyItemChanged(holder.getBindingAdapterPosition());
        });

        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                AppDatabase.getInstance(v.getContext()).shoppingDao().deleteItem(item);
                items.remove(currentPos);
                notifyItemRemoved(currentPos);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            showEditDialog(v.getContext(), item, holder.getBindingAdapterPosition());
        });

    }

    private void showEditDialog(Context context, Item item, int position) {
        // 1. "Nafouknutí" XML layoutu
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_item, null);

        // 2. Propojení prvků z XML
        EditText etName = dialogView.findViewById(R.id.etEditName);
        EditText etQuantity = dialogView.findViewById(R.id.etEditQuantity);

        // 3. Předvyplnění daty
        etName.setText(item.getName());
        etQuantity.setText(String.valueOf(item.getQuantity()));

        // 4. Sestavení AlertDialogu
        new AlertDialog.Builder(context)
                .setTitle("Upravit položku")
                .setView(dialogView)
                .setPositiveButton("Uložit", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String qtyStr = etQuantity.getText().toString().trim();

                    if (!newName.isEmpty() && !qtyStr.isEmpty()) {
                        item.setName(newName);
                        item.setQuantity(Integer.parseInt(qtyStr));

                        // Uložení do Room databáze na pozadí
                        new Thread(() -> {
                            AppDatabase.getInstance(context).shoppingDao().updateItem(item);

                            // Update UI v hlavním vlákně
                            if (context instanceof Activity) {
                                ((Activity) context).runOnUiThread(() -> notifyItemChanged(position));
                            }
                        }).start();
                    }
                })
                .setNegativeButton("Zrušit", null)
                .create()
                .show();
    }

    private void updateStroke(ItemsViewHolder holder, boolean isCrossedOut) {
        int flag = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;
        if (isCrossedOut) {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | flag);
            holder.tvQuantity.setPaintFlags(holder.tvQuantity.getPaintFlags() | flag);
        } else {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & ~flag);
            holder.tvQuantity.setPaintFlags(holder.tvQuantity.getPaintFlags() & ~flag);
        }
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
