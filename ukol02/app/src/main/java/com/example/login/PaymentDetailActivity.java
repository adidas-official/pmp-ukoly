package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        goBack();
        getDataFromActivity();
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

    private void getDataFromActivity() {
        Intent i = this.getIntent();
        TextView tvid = findViewById(R.id.textView_id);

        if (i != null) {
            String id = i.getStringExtra("id");
            tvid.setText(id);
        }
    }
}