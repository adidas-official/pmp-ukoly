package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SendPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_payment);
        cancelPayment();
        createPayment();
        copyMessage();
    }

    private void cancelPayment() {
        Button cancel = (Button) findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent("com.example.login.UserActivity");
                startActivity(cancelIntent);
            }
        });
    }

    private void copyMessage() {
        Button copy_message_button = (Button) findViewById(R.id.button_copy_message);
        EditText note = (EditText) findViewById(R.id.editText_note);
        EditText note2rec = (EditText) findViewById(R.id.editText_note2rec);

        copy_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note2rec.setText(note.getText().toString());
            }
        });
    }


    private void createPayment() {
        EditText bankaccount = (EditText) findViewById(R.id.editText_an);
        Spinner bankcode = (Spinner) findViewById(R.id.spinner_bankcode);

        ArrayAdapter<CharSequence> codes = ArrayAdapter.createFromResource(this, R.array.bankcodes, android.R.layout.simple_spinner_item);
        codes.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bankcode.setAdapter(codes);

        EditText amount = (EditText) findViewById(R.id.editText_amount);
        EditText ks = (EditText) findViewById(R.id.editText_ks);
        EditText ss = (EditText) findViewById(R.id.editText_ss);
        EditText vs = (EditText) findViewById(R.id.editText_vs);
        EditText note = (EditText) findViewById(R.id.editText_note);
        EditText note2rec = (EditText) findViewById(R.id.editText_note2rec);
        double balance = this.getBalance();

        Button create_payment = (Button) findViewById(R.id.button_sendMoney);
        create_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        kontrola, jestli je zadane cislo
                try {
                    double payment = Double.parseDouble(amount.getText().toString());
                    if (payment < balance) {
                        Intent checkPayment = new Intent("com.example.login.CheckPaymentActivity");
                        checkPayment.putExtra("payment", payment);
                        checkPayment.putExtra("bankaccount", bankaccount.getText().toString());
                        checkPayment.putExtra("bankcode", bankcode.getSelectedItem().toString());
                        checkPayment.putExtra("ks", ks.getText().toString());
                        checkPayment.putExtra("ss", ss.getText().toString());
                        checkPayment.putExtra("vs", vs.getText().toString());
                        checkPayment.putExtra("note", note.getText().toString());
                        checkPayment.putExtra("note2rec", note2rec.getText().toString());
                        startActivity(checkPayment);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(SendPaymentActivity.this, getResources().getString(R.string.numberFormatException), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private int getBalance() {
        return 12000;
    }

}
