package com.example.incomes_and_other;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class SpisokFragment2 extends Fragment {

    List<Income> incomes = new List<Income>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // начальная инициализация списка
        setInitialData();
        RecyclerView recyclerView = getView().findViewById(R.id.list_inc);
        // создаем адаптер
        IncomeAdapter adapter = new IncomeAdapter(this, incomes);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spisok2, container, false);
    }

    private void setInitialData(){

        incomes.add(new Income ("12.05.1999", 20000, "ЖКХ"));
        incomes.add(new Income ("24.02.2006", 1500000, "Другое"));
        incomes.add(new Income ("22.05.2014", 120, "Продукты"));
    }
}