package com.example.incomes_and_other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter <IncomeAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Income> incomes;

    IncomeAdapter(Context context, List<Income> incomes) {
        this.incomes = incomes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.income_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncomeAdapter.ViewHolder holder, int position) {
        Income income = incomes.get(position);
        holder.dateView.setText(income.getData());
        holder.typeView.setText(income.getSumma());
        holder.summaView.setText(income.getType());
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView typeView, dateView, summaView;
        ViewHolder(View view){
            super(view);
            dateView = view.findViewById(R.id.date);
            typeView = view.findViewById(R.id.type);
            summaView = view.findViewById(R.id.summa);
        }
    }
}
