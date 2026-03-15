package com.example.ukol01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_calculate = (Button) findViewById(R.id.button_calculate);

        button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText_vyskaText = (EditText) findViewById(R.id.textView_vyskaText);
                final EditText editText_vahaText = (EditText) findViewById(R.id.textView_vahaText);
                final TextView textView_resutlBMI = (TextView) findViewById(R.id.editText_resultBMI);

                String vahaText = editText_vahaText.getText().toString();
                float vaha = Float.parseFloat(vahaText);

                String vyskaText = editText_vyskaText.getText().toString();
                float vyska = Float.parseFloat(vyskaText);

                float bmi = vaha / (vyska * vyska);
                textView_resutlBMI.setText(String.valueOf(bmi));

            }
        });
    }





}
