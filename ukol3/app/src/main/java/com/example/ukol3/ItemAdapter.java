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
    /**
     * Logika pro zobrazeni polozek v recycler view
     */

    private int bgColor;
    private int textColor;
    Context context;
    List<Item> items;

    public ItemAdapter(Context context, List<Item> items, int bgColor, int textColor) {
        this.context = context;
        this.items = items;
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        Item item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        updateVisualState(holder, item.isCrossedOut());

        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;
            
            item.setCrossedOut(!item.isCrossedOut());
            new Thread(() -> {
                AppDatabase.getInstance(context).shoppingDao().updateItem(item);
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> notifyItemChanged(currentPos));
                }
            }).start();
        });

        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                new Thread(() -> {
                    AppDatabase.getInstance(context).shoppingDao().deleteItem(item);
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> {
                            int newPos = holder.getBindingAdapterPosition();
                            if (newPos != RecyclerView.NO_POSITION) {
                                items.remove(newPos);
                                notifyItemRemoved(newPos);
                            }
                        });
                    }
                }).start();
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                showEditDialog(context, item, currentPos);
            }
        });

    }

    private void showEditDialog(Context context, Item item, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_item, null);

        EditText etName = dialogView.findViewById(R.id.etEditName);
        EditText etQuantity = dialogView.findViewById(R.id.etEditQuantity);

        etName.setText(item.getName());
        etQuantity.setText(String.valueOf(item.getQuantity()));

        new AlertDialog.Builder(context)
                .setTitle("Upravit položku")
                .setView(dialogView)
                .setPositiveButton("Uložit", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String qtyStr = etQuantity.getText().toString().trim();

                    if (!newName.isEmpty() && !qtyStr.isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(qtyStr);
                            item.setName(newName);
                            item.setQuantity(quantity);

                            new Thread(() -> {
                                AppDatabase.getInstance(context).shoppingDao().updateItem(item);
                                if (context instanceof Activity) {
                                    ((Activity) context).runOnUiThread(() -> notifyItemChanged(position));
                                }
                            }).start();
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Neplatné množství", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Zrušit", null)
                .create()
                .show();
    }

    private void updateVisualState(ItemsViewHolder holder, boolean isCrossedOut) {
        if (isCrossedOut) {
            // Stav: Dokončeno (Červený text, světle růžové pozadí)
            holder.tvName.setTextColor(android.graphics.Color.parseColor("#B71C1C")); // Tmavě červená
            holder.tvQuantity.setTextColor(android.graphics.Color.parseColor("#B71C1C"));
            holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#FFEBEE")); // Světle růžová
        } else {
            // Stav: Aktivní (podle vybraneho motivu)
            holder.tvName.setTextColor(textColor);
            holder.tvQuantity.setTextColor(textColor);
            holder.itemView.setBackgroundColor(bgColor);
            holder.itemView.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
