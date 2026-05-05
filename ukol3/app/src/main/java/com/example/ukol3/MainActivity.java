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

        items = new ArrayList<>();
        etNewItemName = findViewById(R.id.etNewItemName);
        etNewItemQuantity = findViewById(R.id.etNewItemQuantity);

        RecyclerView recyclerView = findViewById(R.id.rvListOfItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemAdapter(this, items);
        recyclerView.setAdapter(adapter);

        Button addNew = findViewById(R.id.add_new);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String name = etNewItemName.getText().toString();
                    int quantity = Integer.parseInt(etNewItemQuantity.getText().toString());
                    if (name.isEmpty() || quantity < 1) {
                        Toast.makeText(MainActivity.this, "Invalid name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addItem(name, quantity);
                } catch (NumberFormatException e) {
                    // Handle the exception
                    Toast.makeText(MainActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        }

    private void addItem(String name, Integer quantity) {
        items.add(new Item(name, quantity));
        adapter.notifyItemInserted(items.size() - 1);
    }
}
