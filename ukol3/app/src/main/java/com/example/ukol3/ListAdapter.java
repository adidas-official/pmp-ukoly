package com.example.ukol3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // KLÍČOVÝ BOD: Kliknutí na řádek otevře detail seznamu
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            // Předáme ID seznamu, aby MainActivity věděla, co načíst
            intent.putExtra("LIST_ID", currentList.getId());
            context.startActivity(intent);
        });

        // Smazání celého seznamu
        holder.btnDelete.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase.getInstance(context).shoppingDao().deleteList(currentList);
                // Zde by bylo dobré smazat i všechny Item, které mají toto listId,
                // pokud nemáte nastaveno CASCADE delete v databázi.

                ((ListsActivity) context).runOnUiThread(() -> {
                    shoppingLists.remove(position);
                    notifyItemRemoved(position);
                });
            }).start();
        });

        // Editace názvu seznamu (můžete použít stejný princip jako u Item)
        holder.btnEdit.setOnClickListener(v -> {
            // Zde by byla metoda showEditListDialog(currentList, position);
        });
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvListName;
        ImageButton btnDelete, btnEdit;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.tvListName); // ID z vašeho shopping_list_view.xml
            btnDelete = itemView.findViewById(R.id.btnDeleteList);
            btnEdit = itemView.findViewById(R.id.btnEditList);
        }
    }
}