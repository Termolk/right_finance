package com.Termolk.rightfinance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    static DBManager dbManager;

    static class Categories extends HashMap<String, Integer> {

    }

    static HashMap<String, Categories> dataMoney = new HashMap<String, Categories>();


    TextView textViewTotalMoney;
    Button buttonSubtractMoney;
    Button buttonAddMoney;
    Button buttonResetValues;
    Button buttonShowDiagram;
    Button buttonShowHistory;

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
        dbManager = DBManager.getInstance(this);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.privatbank.ua")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


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

    private void listenButtons() {
        clickOnButtonAddMoney();
        clickOnButtonSubtractMoney();
        clickOnButtonResetValues();
        clickOnbuttonShowDiagram();
        clickOnbuttonShowHistory();
    }

    private void initialViews() {
        textViewTotalMoney = findViewById(R.id.textViewTotalMoney);
        buttonSubtractMoney = findViewById(R.id.buttonSubtractMoney);
        buttonAddMoney = findViewById(R.id.buttonAddMoney);
        buttonResetValues = findViewById(R.id.buttonResetValues);
        buttonShowDiagram = findViewById(R.id.buttonShowDiagram);
        buttonShowHistory = findViewById(R.id.buttonShowHistory);
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

    private void clickOnbuttonShowDiagram() {
        buttonShowDiagram.setOnClickListener(view -> {
            Intent digIntent = new Intent(this, DiagramActivity.class);
            startActivity(digIntent);
        });

    }private void clickOnbuttonShowHistory() {
        buttonShowHistory.setOnClickListener(view -> {
            Intent hisIntent = new Intent(this, HistoryActivity.class);
            startActivity(hisIntent);
        });
    }

    private void clickOnButtonResetValues() {
        buttonResetValues.setOnClickListener(view -> {
            money = 0;
            dataMoney.clear();
            textViewTotalMoney.setText("0");
            dbManager.clearTable();
            saveMoneyValues();
        });
    }
}