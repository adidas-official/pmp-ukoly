package com.example.ukol3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Item> items;
    private ItemAdapter adapter;

    private EditText etNewItemName;
    private EditText etNewItemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNewItemName = findViewById(R.id.etNewItemName);
        etNewItemQuantity = findViewById(R.id.etNewItemQuantity);

        // 1. Inicializace seznamu a adaptéru (hned na začátku!)
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
                addItemToDatabase(name, quantity);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Neplatné množství", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metoda pro načtení dat
    private void loadDataFromDatabase() {
        new Thread(() -> {
            List<Item> itemsFromDb = AppDatabase.getInstance(this).shoppingDao().getAllItems();
            runOnUiThread(() -> {
                // Aktualizujeme náš lokální seznam i adaptér
                items.clear();
                items.addAll(itemsFromDb);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    // Metoda pro přidání do DB i do UI
    private void addItemToDatabase(String name, int quantity) {
        Item newItem = new Item(name, quantity);

        new Thread(() -> {
            // Uložení do Room
            AppDatabase.getInstance(this).shoppingDao().insertItem(newItem);

            // Získání ID, které vygenerovala DB (pokud ho potřebujete pro další práci)
            // V tomto jednoduchém případě stačí znovu načíst seznam nebo přidat do UI
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
