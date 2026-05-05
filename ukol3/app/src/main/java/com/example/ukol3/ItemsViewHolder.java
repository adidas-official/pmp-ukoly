package com.example.ukol3;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsViewHolder extends RecyclerView.ViewHolder {

    public ImageButton btnDelete;
    public ImageButton btnEdit;
    TextView tvName;
    TextView tvQuantity;

    public ItemsViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
        tvQuantity = itemView.findViewById(R.id.tvQuantity);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}