package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipientAdapter extends RecyclerView.Adapter<RecipientAdapter.RecipientViewHolder> {
    private List<RecipientManagementActivity.Recipient> recipients;
    private OnRecipientActionListener listener;

    public interface OnRecipientActionListener {
        void onEdit(RecipientManagementActivity.Recipient recipient);
        void onDelete(RecipientManagementActivity.Recipient recipient);
    }

    public RecipientAdapter(List<RecipientManagementActivity.Recipient> recipients, OnRecipientActionListener listener) {
        this.recipients = recipients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipient, parent, false);
        return new RecipientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipientViewHolder holder, int position) {
        RecipientManagementActivity.Recipient recipient = recipients.get(position);
        holder.nameTv.setText(recipient.name);
        holder.accountTv.setText(recipient.accountNum);
        holder.editBtn.setOnClickListener(v -> listener.onEdit(recipient));
        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(recipient));
    }

    @Override
    public int getItemCount() {
        return recipients.size();
    }

    static class RecipientViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, accountTv;
        Button editBtn, deleteBtn;

        public RecipientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textView_recipientName);
            accountTv = itemView.findViewById(R.id.textView_recipientAccount);
            editBtn = itemView.findViewById(R.id.button_editRecipient);
            deleteBtn = itemView.findViewById(R.id.button_deleteRecipient);
        }
    }
}
