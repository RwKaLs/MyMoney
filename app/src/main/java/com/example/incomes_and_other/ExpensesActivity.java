package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ExpensesActivity extends AppCompatActivity {

    private final static String FILE_NAME_CSV = "expenses.csv";
    private final static String FILE_NAME_XLSX = "expenses.xlsx";
    ArrayList<Expense> expensesData;
    private DBHelper dbHelperEXP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
    }

    public void onDiagClc(View view) {
        ExpensesDiagrammFragment expensesDiagrammFragment = new ExpensesDiagrammFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesDiagrammFragment);
        ft.commit();
    }

    public void onExportClc(View view) {
        ExpencesExcelFragment expencesExcelFragment = new ExpencesExcelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expencesExcelFragment);
        ft.commit();
    }

    public void onListClc(View view) {
        ExpensesListFragment expensesListFragment = new ExpensesListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesListFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void OnExpCsv(View view){
        exportExpCsv();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный (почти) экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void OnExpXlsx(View view){
        exportExpXlsx();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный (почти) экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }

    void exportExpCsv() {
        //expensesData = new ArrayList<>();
        //dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
        //loadDb();
        try {
            File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/media/" + getPackageName());
            if (!externalAppDir.exists()) {
                externalAppDir.mkdir();
            }

            File file = new File(externalAppDir , FILE_NAME_CSV);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String text = "WOOORKS!";
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void exportExpXlsx() {
        //expensesData = new ArrayList<>();
        //dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
        //loadDb();
        try {
            File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/media/" + getPackageName());
            if (!externalAppDir.exists()) {
                externalAppDir.mkdir();
            }

            File file = new File(externalAppDir , FILE_NAME_XLSX);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String text = "WOOORKS!";
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@SuppressLint("Recycle")
    //private void loadDb(){
    //    SQLiteDatabase sqlLoad = dbHelperEXP.getWritableDatabase();
    //    Cursor cursor = sqlLoad.query(DBHelper.STR_EXP, null, null, null, null, null, null);
    //    if (cursor.moveToFirst()){
    //        int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
    //        int summa = cursor.getColumnIndex(DBHelper.KEY_SUMMA);
    //        int type = cursor.getColumnIndex(DBHelper.KEY_TYPE);
    //        do {
    //            expensesData.add(new Expense(cursor.getString(date), cursor.getInt(summa), cursor.getString(type)));
    //                Log.d("DBLOG", "DATE = " + cursor.getString(date) + " SUMMA = " +
    //                        cursor.getInt(summa) + " TYPE = " + cursor.getString(type));
    //        } while (cursor.moveToNext());
    //    } else {
    //        Log.d("DBLOG", "NODATA");
    //    }
    //}
}