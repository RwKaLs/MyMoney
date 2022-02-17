package com.example.incomes_and_other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private ArrayList<Income> incomes;

    ListAdapter(Context context, ArrayList<Income> incomes) {
        this.incomes = incomes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Income income = incomes.get(position);
        holder.summaView.setText(income.getSumma());
        holder.typeView.setText(income.getType());
        holder.dataView.setText(income.getData());
    }

    @Override
    public int getItemCount() {     // TODO: разобраться, как подавать размер
        return incomes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView typeView, dataView, summaView;
        ViewHolder(View view){
            super(view);
            summaView = view.findViewById(R.id.SumListItem);
            typeView = view.findViewById(R.id.TypeListItem);
            dataView = view.findViewById(R.id.DateListItem);
        }
    }

}
