package com.example.ukol3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
        holder.tvName.setText(items.get(position).getName());
        holder.tvQuantity.setText(String.format(java.util.Locale.getDefault(), "%d", items.get(position).getQuantity()));

        if (items.get(position).isCrossedOut()) {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvQuantity.setPaintFlags(holder.tvQuantity.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvQuantity.setPaintFlags(holder.tvQuantity.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            Item item = items.get(currentPos);

            if (!item.isCrossedOut()) {
                // First click: cross it out
                item.setCrossedOut(true);
                notifyItemChanged(currentPos);
            } else {
                // Second click: remove it
                items.remove(currentPos);
                notifyItemRemoved(currentPos);
            }
        });

    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
