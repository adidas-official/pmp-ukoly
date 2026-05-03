package com.example.login;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecipientManagementActivity extends AppCompatActivity {
    private AccountReaderDBHelper dbHelper;
    private UserSession session;
    private RecipientAdapter adapter;
    private final List<Recipient> recipientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_management);

        dbHelper = AccountReaderDBHelper.getInstance(this);
        session = (UserSession) getIntent().getSerializableExtra("user_session");

        RecyclerView recyclerView = findViewById(R.id.recyclerView_recipients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new RecipientAdapter(recipientsList, new RecipientAdapter.OnRecipientActionListener() {
            @Override
            public void onEdit(Recipient recipient) {
                showRecipientDialog(recipient);
            }

            @Override
            public void onDelete(Recipient recipient) {
                deleteRecipient(recipient);
            }
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.button_add_new).setOnClickListener(v -> showRecipientDialog(null));
        findViewById(R.id.button_back).setOnClickListener(v -> finish());

        loadRecipients();
    }

    private void loadRecipients() {
        recipientsList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                AccountReaderContract.RecipientEntry.TABLE_NAME,
                null,
                AccountReaderContract.RecipientEntry.COLUMN_NAME_OWNER_USERNAME + " = ?",
                new String[]{session.getUsername()},
                null, null, AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AccountReaderContract.RecipientEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME));
            String acc = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM));
            recipientsList.add(new Recipient(id, name, acc));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void showRecipientDialog(Recipient recipient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(recipient == null ? R.string.new_recipient : R.string.edit);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText nameInput = new EditText(this);
        nameInput.setHint(R.string.name);
        if (recipient != null) nameInput.setText(recipient.name);
        layout.addView(nameInput);

        final EditText accInput = new EditText(this);
        accInput.setHint(R.string.accountNum);
        if (recipient != null) accInput.setText(recipient.accountNum);
        layout.addView(accInput);

        builder.setView(layout);

        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            String name = nameInput.getText().toString();
            String acc = accInput.getText().toString();
            if (!name.isEmpty() && !acc.isEmpty()) {
                if (recipient == null) {
                    saveRecipient(name, acc);
                } else {
                    updateRecipient(recipient.id, name, acc);
                }
            } else {
                Toast.makeText(this, R.string.fill_all, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void saveRecipient(String name, String account) {
        if (isDuplicate(name, account, -1)) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME, name);
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM, account);
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_OWNER_USERNAME, session.getUsername());
        
        db.insert(AccountReaderContract.RecipientEntry.TABLE_NAME, null, values);
        loadRecipients();
    }

    private void updateRecipient(int id, String name, String account) {
        if (isDuplicate(name, account, id)) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME, name);
        values.put(AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM, account);
        
        db.update(AccountReaderContract.RecipientEntry.TABLE_NAME, values, 
                AccountReaderContract.RecipientEntry._ID + " = ?", new String[]{String.valueOf(id)});
        loadRecipients();
    }

    private void deleteRecipient(Recipient recipient) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.delete_confirm, recipient.name))
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete(AccountReaderContract.RecipientEntry.TABLE_NAME, 
                            AccountReaderContract.RecipientEntry._ID + " = ?", new String[]{String.valueOf(recipient.id)});
                    loadRecipients();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private boolean isDuplicate(String name, String account, int currentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = AccountReaderContract.RecipientEntry.COLUMN_NAME_OWNER_USERNAME + " = ? AND (" +
                AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME + " = ? OR " +
                AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM + " = ?) AND " +
                AccountReaderContract.RecipientEntry._ID + " != ?";
        String[] selectionArgs = {session.getUsername(), name, account, String.valueOf(currentId)};
        
        Cursor cursor = db.query(AccountReaderContract.RecipientEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        
        if (exists) {
            Toast.makeText(this, R.string.recipient_exists, Toast.LENGTH_SHORT).show();
        }
        return exists;
    }

    public static class Recipient {
        int id;
        String name;
        String accountNum;
        Recipient(int id, String name, String accountNum) {
            this.id = id;
            this.name = name;
            this.accountNum = accountNum;
        }
    }
}
