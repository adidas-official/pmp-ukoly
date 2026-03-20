package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        displayWelcomeMessage();
        generateSummary();
        sendPaymentIntent();
        Logout();
    }

    public void generateSummary() {
//        Tohle se bude brat z databaze z tabulky payments
        String[] payments = {"1500 Kc", "200 Kc", "1420 Kc"};
        String[] accounts = {"2800123456/2010", "55001294421/5510", "8002101092/0800"};
        String[] dates = {"02.03.2026", "03.03.2026", "04.03.2026"};

        TableLayout summary = findViewById(R.id.tableLayout_summary);
        for (int i = 0; i < 3; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            TextView tvPayment = new TextView(this);
            TextView tvAccount = new TextView(this);
            TextView tvDate = new TextView(this);

            tvPayment.setText(payments[i]);
            tvAccount.setText(accounts[i]);
            tvDate.setText(dates[i]);

            tvPayment.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            tvAccount.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            tvDate.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            row.addView(tvDate);
            row.addView(tvAccount);
            row.addView(tvPayment);

            row.setTag(i);
            row.setClickable(true);
            row.setFocusable(true);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowPaymentClick(v);
                }
            });
            summary.addView(row);
        }
    }

    public void rowPaymentClick(View view) {
        String tagId = view.getTag().toString();
        int id = Integer.parseInt(tagId);
        showPaymentDetail(id);
    }

    private void showPaymentDetail(int id) {
        Intent paymentDetail = new Intent("com.example.login.PaymentDetailActivity" );
        paymentDetail.putExtra("id", Integer.toString(id));
        startActivity(paymentDetail);
    }
    private void Logout() {

        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent("com.example.login.LoginActivity");
                startActivity(logout);
            }
        });
    }

    private void sendPaymentIntent() {
        Button sendPayment = findViewById(R.id.button_sendMoney);
        sendPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendPaymentActivity = new Intent("com.example.login.SendPaymentActivity");
                startActivity(sendPaymentActivity);
            }
        });
    }

    private void displayWelcomeMessage() {
        TextView user = findViewById(R.id.textView_welcome);
        Intent login_success = getIntent();
        String username = login_success.getStringExtra("username");
        if (username != null) {
            user.setText(getString(R.string.welcome, username));
        }
    }
}