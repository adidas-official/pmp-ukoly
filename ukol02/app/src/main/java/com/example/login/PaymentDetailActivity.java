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
        Button cancel = (Button) findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent("com.example.login.UserActivity");
                startActivity(cancelIntent);
            }
        });
    }

    private void getDataFromActivity() {
        Intent i = this.getIntent();
        TextView tvid = (TextView) findViewById(R.id.textView_id);

        if (i != null) {
            String id = i.getStringExtra("id");
            tvid.setText(id);
        }
    }
}