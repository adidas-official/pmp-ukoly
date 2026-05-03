package com.example.login;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class PaymentDetailActivity extends AppCompatActivity {
    private AccountReaderDBHelper dbHelper;
    private String currentRecipientAccount;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        
        dbHelper = AccountReaderDBHelper.getInstance(this);
        
        // Retrieve session to know the owner of potential new recipient
        session = (UserSession) getIntent().getSerializableExtra("user_session");
        
        goBack();
        getDataFromActivity();
        setupSaveRecipient();
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
            currentRecipientAccount = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT));
            String ks = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_KS));
            String ss = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_SS));
            String vs = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_VS));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE));
            String note2Rec = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_NOTE_TO_RECIPIENT));

            // Set Data
            tvid.setText(id);
            tvAccount.setText(currentRecipientAccount);
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

    private void setupSaveRecipient() {
        Button saveBtn = findViewById(R.id.button_save_recipient);
        saveBtn.setOnClickListener(v -> {
            if (currentRecipientAccount == null || session == null) return;
            
            final EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.save_recipient)
                    .setMessage("Zadejte jmeno prijemce")
                    .setView(input)
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        String name = input.getText().toString();
                        if (!name.isEmpty()) {
                            saveRecipientToDb(name, currentRecipientAccount);
                        } else {
                            Toast.makeText(this, "Jmeno nesmi byt prazdne", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
    }

    private void saveRecipientToDb(String name, String account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Duplicate check
        Cursor cursor = db.query(
                AccountReaderContract.RecipientEntry.TABLE_NAME,
                null,
                AccountReaderContract.RecipientEntry.COLUMN_NAME_OWNER_USERNAME + " = ? AND (" +
                AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME + " = ? OR " +
                AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM + " = ?)",
                new String[]{session.getUsername(), name, account},
                null, null, null
        );

        if (cursor.getCount() > 0) {
            Toast.makeText(this, "Prijemce s timto jmenem nebo uctem jiz existuje", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME, name);
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM, account);
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_OWNER_USERNAME, session.getUsername());
        
        db.insert(AccountReaderContract.RecipientEntry.TABLE_NAME, null, values);
        Toast.makeText(this, "Prijemce ulozen", Toast.LENGTH_SHORT).show();
    }
}
