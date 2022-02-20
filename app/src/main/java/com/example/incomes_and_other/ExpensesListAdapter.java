package com.example.incomes_and_other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Expense> expenses;
    private static final String OTHER = "other";
    private static final String PRODUCTS = "products";
    private static final String ZKH = "ZKH";
    private static final String HEALTH = "health";
    private static final String CLOTHES = "clothes";
    private static final String ENTERTAINMENT = "entertainment";

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
        ImageView iV = holder.itemView.findViewById(R.id.TypeListItem);
        tv_date.setText(String.valueOf(expenses.get(position).getData()));
        tv_sum.setText(String.valueOf(expenses.get(position).getSumma()));

        Bitmap bm_prod = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_products);
        Bitmap bm_cloth = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_clothes);
        Bitmap bm_health = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_health);
        Bitmap bm_firewater = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.firewater);
        Bitmap fum = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.game_fun);
        Bitmap other = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_dotes);

        if (expenses.size() > position && expenses.get(position).getData() != null && expenses.get(position).getSumma() != 0 && expenses.get(position).getType() != null) {
            switch (expenses.get(position).getType()) {
                case PRODUCTS:
                    iV.setImageBitmap(bm_prod);
                    break;
                case CLOTHES:
                    iV.setImageBitmap(bm_cloth);
                    break;
                case HEALTH:
                    iV.setImageBitmap(bm_health);
                    break;
                case ZKH:
                    iV.setImageBitmap(bm_firewater);
                    break;
                case ENTERTAINMENT:
                    iV.setImageBitmap(fum);
                    break;
                case OTHER:
                    iV.setImageBitmap(other);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}