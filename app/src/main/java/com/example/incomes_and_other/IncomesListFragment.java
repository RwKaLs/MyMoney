package com.example.incomes_and_other;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class IncomesListFragment extends Fragment {

    ArrayList<Income> incomes = new ArrayList<Income>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_incomes_list, container, false);

        // начальная инициализация списка
        setInitialData();
        RecyclerView recyclerView = view.findViewById(R.id.list_Exp);
        // создаем адаптер
        ListAdapter adapter = new ListAdapter(this.getContext(), incomes);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void setInitialData(){
        // Инициализация данных вручную для проверки
        incomes.add(new Income ("12.05.1999", 100, "Другое"));
        incomes.add(new Income ("24.02.2006", 1000, "ЖКХ"));
        incomes.add(new Income ("01.01.2000", 550, "Другое"));
        incomes.add(new Income ("07.10.2020", 25000, "Продукты"));
    }
}