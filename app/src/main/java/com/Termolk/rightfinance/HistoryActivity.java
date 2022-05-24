package com.Termolk.rightfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbManager = DBManager.getInstance(this);
        ArrayList<Operation> operationArrayList = dbManager.getAllOperations();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        AdapterRecyclerViewForOperations adapterRecyclerViewForOperations = new AdapterRecyclerViewForOperations(operationArrayList);
        recyclerView.setAdapter(adapterRecyclerViewForOperations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}