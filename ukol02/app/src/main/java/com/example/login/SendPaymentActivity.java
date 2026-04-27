package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SendPaymentActivity extends AppCompatActivity {
    /**
     * aktivita vyuzivajici Prepravku pro snazsi praci pri sdileni mezi aktivitami.
     * Upgrade oproti predchozi verzi v ramci vyuziti databaze. Umoznuje nosit udaje s sebou mezi
     * aktivitami bez nutnosti vyptavani se DB na stejne data (cislo uctu, jmeno uzivatele atd.).
     */
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_payment);
        
        session = (UserSession) getIntent().getSerializableExtra("user_session");
        
        cancelPayment();
        createPayment();
        copyMessage();
    }

    private void cancelPayment() {
        Button cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(v -> finish());
    }

    private void copyMessage() {
        Button copy_message_button = findViewById(R.id.button_copy_message);
        EditText note = findViewById(R.id.editText_note);
        EditText note2rec = findViewById(R.id.editText_note2rec);

        copy_message_button.setOnClickListener(v -> note2rec.setText(note.getText().toString()));
    }

    /**
     * Uzivatel zadal udaje o platbe a chysta se zaplatit.
     * V tomto kroku je overeno, zda ma dostatecny zustatek na uctu, pokud ano, je smerovan na
     * aktivitu, kde potvrdi zadane udaje a platba se nasledne zpracuje.
     * Vyjimka osetruje stav, kdy uzivatel zadal ne-cislo
     */
    private void createPayment() {
        EditText bankaccountEt = findViewById(R.id.editText_an);
        Spinner bankcodeSpinner = findViewById(R.id.spinner_bankcode);

        ArrayAdapter<CharSequence> codes = ArrayAdapter.createFromResource(this, R.array.bankcodes, android.R.layout.simple_spinner_item);
        codes.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bankcodeSpinner.setAdapter(codes);

        EditText amountEt = findViewById(R.id.editText_amount);
        EditText ksEt = findViewById(R.id.editText_ks);
        EditText ssEt = findViewById(R.id.editText_ss);
        EditText vsEt = findViewById(R.id.editText_vs);
        EditText noteEt = findViewById(R.id.editText_note);
        EditText note2recEt = findViewById(R.id.editText_note2rec);
        
        Button sendBtn = findViewById(R.id.button_sendMoney);
        sendBtn.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(amountEt.getText().toString());

                if (session != null && amount <= session.getBalance()) {
                    // Create PaymentRequest transport object
                    PaymentRequest request = new PaymentRequest(
                            bankaccountEt.getText().toString(),
                            bankcodeSpinner.getSelectedItem().toString(),
                            amount,
                            ksEt.getText().toString(),
                            ssEt.getText().toString(),
                            vsEt.getText().toString(),
                            noteEt.getText().toString(),
                            note2recEt.getText().toString()
                    );

                    Intent intent = new Intent(SendPaymentActivity.this, CheckPaymentActivity.class);
                    intent.putExtra("user_session", session);
                    intent.putExtra("payment_request", request);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Insufficient funds!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
