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
    private static final String SALARY = "salary";
    private static final String PRESENT = "present";
    private static final String OTHER = "other";

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
        String[] dateOutf = incomes.get(position).getData().split("-");
        String dateOut = dateOutf[2] + "." + dateOutf[1] + "." + dateOutf[0];
        tv_date.setText(dateOut);
        tv_sum.setText(String.valueOf(incomes.get(position).getSumma()));

        Bitmap bm_sal = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_money_hand);
        Bitmap bm_gift = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.gift);
        Bitmap bm_other = BitmapFactory.decodeResource(holder.itemView.getResources(), R.drawable.m_dotes);
        if (incomes.size() > position && incomes.get(position).getData() != null && incomes.get(position).getSumma() != 0 && incomes.get(position).getType() != null) {
            switch (incomes.get(position).getType()) {
                case SALARY:
                    iV.setImageBitmap(bm_sal);
                    break;
                case PRESENT:
                    iV.setImageBitmap(bm_gift);
                    break;
                case OTHER:
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
