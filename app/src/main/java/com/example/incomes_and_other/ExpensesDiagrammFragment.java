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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperEXP = new DBHelper(this.getContext(), DBHelper.STR_EXP);
        expensesData = new ArrayList<>();
        loadDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_expenses_diagramm, container, false);

        chart = view.findViewById(R.id.chart_exp);

        ArrayList<PieEntry> yVals = new ArrayList<>();
        yVals.add (new PieEntry (28.6f, "нарушение"));
        yVals.add (new PieEntry (71.3f, "Нет нарушений"));  // Здесь происходит инициализация данных в диаграмме

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4A92FC"));
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