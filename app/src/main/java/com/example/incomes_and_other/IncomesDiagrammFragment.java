package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class IncomesDiagrammFragment extends Fragment {

    private PieChart chart;
    private DBHelper dbHelperINC;
    private ArrayList<Income> incomesData;
    int sumSalary, sumPresent, sumOther;
    private static final String SALARY = "salary";
    private static final String PRESENT = "present";
    private static final String OTHER = "other";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomesData = new ArrayList<>();
        dbHelperINC = new DBHelper(this.getContext(), DBHelper.STR_INC);
        loadDb();
        sumSalary = 0;
        sumPresent = 0;
        sumOther = 0;
        for (Income i : incomesData){
            switch (i.getType()){
                case SALARY:
                    sumSalary += i.getSumma();
                    break;
                case PRESENT:
                    sumPresent += i.getSumma();
                    break;
                case OTHER:
                    sumOther += i.getSumma();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_incomes_diagramm, container, false);

        chart = view.findViewById(R.id.chart_inc);

        ArrayList<PieEntry> yVals = new ArrayList<>();
        if (sumSalary != 0) {
            yVals.add(new PieEntry(sumSalary, "Зарплата"));
        }
        if (sumPresent != 0) {
            yVals.add(new PieEntry(sumPresent, "Подарок"));    // Здесь происходит инициализация данных в диаграмме
        }
        if (sumOther != 0) {
            yVals.add(new PieEntry(sumOther, "Другое"));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ff7f01"));
        colors.add(Color.parseColor("#00ff01"));
        colors.add(Color.parseColor("#4b0082"));   // Цвета диаграммы

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);

        chart.setData(pieData);


        String descriptionStr = "Доли доходов";
        Description description = new Description();     // Подписи к диаграмме
        description.setText(descriptionStr);
        chart.setDescription(description);

        return view;
    }
    @SuppressLint("Recycle")
    private void loadDb(){
        SQLiteDatabase sqlLoad = dbHelperINC.getWritableDatabase();
        Cursor cursor = sqlLoad.query(DBHelper.STR_INC, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int summa = cursor.getColumnIndex(DBHelper.KEY_SUMMA);
            int type = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            do {
                incomesData.add(new Income(cursor.getString(date), cursor.getInt(summa), cursor.getString(type)));
//                    Log.d("DBLOG", "DATE = " + cursor.getString(date) + " SUMMA = " +
//                            cursor.getInt(summa) + " TYPE = " + cursor.getString(type));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBLOG", "NODATA");
        }
    }
}