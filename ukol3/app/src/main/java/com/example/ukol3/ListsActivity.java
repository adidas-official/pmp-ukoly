package com.example.ukol3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListsActivity extends AppCompatActivity {

    private List<ShoppingList> shoppingLists;
    private ListAdapter adapter;
    private EditText etNewListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        etNewListName = findViewById(R.id.etNewListName);
        shoppingLists = new ArrayList<>();
        adapter = new ListAdapter(this, shoppingLists);

        RecyclerView recyclerView = findViewById(R.id.rvListOfLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadDataFromDatabase();

        Button addNew = findViewById(R.id.add_newList);
        addNew.setOnClickListener(v -> {
            String name = etNewListName.getText().toString().trim();
            if (!name.isEmpty()) {
                addListToDatabase(name);
            }
        });
    }

//    // Metoda pro načtení dat
    private void loadDataFromDatabase() {
        new Thread(() -> {
            // Run DB query on background thread
            List<ShoppingList> listsFromDb = AppDatabase.getInstance(this).shoppingDao().getAllShoppingLists();

            // Switch back to UI thread to update the adapter
            runOnUiThread(() -> {
                shoppingLists.clear();
                shoppingLists.addAll(listsFromDb);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
//
//    // Metoda pro přidání do DB i do UI
    private void addListToDatabase(String name) {
        ShoppingList newList = new ShoppingList(name);

        new Thread(() -> {
            // Uložení do Room
                long generatedId = AppDatabase.getInstance(this).shoppingDao().insertShoppingList(newList);

                newList.setListId((int) generatedId);

                runOnUiThread(() -> {
                    shoppingLists.add(newList);
                    adapter.notifyItemInserted(shoppingLists.size() - 1);
                    etNewListName.setText("");
                    RecyclerView rv = findViewById(R.id.rvListOfLists);
                    rv.scrollToPosition(shoppingLists.size() - 1);
                });
        }).start();

    }
}
