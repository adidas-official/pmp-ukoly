package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PayMeActivity extends AppCompatActivity {
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_me);

        session = (UserSession) getIntent().getSerializableExtra("user_session");

        EditText amountEt = findViewById(R.id.editText_payMeAmount);
        Button generateBtn = findViewById(R.id.button_generateQR);
        Button backBtn = findViewById(R.id.button_back);

        generateBtn.setOnClickListener(v -> {
            String amountStr = amountEt.getText().toString();
            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Zadejte castku", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Intent intent = new Intent(PayMeActivity.this, QRCodeActivity.class);
                intent.putExtra("user_session", session);
                intent.putExtra("amount", amount);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Neplatny format castky", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> finish());
    }
}
