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

public class ExpensesDiagrammFragment extends Fragment {

    private PieChart chart;
    private DBHelper dbHelperEXP;
    private ArrayList<Expense> expensesData;
    int sumProduct, sumZKH, sumHealth, sumClothes, sumOther, sumEntertainment;
    private static final String OTHER = "other";
    private static final String PRODUCTS = "products";
    private static final String ZKH = "ZKH";
    private static final String HEALTH = "health";
    private static final String CLOTHES = "clothes";
    private static final String ENTERTAINMENT = "entertainment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expensesData = new ArrayList<>();
        dbHelperEXP = new DBHelper(this.getContext(), DBHelper.STR_EXP);
        loadDb();
        sumProduct = 0;
        sumZKH = 0;
        sumHealth = 0;
        sumClothes = 0;
        sumOther = 0;
        sumEntertainment = 0;
        for (Expense i : expensesData){
            switch (i.getType()){
                case PRODUCTS:
                    sumProduct += i.getSumma();
                    break;
                case ZKH:
                    sumZKH += i.getSumma();
                    break;
                case HEALTH:
                    sumHealth += i.getSumma();
                    break;
                case CLOTHES:
                    sumClothes += i.getSumma();
                    break;
                case ENTERTAINMENT:
                    sumEntertainment += i.getSumma();
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

        View view = inflater.inflate(R.layout.fragment_expenses_diagramm, container, false);

        chart = view.findViewById(R.id.chart_exp);

        ArrayList<PieEntry> yVals = new ArrayList<>();
        if (sumProduct != 0) {
            yVals.add(new PieEntry(sumProduct, "Продукты"));
        }
        if (sumZKH != 0) {
            yVals.add(new PieEntry(sumZKH, "ЖКХ"));
        }
        if (sumHealth != 0) {
            yVals.add(new PieEntry(sumHealth, "Здоровье"));
        }
        if (sumClothes != 0) {
            yVals.add(new PieEntry(sumClothes, "Одежда"));
        }
        if (sumEntertainment != 0) {
            yVals.add(new PieEntry(sumEntertainment, "Развлечения"));
        }
        if (sumOther != 0) {
            yVals.add(new PieEntry(sumOther, "Другое"));
        }         // Здесь происходит инициализация данных в диаграмме

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4b0082"));
        colors.add(Color.parseColor("#05fa3a"));
        colors.add(Color.parseColor("#f6fa05"));
        colors.add(Color.parseColor("#fa05f2"));
        colors.add(Color.parseColor("#f70505"));
        colors.add(Color.parseColor("#ee6e55"));   // Цвета диаграммы

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);

        chart.setData(pieData);


        String descriptionStr = "Доли расходов";
        Description description = new Description();     // Подписи к диаграмме
        description.setText(descriptionStr);
        chart.setDescription(description);

        return view;
    }

    @SuppressLint("Recycle")
    private void loadDb(){
        SQLiteDatabase sqlLoad = dbHelperEXP.getWritableDatabase();
        Cursor cursor = sqlLoad.query(DBHelper.STR_EXP, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int summa = cursor.getColumnIndex(DBHelper.KEY_SUMMA);
            int type = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            do {
                expensesData.add(new Expense(cursor.getString(date), cursor.getInt(summa), cursor.getString(type)));
//                    Log.d("DBLOG", "DATE = " + cursor.getString(date) + " SUMMA = " +
//                            cursor.getInt(summa) + " TYPE = " + cursor.getString(type));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBLOG", "NODATA");
        }
    }
}