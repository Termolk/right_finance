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

public class MainActivity extends AppCompatActivity {

    TextView textViewTotalMoney;
    Button buttonSubtractMoney;
    Button buttonAddMoney;

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
        listenButtons();
        loadMoneyValues();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHANGE_MONEY:
                    money = data.getIntExtra("ChangedMoney", 0);
                    textViewTotalMoney.setText(money + "");
                    break;
            }
        } else {
            Toast.makeText(this, "Неверный резалт", Toast.LENGTH_SHORT).show();
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

    private void listenButtons() {
        clickOnButtonAddMoney();
        clickOnButtonSubtractMoney();
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

    private void initialViews() {
        textViewTotalMoney = findViewById(R.id.textViewTotalMoney);
        buttonSubtractMoney = findViewById(R.id.buttonSubtractMoney);
        buttonAddMoney = findViewById(R.id.buttonAddMoney);
    }

}