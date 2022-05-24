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

import com.google.gson.Gson;

import java.util.Collections;
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

    Gson gson = new Gson();

    final String SAVED_SERIALIZED_MAP = "saved_serialized_map";
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

//                    //Debug HashMap
//                    for (Map.Entry<String, Categories> entry:
//                            dataMoney.entrySet()) {
//                        Log.d("DataCategories",entry.getKey() + ":");
//                        Categories categories = entry.getValue();
//                        for (String item:
//                                categories.keySet()) {
//                            Log.d("DataCategories",item + " " + categories.get(item));
//                        }
//                    }
                    break;
            }
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMoneyValues();
    }

    private void saveMoneyValues() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        Gson gson = new Gson();
        MapWrapper wrapper = new MapWrapper();
        wrapper.setMyMap(MainActivity.dataMoney);
        String serializedMap = gson.toJson(wrapper);

        ed.putString(SAVED_MONEY, money + "");
        ed.putString(SAVED_SERIALIZED_MAP, serializedMap + "");
        Log.d("123", serializedMap);
        ed.commit();
    }

    private void loadMoneyValues() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_MONEY, "0");
        money = Integer.parseInt(savedText);
        textViewTotalMoney.setText(savedText);

        String wrapperStr = sPref.getString(SAVED_SERIALIZED_MAP, "{\"myMap\":{}}");
        MapWrapper wrapper = gson.fromJson(wrapperStr, MapWrapper.class);
        HashMap<String, Categories> savedMap = wrapper.getMyMap();
        MainActivity.dataMoney.clear();
        dataMoney.putAll(savedMap);

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
            saveMoneyValues();
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