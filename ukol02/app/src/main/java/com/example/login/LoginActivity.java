package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView attempts;
    private Button submit_btn;
    
    private int attempt_counter = 5;
    private AccountReaderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Updated to use Singleton
        dbHelper = AccountReaderDBHelper.getInstance(this);

        username = findViewById(R.id.editText_username);
        password = findViewById(R.id.editText_password);
        submit_btn = findViewById(R.id.button_submit);
        attempts = findViewById(R.id.editText_attempts);
        
        attempts.setText(String.valueOf(attempt_counter));

        setupLoginButton();
    }

    private void setupLoginButton() {
        submit_btn.setOnClickListener(v -> {
            String userStr = username.getText().toString();
            String passStr = password.getText().toString();

            UserSession session = attemptLogin(userStr, passStr);
            if (session != null) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                intent.putExtra("user_session", session);
                startActivity(intent);
            } else {
                attempt_counter--;
                Toast.makeText(LoginActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                attempts.setText(String.valueOf(attempt_counter));

                if (attempt_counter <= 0 ) {
                    submit_btn.setEnabled(false);
                }
            }
        });
    }

    private UserSession attemptLogin(String user, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                AccountReaderContract.AccountReader.COLUMN_NAME_ACCOUNT_NUM,
                AccountReaderContract.AccountReader.COLUMN_NAME_BALANCE,
                AccountReaderContract.AccountReader.COLUMN_NAME_BANK_CODE_OWNER
        };

        String selection = AccountReaderContract.AccountReader.COLUMN_NAME_USERNAME + " = ? AND " +
                AccountReaderContract.AccountReader.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = { user, pass };

        Cursor cursor = db.query(
                AccountReaderContract.AccountReader.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );

        UserSession session = null;
        if (cursor.moveToFirst()) {
            String accNum = cursor.getString(0);
            double balance = cursor.getDouble(1);
            int bankCode = cursor.getInt(2);
            session = new UserSession(user, accNum, balance, bankCode);
        }
        cursor.close();
        return session;
    }

    @Override
    protected void onDestroy() {
        // No need to close singleton dbHelper here
        super.onDestroy();
    }
}
