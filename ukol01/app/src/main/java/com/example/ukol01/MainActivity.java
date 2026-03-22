package com.example.ukol01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToCategory(View v){
        Intent categoryActivity = new Intent("com.example.ukol01.categoryActivity");
        startActivity(categoryActivity);
    }


    public void onClick(View v) {
        final EditText editText_vyskaText = findViewById(R.id.editText_vyskaText);
        final EditText editText_vahaText = (EditText) findViewById(R.id.editText_vahaText);
        TextView textView_resutlBMI = findViewById(R.id.textView_resultBMI);

        try {
            double vyska = Double.parseDouble(editText_vyskaText.getText().toString())/100;
            double vaha = Double.parseDouble(editText_vahaText.getText().toString());
            double bmi = Math.round((vaha / (vyska * vyska)) * 100.0) / 100.0;


            if (bmi < 18.5) {
                textView_resutlBMI.setText("Tvoje BMI: " +bmi + "\nHodnocení : Podváha");
                textView_resutlBMI.setTextColor(Color.WHITE);
                textView_resutlBMI.setBackgroundResource(R.drawable.result_blue);
            } else if (bmi < 25) {
                textView_resutlBMI.setText("Tvoje BMI: " +bmi + "\nHodnocení : Normální váha");
                textView_resutlBMI.setTextColor(Color.WHITE);
                textView_resutlBMI.setBackgroundResource(R.drawable.result_green);
            } else if (bmi < 30) {
                textView_resutlBMI.setText("Tvoje BMI: " +bmi + "\nHodnocení : Nadváha");
                textView_resutlBMI.setTextColor(Color.WHITE);
                textView_resutlBMI.setBackgroundResource(R.drawable.result_orange);
            } else {
                textView_resutlBMI.setText("Tvoje BMI: " +bmi + "\nHodnocení : Obezita");
                textView_resutlBMI.setTextColor(Color.WHITE);
                textView_resutlBMI.setBackgroundResource(R.drawable.result_red);
            }


        } catch (NumberFormatException e) {
            Toast.makeText(this, "Zadej platná čísla", Toast.LENGTH_SHORT).show();
        }
    }

}