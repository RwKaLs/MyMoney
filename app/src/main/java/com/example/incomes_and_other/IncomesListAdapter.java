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

public class IncomesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Income> incomes;

    IncomesListAdapter(ArrayList<Income> incomes) {
        this.incomes = incomes;
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
        tv_date.setText(String.valueOf(incomes.get(position).getData()));
        tv_sum.setText(String.valueOf(incomes.get(position).getSumma()));

        Bitmap bm_sal = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_money_hand);
        Bitmap bm_gift = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.gift);
        Bitmap bm_other = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_dotes);
        if (incomes.size() > position && incomes.get(position).getData() != null && incomes.get(position).getSumma() != 0 && incomes.get(position).getType() != null) {
            switch (incomes.get(position).getType()) {
                case "Зарплата":
                    iV.setImageBitmap(bm_sal);
                    break;
                case "Подарок":
                    iV.setImageBitmap(bm_gift);
                    break;
                case "Другое":
                    iV.setImageBitmap(bm_other);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }
}
