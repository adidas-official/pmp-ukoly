package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Objekt trida UserActivity je predavan dalsi aktivite s udaji o prihlasenem uzivateli.
 * Flow aplikace je nasledujici:
 * - Prihlaseni uzivatele, udaje se nesou v objektu tridy UserSession (Transport pattern)
 * - Udaje o nove platbe se zadavaji ve formulari v aktivite SendPaymentActivity
 * - Po zadani uzivatel zkontroluje spravnost udaju a potvrdi platbu v aktivite CheckPaymentActivity
 */
public class UserActivity extends AppCompatActivity {
    private UserSession session;
    private AccountReaderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        session = (UserSession) getIntent().getSerializableExtra("user_session");
        // Updated to use Singleton
        dbHelper = AccountReaderDBHelper.getInstance(this);
        
        displayWelcomeMessage();
        displayAccountInfo();
        generateSummary();
        sendPaymentIntent();
        Logout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Since session data (like balance) might have changed in other activities, 
        // we refresh it from the database to keep the "Transport" object in sync.
        refreshSessionData();
        displayAccountInfo();
        generateSummary();
    }

    private void refreshSessionData() {
        if (session == null) return;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                AccountReaderContract.AccountReader.TABLE_NAME,
                new String[]{AccountReaderContract.AccountReader.COLUMN_NAME_BALANCE},
                AccountReaderContract.AccountReader.COLUMN_NAME_USERNAME + " = ?",
                new String[]{session.getUsername()},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            session.setBalance(cursor.getDouble(0));
        }
        cursor.close();
    }

    private void displayAccountInfo() {
        if (session == null) return;

        TextView tvAn = findViewById(R.id.textView_an);
        TextView tvBalance = findViewById(R.id.textView_balance);

        tvAn.setText(session.getAccountNumber());
        tvBalance.setText(String.format(Locale.getDefault(), "%.2f Kc", session.getBalance()));
    }

    public void generateSummary() {
        TableLayout summary = findViewById(R.id.tableLayout_summary);
        summary.removeAllViews();

        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        
        TextView h1 = new TextView(this); h1.setText(R.string.date);
        TextView h2 = new TextView(this); h2.setText(R.string.receiver);
        TextView h3 = new TextView(this); h3.setText(R.string.amount);
        
        headerRow.addView(h1);
        headerRow.addView(h2);
        headerRow.addView(h3);
        summary.addView(headerRow);

        if (session == null) return;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                AccountReaderContract.PaymentEntry._ID,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT,
                AccountReaderContract.PaymentEntry.COLUMN_NAME_DATE
        };

        String selection = AccountReaderContract.PaymentEntry.COLUMN_NAME_FROM_ACCOUNT + " = ?";
        String[] selectionArgs = { session.getAccountNumber() };

        Cursor cursor = db.query(
                AccountReaderContract.PaymentEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null,
                AccountReaderContract.PaymentEntry._ID + " DESC"
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry._ID));
            String toAccount = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_TO_ACCOUNT));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_AMOUNT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.PaymentEntry.COLUMN_NAME_DATE));

            addTableRow(summary, id, toAccount, amount, date);
        }
        cursor.close();
    }

    private void addTableRow(TableLayout summary, int id, String account, double amount, String date) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        
        TextView tvDate = new TextView(this);
        TextView tvAccount = new TextView(this);
        TextView tvPayment = new TextView(this);

        tvDate.setText(date);
        tvAccount.setText(account);
        tvPayment.setText(String.format(Locale.getDefault(), "-%.2f Kc", amount));

        tvDate.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tvAccount.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tvPayment.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        row.addView(tvDate);
        row.addView(tvAccount);
        row.addView(tvPayment);

        row.setTag(id);
        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(v -> showPaymentDetail((int) v.getTag()));
        summary.addView(row);
    }

    private void showPaymentDetail(int id) {
        Intent paymentDetail = new Intent(this, PaymentDetailActivity.class);
        paymentDetail.putExtra("id", String.valueOf(id));
        startActivity(paymentDetail);
    }
    
    private void Logout() {
        findViewById(R.id.button_logout).setOnClickListener(v -> finish());
    }

    private void sendPaymentIntent() {
        findViewById(R.id.button_sendMoney).setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, SendPaymentActivity.class);
            intent.putExtra("user_session", session);
            startActivity(intent);
        });
    }

    private void displayWelcomeMessage() {
        TextView userTv = findViewById(R.id.textView_welcome);
        if (session != null) {
            userTv.setText(getString(R.string.welcome, session.getUsername()));
        }
    }

    @Override
    protected void onDestroy() {
        // No need to close singleton dbHelper here
        super.onDestroy();
    }
}
