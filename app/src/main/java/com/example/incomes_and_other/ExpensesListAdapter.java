package com.example.incomes_and_other;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Expense> expenses;

    ExpensesListAdapter(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.list_item, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tv_date = holder.itemView.findViewById(R.id.DateListItem);
        TextView tv_sum = holder.itemView.findViewById(R.id.SumListItem);
        TextView tv_type = holder.itemView.findViewById(R.id.TypeListItem);
        tv_date.setText(String.valueOf(expenses.get(position).getData()));
        tv_sum.setText(String.valueOf(expenses.get(position).getSumma()));
        tv_type.setText(String.valueOf(expenses.get(position).getType()));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}