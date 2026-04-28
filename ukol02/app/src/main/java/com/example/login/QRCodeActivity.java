package com.example.login;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QRCodeActivity extends AppCompatActivity {
    private UserSession session;
    private double amount;
    private AccountReaderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        dbHelper = AccountReaderDBHelper.getInstance(this);
        session = (UserSession) getIntent().getSerializableExtra("user_session");
        amount = getIntent().getDoubleExtra("amount", 0.0);

        TextView detailsTv = findViewById(R.id.textView_qrDetails);
        ImageView qrImage = findViewById(R.id.imageView_qrCode);
        Button doneBtn = findViewById(R.id.button_qrDone);

        // Simulation: Set a system icon as a "QR code" placeholder
        qrImage.setImageResource(android.R.drawable.ic_menu_crop);
        qrImage.setBackgroundColor(getResources().getColor(android.R.color.white));

        if (session != null) {
            String details = String.format(Locale.getDefault(), 
                "Cislo uctu: %s\nCastka: %.2f Kc", 
                session.getAccountNumber(), amount);
            detailsTv.setText(details);
        }

        doneBtn.setOnClickListener(v -> {
            simulateIncomingPayment();
        });
    }

    /**
     * Simulation: Adds the amount to the balance and records it as an incoming payment
     */
    private void simulateIncomingPayment() {
        if (session == null) {
            finish();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            double currentBalance = session.getBalance();
            double newBalance = currentBalance + amount;

            // 1. Update balance in DB
            ContentValues accountValues = new ContentValues();
            accountValues.put(AccountReaderContract.AccountReader.COLUMN_NAME_BALANCE, newBalance);
            db.update(AccountReaderContract.AccountReader.TABLE_NAME, accountValues, 
                    AccountReaderContract.AccountReader.COLUMN_NAME_USERNAME + " = ?", 
                    new String[]{session.getUsername()});

            // 2. Record simulated incoming payment
            ContentValues paymentValues = new ContentValues();
            paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_FROM_ACCOUNT, "987654321/0100"); // Random sender
            paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT, session.getAccountNumber());
            paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT, amount); // Positive for incoming
            paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_DATE, 
                    new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
            paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE, "QR Platba");

            db.insert(AccountReaderContract.PaymentEntry.TABLE_NAME, null, paymentValues);

            db.setTransactionSuccessful();
            Toast.makeText(this, "Simulovana platba prijata!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Chyba simulace", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }
    }
}
