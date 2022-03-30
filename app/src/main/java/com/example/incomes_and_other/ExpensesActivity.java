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
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    // replace with a button
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

    public void OnExpXlsx(View view) {
        exportExpXlsx(3, 5);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный (почти) экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }

    void exportExpCsv() {
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

    public static void exportExpXlsx(int row, int col) {
        try {
            FileInputStream file = new FileInputStream(Environment.getExternalStorageDirectory().toString() +"Android/media/"+ "expenses.xlsx");

            Sheet sheet;

            Workbook workbook = new HSSFWorkbook(file);
            sheet = workbook.createSheet("Sheet 1");
            Cell cell;

            //Update the value of cell

            cell = sheet.getRow(row).getCell(col);
            cell.setCellValue("changed");

            file.close();

            FileOutputStream outFile =new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"Android/media/"+ "expenses.xlsx");
            workbook.write(outFile);
            outFile.close();

        } catch (IOException e) {
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