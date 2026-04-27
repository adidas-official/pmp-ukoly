package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        getDataFromActivity();
        goBack();
        confirm();

    }

    private void goBack() {
        Button cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void confirm() {
        Button confirm = findViewById(R.id.button_confirm);
//        TODO: when db complete insert data into db, calculate balance
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmPayment = new Intent("com.example.login.UserActivity");
                startActivity(confirmPayment);
            }
        });
    }

    private void getDataFromActivity() {
        Intent i = getIntent();
        TextView amountTV = findViewById(R.id.textView_amount);
        TextView bankcodeTV = findViewById(R.id.textView_bankaccount);
        if (i != null ) {
            double payment = i.getDoubleExtra("payment", 0.0);
            String code = i.getStringExtra("bankcode");
            amountTV.setText(String.valueOf(payment));
            bankcodeTV.setText(code);
        }
    }
}