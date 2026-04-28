package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class PaymentDetailActivity extends AppCompatActivity {
    private AccountReaderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        
        dbHelper = AccountReaderDBHelper.getInstance(this);
        
        goBack();
        getDataFromActivity();
    }

    private void goBack() {
        Button cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(v -> finish());
    }

    private void getDataFromActivity() {
        Intent i = this.getIntent();
        if (i != null) {
            String id = i.getStringExtra("id");
            if (id != null) {
                fetchAndDisplayPayment(id);
            }
        }
    }

    private void fetchAndDisplayPayment(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                AccountReaderContract.PaymentEntry._ID,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_KS,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_SS,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_VS,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE_TO_RECIPIENT
        };

        String selection = AccountReaderContract.PaymentEntry._ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                AccountReaderContract.PaymentEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor.moveToFirst()) {
            // Bind Views
            TextView tvid = findViewById(R.id.textView_id);
            TextView tvAccount = findViewById(R.id.textView_bankaccount);
            TextView tvAmount = findViewById(R.id.textView_amount);
            TextView tvKs = findViewById(R.id.textView_ks);
            TextView tvSs = findViewById(R.id.textView_ss);
            TextView tvVs = findViewById(R.id.textView_vs);
            TextView tvNote = findViewById(R.id.textView_note);
            TextView tvNote2Rec = findViewById(R.id.textView_note2reciever);

            // Get Data
            String toAccount = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT));
            String ks = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_KS));
            String ss = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_SS));
            String vs = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_VS));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE));
            String note2Rec = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE_TO_RECIPIENT));

            // Set Data
            tvid.setText(id);
            tvAccount.setText(toAccount);
            tvAmount.setText(String.format(Locale.getDefault(), "%.2f Kc", amount));
            tvKs.setText(ks != null && !ks.isEmpty() ? ks : "-");
            tvSs.setText(ss != null && !ss.isEmpty() ? ss : "-");
            tvVs.setText(vs != null && !vs.isEmpty() ? vs : "-");
            tvNote.setText(note != null && !note.isEmpty() ? note : "-");
            tvNote2Rec.setText(note2Rec != null && !note2Rec.isEmpty() ? note2Rec : "-");
            
        } else {
            Toast.makeText(this, "Payment not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
