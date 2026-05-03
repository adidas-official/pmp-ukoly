package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SendPaymentActivity extends AppCompatActivity {
    /**
     * aktivita vyuzivajici Prepravku pro snazsi praci pri sdileni mezi aktivitami.
     * Upgrade oproti predchozi verzi v ramci vyuziti databaze. Umoznuje nosit udaje s sebou mezi
     * aktivitami bez nutnosti vyptavani se DB na stejne data (cislo uctu, jmeno uzivatele atd.).
     * Poznamka se da zkopirovat a vlozi se do zpravy pro prijemce
     */
    private UserSession session;
    private AccountReaderDBHelper dbHelper;
    private final List<Recipient> recipientsList = new ArrayList<>();
    private ArrayAdapter<String> recipientAdapter;

    private EditText bankaccountEt;
    private Spinner bankcodeSpinner;
    private Spinner recipientsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_payment);
        
        session = (UserSession) getIntent().getSerializableExtra("user_session");
        dbHelper = AccountReaderDBHelper.getInstance(this);

        bankaccountEt = findViewById(R.id.editText_an);
        bankcodeSpinner = findViewById(R.id.spinner_bankcode);
        recipientsSpinner = findViewById(R.id.spinner_recipients);

        setupBankCodes();
        setupRecipientsSpinner();
        cancelPayment();
        createPayment();
        copyMessage();
    }

    private void setupBankCodes() {
        ArrayAdapter<CharSequence> codes = ArrayAdapter.createFromResource(this, R.array.bankcodes, android.R.layout.simple_spinner_item);
        codes.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bankcodeSpinner.setAdapter(codes);
    }

    private void setupRecipientsSpinner() {
        // Logika pro nacteni a zobrazeni seznamu prijemcu
        loadRecipients();
        List<String> names = new ArrayList<>();
        names.add(getString(R.string.new_recipient));
        for (Recipient r : recipientsList) {
            names.add(r.name + " (" + r.accountNum + ")");
        }

        recipientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        recipientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipientsSpinner.setAdapter(recipientAdapter);

        recipientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Recipient selected = recipientsList.get(position - 1);
                    String[] parts = selected.accountNum.split("/");
                    bankaccountEt.setText(parts[0]);
                    if (parts.length > 1) {
                        setSpinnerToValue(bankcodeSpinner, parts[1]);
                    }
                } else {
                    // "Novy prijemce" selected - clear fields
                    bankaccountEt.setText("");
                    bankcodeSpinner.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadRecipients() {
        // Nacteni prijemcu z databaze pro konkretniho uzivatele
        recipientsList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                AccountReaderContract.RecipientEntry.TABLE_NAME,
                null,
                AccountReaderContract.RecipientEntry.COLUMN_NAME_OWNER_USERNAME + " = ?",
                new String[]{session.getUsername()},
                null, null, null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.RecipientEntry.COLUMN_NAME_NAME));
            String acc = cursor.getString(cursor.getColumnIndexOrThrow(AccountReaderContract.RecipientEntry.COLUMN_NAME_ACCOUNT_NUM));
            recipientsList.add(new Recipient(name, acc));
        }
        cursor.close();
    }

    private void cancelPayment() {
        findViewById(R.id.button_cancel).setOnClickListener(v -> finish());
    }

    private void copyMessage() {
        Button copyBtn = findViewById(R.id.button_copy_message);
        EditText note = findViewById(R.id.editText_note);
        EditText note2rec = findViewById(R.id.editText_note2rec);
        copyBtn.setOnClickListener(v -> note2rec.setText(note.getText().toString()));
    }

    private void createPayment() {
        /*
         Uzivatel zadal udaje o platbe a chysta se zaplatit.
         V tomto kroku je overeno, zda ma dostatecny zustatek na uctu, pokud ano, je smerovan na
         aktivitu, kde potvrdi zadane udaje a platba se nasledne zpracuje.
         Vyjimka osetruje stav, kdy uzivatel zadal ne-cislo
        */
        EditText amountEt = findViewById(R.id.editText_amount);
        EditText ksEt = findViewById(R.id.editText_ks);
        EditText ssEt = findViewById(R.id.editText_ss);
        EditText vsEt = findViewById(R.id.editText_vs);
        EditText noteEt = findViewById(R.id.editText_note);
        EditText note2recEt = findViewById(R.id.editText_note2rec);

        findViewById(R.id.button_sendMoney).setOnClickListener(v -> {
            try {
                String amountStr = amountEt.getText().toString();
                if (amountStr.isEmpty()) {
                    Toast.makeText(this, R.string.numberFormatException, Toast.LENGTH_SHORT).show();
                    return;
                }
                double amount = Double.parseDouble(amountStr);
                if (session != null && amount <= session.getBalance()) {
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
                    Intent intent = new Intent(this, CheckPaymentActivity.class);
                    intent.putExtra("user_session", session);
                    intent.putExtra("payment_request", request);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Nedostatecny zustatek!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.numberFormatException, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private static class Recipient {
        String name;
        String accountNum;
        Recipient(String name, String accountNum) {
            this.name = name;
            this.accountNum = accountNum;
        }
    }
}
