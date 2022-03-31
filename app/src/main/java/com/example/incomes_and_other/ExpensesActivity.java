package com.example.incomes_and_other;

import android.annotation.SuppressLint;
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

import com.univocity.parsers.csv.CsvRoutines;
import com.univocity.parsers.csv.CsvWriterSettings;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
        ExpensesListFragment expensesListFragment = new ExpensesListFragment(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesListFragment);
        ft.commit();
    }

    public void onSevenDaysClick(View view){
        ExpensesListFragment expensesListFragment = new ExpensesListFragment(1);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesListFragment);
        ft.commit();
    }
    public void onOneMonthClick(View view){
        ExpensesListFragment expensesListFragment = new ExpensesListFragment(2);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesListFragment);
        ft.commit();
    }
    public void onThreeMonthsClick(View view){
        ExpensesListFragment expensesListFragment = new ExpensesListFragment(3);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesListFragment);
        ft.commit();
    }

    // replace with a button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void OnExpCsv(View view){
        exportExpCsv();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void OnExpXlsx(View view) {
        exportExpXlsx();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void exportExpCsv() {
        expensesData = new ArrayList<>();
        dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
        loadDb();
        CsvWriterSettings settings = new CsvWriterSettings();

        settings.setHeaders("type", "data", "summa");

        File fildir = new File(Environment.getExternalStorageDirectory() + "/Android/media/" + getPackageName());
        if (!fildir.exists()){
            fildir.mkdir();
        }
        File file = new File(fildir, FILE_NAME_CSV);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new CsvRoutines(settings).writeAll(expensesData, Expense.class, file, "type", "data", "summa");
    }

    private void exportExpXlsx() {
        expensesData = new ArrayList<>();
        dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
        loadDb();

        String csvFile = "expenses.xlsx";
        File fildir = new File(Environment.getExternalStorageDirectory() + "/Android/media/" + getPackageName());
        File file = new File(fildir, csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);


            try {

                //Excel sheet name. 0 (number)represents first sheet
                WritableSheet sheet = workbook.createSheet("sheet1", 0);
                // column and row title
                sheet.addCell(new Label(0, 0, "Data"));
                sheet.addCell(new Label(1, 0, "Type"));
                sheet.addCell(new Label(2, 0, "Summa"));


                for (int i = 0; i < expensesData.size(); i++) {
                    sheet.addCell(new Label(0, i + 1, expensesData.get(i).getData()));
                    sheet.addCell(new Label(1, i + 1, expensesData.get(i).getType()));
                    sheet.addCell(new Label(2, i + 1, String.valueOf(expensesData.get(i).getSumma())));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //closing cursor
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Log.d("DBLOG", "DATE = " + cursor.getString(date) + " SUMMA = " +
                            cursor.getInt(summa) + " TYPE = " + cursor.getString(type));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBLOG", "NODATA");
        }
    }
}