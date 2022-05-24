package com.Termolk.rightfinance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    static class Categories extends HashMap<String, Integer> {

    }
    static HashMap<String, Categories> dataMoney = new HashMap<String, Categories>();

    TextView textViewTotalMoney;
    Button buttonSubtractMoney;
    Button buttonAddMoney;
    Button buttonResetValues;

    SharedPreferences sPref;

    Intent intent;

    final String SAVED_MONEY = "saved_money";
    final int REQUEST_CODE_CHANGE_MONEY = 1;
    
    static int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialViews();
        intent = new Intent(this, MoneyTransactions.class);
        loadMoneyValues();
        listenButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHANGE_MONEY:
                    money = data.getIntExtra("ChangedMoney", money);
                    textViewTotalMoney.setText(money + "");
                    saveMoneyValues();

                    //Debug HashMap
                    for (Map.Entry<String, Categories> entry:
                            dataMoney.entrySet()) {
                        Log.d("DataCategories",entry.getKey() + ":");
                        Categories categories = entry.getValue();
                        for (String item:
                                categories.keySet()) {
                            Log.d("DataCategories",item + " " + categories.get(item));
                        }
                    }
                    break;
            }
        } else {

        }
        saveMoneyValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMoneyValues();
    }

    private void saveMoneyValues() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_MONEY, money + "");
        ed.commit();
    }

    private void loadMoneyValues() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_MONEY, "0");
        money = Integer.parseInt(savedText);
        textViewTotalMoney.setText(savedText);
    }


    private void clickOnButtonAddMoney() {
        buttonAddMoney.setOnClickListener(view -> {
            intent.putExtra("action", "Добавить");
            startActivityForResult(intent, REQUEST_CODE_CHANGE_MONEY);
        });
    }

    private void clickOnButtonSubtractMoney() {
        buttonSubtractMoney.setOnClickListener(view -> {
            intent.putExtra("action", "Вычесть");
            startActivityForResult(intent, REQUEST_CODE_CHANGE_MONEY);
        });
    }

    private void clickOnButtonResetValues() {
        buttonResetValues.setOnClickListener(view -> {
            money = 0;
            dataMoney.clear();
            textViewTotalMoney.setText("0");
        });
    }

    private void listenButtons() {
        clickOnButtonAddMoney();
        clickOnButtonSubtractMoney();
        clickOnButtonResetValues();
    }

    private void initialViews() {
        textViewTotalMoney = findViewById(R.id.textViewTotalMoney);
        buttonSubtractMoney = findViewById(R.id.buttonSubtractMoney);
        buttonAddMoney = findViewById(R.id.buttonAddMoney);
        buttonResetValues = findViewById(R.id.buttonResetValues);
    }

}

