package com.example.ukol3;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditListActivity extends AppCompatActivity {

    private int selectedBgColor;
    private int selectedTextColor;
    private int listId;
    private TextView tvListName;
    private EditText etName;

    private final int[] presetColors = {
            0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7, 0xFF3F51B5,
            0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4, 0xFF009688, 0xFF4CAF50,
            0xFF8BC34A, 0xFFCDDC39, 0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800,
            0xFF795548, 0xFF9E9E9E, 0xFF607D8B, 0xFF000000, 0xFFFFFFFF
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        // 1. Načtení dat z Intentu
        listId = getIntent().getIntExtra("LIST_ID", -1);
        String currentName = getIntent().getStringExtra("LIST_NAME");
        selectedBgColor = getIntent().getIntExtra("BG_COLOR", 0xFFFFFFFF);
        selectedTextColor = getIntent().getIntExtra("TEXT_COLOR", 0xFF000000);

        // 2. Inicializace UI
        tvListName = findViewById(R.id.tvListName);
        etName = findViewById(R.id.etEditName);
        GridView gvBackground = findViewById(R.id.gvBackgroundColors);
        GridView gvText = findViewById(R.id.gvTextColors);

        // Nastavení výchozího stavu náhledu
        tvListName.setText(currentName);
        updatePreview();

        // 3. Nastavení mřížek barev
        setupColorGrid(gvBackground, true);
        setupColorGrid(gvText, false);

        // 4. Tlačítka
        findViewById(R.id.btnSave).setOnClickListener(v -> saveChanges(etName.getText().toString()));
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Bonus: Dynamická změna náhledu při psaní jména
        etName.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvListName.setText(s.toString());
            }
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupColorGrid(GridView gridView, boolean isBackground) {
        gridView.setAdapter(new android.widget.BaseAdapter() {
            @Override
            public int getCount() { return presetColors.length; }
            @Override
            public Object getItem(int i) { return presetColors[i]; }
            @Override
            public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View box = new View(EditListActivity.this);
                box.setLayoutParams(new GridView.LayoutParams(100, 100));
                box.setBackgroundColor(presetColors[i]);
                // Volitelné: přidání rámečku, aby byla vidět bílá barva na bílém
                box.setBackgroundResource(R.drawable.color_box_border);
                box.setBackgroundColor(presetColors[i]);
                return box;
            }
        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (isBackground) {
                selectedBgColor = presetColors[position];
            } else {
                selectedTextColor = presetColors[position];
            }
            updatePreview();
        });
    }

    private void updatePreview() {
        tvListName.setBackgroundColor(selectedBgColor);
        tvListName.setTextColor(selectedTextColor);
    }

    private void saveChanges(String newName) {
        if (newName.isEmpty()) return;

        new Thread(() -> {
            ShoppingList list = new ShoppingList(newName);
            list.setListId(listId);
            list.setBackgroundColor(selectedBgColor);
            list.setTextColor(selectedTextColor);

            AppDatabase.getInstance(this).shoppingDao().updateShoppingList(list);
            runOnUiThread(this::finish);
        }).start();
    }
}