package com.test_1;

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

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        EditText firstNumberEditText = findViewById(R.id.firstNumber);
        EditText secondNumberEditText = findViewById(R.id.secondNumber);

        Spinner spinner = findViewById(R.id.spinner);
        String[] operations = {"Plus", "Minus", "Inmultire", "Impartire"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, operations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                operation = "Plus";
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            String firstNumberString = firstNumberEditText.getText().toString();
            double firstNumber = 0;
            if (!"".equals(firstNumberString)) {
                firstNumber = Double.parseDouble(firstNumberString);
            }

            String secondNumberString = secondNumberEditText.getText().toString();
            double secondNumber = 0;
            if (!"".equals(secondNumberString)) {
                secondNumber = Double.parseDouble(secondNumberString);
            }

            String result = compute(firstNumber, secondNumber);
            if (result.equals("Impartire la zero")) {
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

    private String compute(double firstNumber, double secondNumber) {
        if (operation.equals("Impartire") && secondNumber == 0) {
            return "Impartire la zero";
        }
        switch (operation) {
            case "Minus":
                return firstNumber + " - " + secondNumber + " = " + (firstNumber - secondNumber);
            case "Inmultire":
                return firstNumber + " * " + secondNumber + " = " + (firstNumber * secondNumber);
            case "Impartire":
                return firstNumber + " / " + secondNumber + " = " + (firstNumber / secondNumber);
            default:
                return firstNumber + " + " + secondNumber + " = " + (firstNumber + secondNumber);
        }
    }
}