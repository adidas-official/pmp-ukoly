package com.example.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckPaymentActivity extends AppCompatActivity {
    /**
     * Uzivatel si overi, zda zadal spravne udaje o platbe a pripadne platbu zrusi nebo potvrdi.
     * Data jsou v prepravce pro snazsi manipulaci
     */
    private AccountReaderDBHelper dbHelper;
    private UserSession session;
    private PaymentRequest paymentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        
        // Use Singleton instead of 'new'
        dbHelper = AccountReaderDBHelper.getInstance(this);
        
        getDataFromActivity();
        setupCancelButton();
        setupConfirmButton();
    }

    private void getDataFromActivity() {
        Intent i = getIntent();
        if (i != null) {
            // Retrieve transport objects
            session = (UserSession) i.getSerializableExtra("user_session");
            paymentRequest = (PaymentRequest) i.getSerializableExtra("payment_request");

            if (paymentRequest != null) {
                TextView amountTV = findViewById(R.id.textView_amount);
                TextView bankAccountTV = findViewById(R.id.textView_bankaccount);
                
                amountTV.setText(String.format(Locale.getDefault(), "%.2f Kc", paymentRequest.getAmount()));
                bankAccountTV.setText(String.format(Locale.getDefault(), "%s/%s", 
                        paymentRequest.getToAccount(), paymentRequest.getBankCode()));
            }
        }
    }

    private void setupCancelButton() {
        Button cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupConfirmButton() {
        Button confirm = findViewById(R.id.button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        if (session == null || paymentRequest == null) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            double currentBalance = session.getBalance();
            double amount = paymentRequest.getAmount();

            if (currentBalance >= amount) {
                double newBalance = currentBalance - amount;

                // 1. Update sender's balance in DB
                ContentValues accountValues = new ContentValues();
                accountValues.put(AccountReaderContract.AccountReader.COLUMN_NAME_BALANCE, newBalance);
                db.update(AccountReaderContract.AccountReader.TABLE_NAME, accountValues, 
                        AccountReaderContract.AccountReader.COLUMN_NAME_USERNAME + " = ?", 
                        new String[]{session.getUsername()});

                // 2. Record the payment in DB
                ContentValues paymentValues = new ContentValues();
                paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_FROM_ACCOUNT, session.getAccountNumber());
                paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT, paymentRequest.getToAccount());
                paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT, amount);
                paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE, paymentRequest.getNote());
                
                String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                paymentValues.put(AccountReaderContract.PaymentEntry.COLUMN_NAME_DATE, currentDate);

                db.insert(AccountReaderContract.PaymentEntry.TABLE_NAME, null, paymentValues);

                // 3. Update the Transport Object (UserSession) local state
                session.setBalance(newBalance);

                db.setTransactionSuccessful();
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
                
                // 4. Return to UserActivity with the updated session
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra("user_session", session);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Insufficient funds!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error processing payment", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        // No need to close singleton dbHelper here
        super.onDestroy();
    }
}
