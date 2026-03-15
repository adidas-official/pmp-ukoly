package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        displayWelcomeMessage();
        sendPaymentIntent();
        Logout();
    }

    private void Logout() {

        Button logoutButton = (Button) findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent("com.example.login.LoginActivity");
                startActivity(logout);
            }
        });
    }

    private void sendPaymentIntent() {
        Button sendPayment = (Button) findViewById(R.id.button_sendMoney);
        sendPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendPaymentActivity = new Intent("com.example.login.SendPaymentActivity");
                startActivity(sendPaymentActivity);
            }
        });
    }

    private void displayWelcomeMessage() {
        TextView user = (TextView) findViewById(R.id.textView_welcome);
        Intent login_success = getIntent();
        String username = login_success.getStringExtra("username");
        if (username != null) {
            user.setText(getString(R.string.welcome, username));
            Toast.makeText(UserActivity.this, username, Toast.LENGTH_LONG).show();
        }
    }
}