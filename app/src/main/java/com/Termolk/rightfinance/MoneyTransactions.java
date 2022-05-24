package com.Termolk.rightfinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        if(arguments!=null){
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
                if (action.equals("Добавить")) {
                        intent.putExtra("ChangedMoney", MainActivity.money + selectedNumber);
                }
                else if (action.equals("Вычесть")) {
                    if (MainActivity.money - selectedNumber < 0 ){
                        Toast.makeText(this, "Недостаточно средств для списания", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        intent.putExtra("ChangedMoney", MainActivity.money - selectedNumber);
                    }
                }
                putValuesIntoHashMap();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Вы не указали числовое значение", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillSpinner() {
        if (action.equals("Добавить")) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesAdd);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setPrompt("Доход");
        } else if (action.equals("Вычесть")) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesSub);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setPrompt("Расход");
        }
    }

    private void initialViews() {
        textViewAction = findViewById(R.id.textViewAction);
        editTextMoney = findViewById(R.id.editTextMoney);
        buttonChange = findViewById(R.id.buttonChange);
        spinner = findViewById(R.id.spinnerCategories);
    }

    private void putValuesIntoHashMap() {
        if (!MainActivity.dataMoney.containsKey(action)) {
            MainActivity.dataMoney.put(action, new MainActivity.Categories());
        }
        MainActivity.Categories categories = MainActivity.dataMoney.get(action);
        if (!categories.containsKey(selectedCategory)) {
            categories.put(selectedCategory, selectedNumber);
        }
        else categories.put(selectedCategory, categories.get(selectedCategory) + selectedNumber);
    }
}