package com.Termolk.rightfinance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecyclerViewForOperations extends RecyclerView.Adapter<AdapterRecyclerViewForOperations.ViewHolder> {
    LayoutInflater layoutInflater;
    ArrayList<Operation> operations;

    public AdapterRecyclerViewForOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategory;
        TextView textViewCountMoney;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewCountMoney = itemView.findViewById(R.id.textViewCountMoney);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_view_for_operations, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textViewCategory = holder.textViewCategory;
        TextView textViewCountMoney = holder.textViewCountMoney;

        String category = operations.get(position).operation;
        String countMoney = operations.get(position).money + "";

        textViewCategory.setText(category);
        textViewCountMoney.setText(countMoney);
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }
}
