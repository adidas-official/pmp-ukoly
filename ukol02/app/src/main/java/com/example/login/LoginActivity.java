package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView attempts;
    private Button submit_btn;
    private static String uname;
    private static String pwd;
    int attempt_counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login();
        Account account = new Account("280011223344", 2010, 100000.0,"harrypotter", "voldemortsux");
        uname = account.getUsername();
        pwd = account.getPassword();
        try (dbConnect db = new dbConnect(this)) {
            db.addAccount(account);
        }
    }

    public void Login() {
        username = (EditText) findViewById(R.id.editText_username);
        password = (EditText) findViewById(R.id.editText_password);
        submit_btn = (Button) findViewById(R.id.button_submit);
        attempts = (EditText) findViewById(R.id.editText_attempts);
        attempts.setText(Integer.toString(attempt_counter));

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin(username, password)) {
                    Intent login_success = new Intent("com.example.login.UserActivity");
                    login_success.putExtra("username", username.getText().toString());
                    startActivity(login_success);
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect credentials, remaining attempts: ".concat(Integer.toString(--attempt_counter)), Toast.LENGTH_SHORT).show();
                    attempts.setText(Integer.toString(attempt_counter));

                    if (attempt_counter == 0 ) {
                        Toast.makeText(LoginActivity.this, "Sorry, you are blocked", Toast.LENGTH_LONG).show();
                        submit_btn.setEnabled(false);
                    }
                }

            }
        });


    }

    private Boolean checkLogin(EditText username, EditText password) {
        if (username.getText().toString().equals(uname) && password.getText().toString().equals(pwd)) {
            return true;
        }
        return false;
    }
}