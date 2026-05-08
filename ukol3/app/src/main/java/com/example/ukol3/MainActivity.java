package com.example.ukol3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Item> items;
    private ItemAdapter adapter;

    private EditText etNewItemName;
    private EditText etNewItemQuantity;
    private Button btnBack;
    private TextView tvListName;
    private int currentListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Získání ID seznamu z Intentu (výchozí hodnota -1 značí chybu)
        currentListId = getIntent().getIntExtra("LIST_ID", -1);

        if (currentListId == -1) {
            Toast.makeText(this, "Chyba: Seznam nenalezen", Toast.LENGTH_SHORT).show();
            finish(); // Zavře aktivitu, pokud nemá ID
            return;
        }

        etNewItemName = findViewById(R.id.etNewItemName);
        etNewItemQuantity = findViewById(R.id.etNewItemQuantity);
        tvListName = findViewById(R.id.tvListName);
        btnBack = findViewById(R.id.btnBack);

        tvListName.setText(getIntent().getStringExtra("LIST_NAME"));

        // 1. Inicializace seznamu a adaptéru
        items = new ArrayList<>();
        adapter = new ItemAdapter(this, items);

        // 2. Nastavení RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvListOfItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 3. Načtení dat z databáze na pozadí
        loadDataFromDatabase();

        Button addNew = findViewById(R.id.add_new);
        addNew.setOnClickListener(v -> {
            try {
                String name = etNewItemName.getText().toString().trim();
                String qtyStr = etNewItemQuantity.getText().toString().trim();

                if (name.isEmpty() || qtyStr.isEmpty()) {
                    Toast.makeText(this, "Vyplňte všechna pole", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(qtyStr);
                addItemToDatabase(currentListId, name, quantity);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Neplatné množství", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadDataFromDatabase() {
        new Thread(() -> {
            List<Item> itemsFromDb = AppDatabase.getInstance(this).shoppingDao().getItemsForList(currentListId);
            runOnUiThread(() -> {
                items.clear();
                items.addAll(itemsFromDb);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void addItemToDatabase(int shoppingListId, String name, int quantity) {
        Item newItem = new Item(shoppingListId, name, quantity);

        new Thread(() -> {
            // Uložení do Room a získání vygenerovaného ID
            long generatedId = AppDatabase.getInstance(this).shoppingDao().insertItem(newItem);
            newItem.setId((int) generatedId);

            runOnUiThread(() -> {
                items.add(newItem);
                adapter.notifyItemInserted(items.size() - 1);

                // Vyčištění políček
                etNewItemName.setText("");
                etNewItemQuantity.setText("");
            });
        }).start();
    }
}
