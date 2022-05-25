package com.Termolk.rightfinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MoneyTransactions extends AppCompatActivity {
    TextView textViewAction;
    EditText editTextMoney;
    Button buttonChange;
    Spinner spinner;

    Resources res;

    String[] categoriesSub;
    String[] categoriesAdd;


    String selectedCategory;
    int selectedNumber;

    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transactions);

        res = getResources();
        categoriesSub = res.getStringArray(R.array.categories_sub);
        categoriesAdd = res.getStringArray(R.array.categories_add);

        initialViews();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            action = arguments.get("action").toString();
            textViewAction.setText(action);
        }
        fillSpinner();
        clickOnButtonChange();

    }

    private void clickOnButtonChange() {
        buttonChange.setOnClickListener(view -> {
            if (!editTextMoney.getText().toString().equals("")) {
                selectedCategory = spinner.getSelectedItem().toString();
                selectedNumber = Integer.parseInt(editTextMoney.getText().toString());
                Intent intent = new Intent();
                if (action.equals(getResources().getString(R.string.add))) {
                    if (selectedNumber + MainActivity.money <= 1000000000) {
                        intent.putExtra("ChangedMoney", MainActivity.money + selectedNumber);
                        MainActivity.dbManager.addOperation(selectedCategory, selectedNumber);
                        putValuesIntoHashMap();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.much_money), Toast.LENGTH_SHORT).show();
                        MainActivity.dataMoney.clear();
                        MainActivity.money = 0;
                        MainActivity.dbManager.clearTable();
                    }

                } else if (action.equals(getResources().getString(R.string.substract))) {
                    if (MainActivity.money - selectedNumber < 0) {
                        Toast.makeText(this, getResources().getString(R.string.havent_money), Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("ChangedMoney", MainActivity.money - selectedNumber);
                        MainActivity.dbManager.addOperation(selectedCategory, selectedNumber);
                        putValuesIntoHashMap();
                    }
                }
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.need_numbers), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillSpinner() {
        if (action.equals(getResources().getString(R.string.add))) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categoriesAdd);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setPrompt(getResources().getString(R.string.income));
        } else if (action.equals(getResources().getString(R.string.substract))) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categoriesSub);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setPrompt(getResources().getString(R.string.consumption));
        }
    }

    private void putValuesIntoHashMap() {
        if (!MainActivity.dataMoney.containsKey(action)) {
            MainActivity.dataMoney.put(action, new MainActivity.Categories());
        }
        MainActivity.Categories categories = MainActivity.dataMoney.get(action);
        if (!categories.containsKey(selectedCategory)) {
            categories.put(selectedCategory, selectedNumber);
        } else {
            if (categories.get(selectedCategory) + selectedNumber <= 1000000000) {
                categories.put(selectedCategory, categories.get(selectedCategory) + selectedNumber);
            } else {
                Toast.makeText(this, getResources().getString(R.string.much_money), Toast.LENGTH_SHORT).show();
                MainActivity.dataMoney.clear();
                MainActivity.money = 0;
                Intent intent = new Intent(this, MainActivity.class);
                MainActivity.dbManager.clearTable();
                intent.putExtra("ChangedMoney", MainActivity.money + 0);
                finish();
            }
        }
    }

    private void initialViews() {
        textViewAction = findViewById(R.id.textViewAction);
        editTextMoney = findViewById(R.id.editTextMoney);
        buttonChange = findViewById(R.id.buttonChange);
        spinner = findViewById(R.id.spinnerCategories);
    }
}