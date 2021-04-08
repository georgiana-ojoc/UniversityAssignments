package com.test_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private String from;
    private String to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        EditText numberEditText = findViewById(R.id.number);

        String[] operations = {"km", "m", "cm", "mm", "inch"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, operations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner fromSpinner = findViewById(R.id.from);
        fromSpinner.setAdapter(adapter);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                from = "km";
            }
        });

        Spinner toSpinner = findViewById(R.id.to);
        toSpinner.setAdapter(adapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                to = "km";
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            String numberString = numberEditText.getText().toString();
            double number = 0;
            if (!"".equals(numberString)) {
                number = Double.parseDouble(numberString);
            }

            String result = compute(number);
            if (result.equals("Conversie intre aceleasi metrici")) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        R.style.AlertDialog);
                builder.setTitle("Rezultat")
                        .setMessage(result)
                        .setPositiveButton("Share", (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Rezultat");
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, result);
                            startActivity(Intent.createChooser(intent, "Rezultat"));
                        }).setNegativeButton("Ok", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    TextView message = alertDialog.findViewById(android.R.id.message);
                    message.setGravity(Gravity.CENTER);

                    Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(Color.BLACK);

                    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setTextColor(Color.BLACK);

                    LinearLayout.LayoutParams layout = new LinearLayout
                            .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 10);
                    layout.gravity = Gravity.CENTER;
                    negativeButton.setLayoutParams(layout);
                    positiveButton.setLayoutParams(layout);
                });
                alertDialog.show();
            }
        });
    }

    private String compute(double number) {
        if (from.equals(to)) {
            return "Conversie intre aceleasi metrici";
        }
        double result = number;
        switch (from) {
            case "km":
                result *= 100000;
                break;
            case "m":
                result *= 100;
                break;
            case "mm":
                result /= 10;
                break;
            case "inch":
                result *= 2.54;
        }
        switch (to) {
            case "km":
                result /= 100000;
                break;
            case "m":
                result /= 100;
                break;
            case "mm":
                result *= 10;
                break;
            case "inch":
                result /= 2.54;
        }
        return number + " " + from + " = " + new DecimalFormat(".00").format(result)
                + ' ' + to;
    }
}