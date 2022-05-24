package com.Termolk.rightfinance;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyTransactions extends AppCompatActivity {
    TextView textViewAction;
    EditText editTextMoney;
    Button buttonChange;

    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transactions);

        initialViews();

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            action = arguments.get("action").toString();
            textViewAction.setText(action);
        }
        clickOnButtonChange();

    }


    private void clickOnButtonChange() {
        buttonChange.setOnClickListener(view -> {
            if (!editTextMoney.getText().toString().equals("")) {
                Intent intent = new Intent();
                if (action.equals("Добавить")) {
                    intent.putExtra("ChangedMoney", MainActivity.money + Integer.parseInt(editTextMoney.getText().toString()));
                } else if (action.equals("Вычесть")) {
                    if (MainActivity.money - Integer.parseInt(editTextMoney.getText().toString()) <= 0 ){
                        intent.putExtra("ChangedMoney", 0);
                    }
                    else {
                        intent.putExtra("ChangedMoney", MainActivity.money - Integer.parseInt(editTextMoney.getText().toString()));
                    }

                }
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Вы не указали числовое значение", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialViews() {
        textViewAction = findViewById(R.id.textViewAction);
        editTextMoney = findViewById(R.id.editTextMoney);
        buttonChange = findViewById(R.id.buttonChange);
    }


}