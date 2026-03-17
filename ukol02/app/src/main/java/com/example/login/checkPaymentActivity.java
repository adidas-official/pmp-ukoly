package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class checkPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        getDataFromActivity();

    }

    private void getDataFromActivity() {
        Intent i = getIntent();
        TextView amountTV = (TextView) findViewById(R.id.textView_amount);
        TextView bankcodeTV = (TextView) findViewById(R.id.textView_bankcode);
        if (i != null ) {
            double payment = i.getDoubleExtra("payment", 0.0);
            String code = i.getStringExtra("bankcode");
            amountTV.setText(String.valueOf(payment));
            bankcodeTV.setText(code);
        }
    }
}