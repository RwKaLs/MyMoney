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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.CellView;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class IncomesActivity extends AppCompatActivity {

    private final static String FILE_NAME_CSV = "incomes.csv";
    private final static String FILE_NAME_XLSX = "incomes.xlsx";
    DBHelper dbHelperINC;
    private ArrayList<Income> incomesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes);
    }

    public void onListClc(View view) {
        IncomesListFragment incomesListFragment = new IncomesListFragment(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesListFragment);
        ft.commit();
    }

    public void onExportClc(View view) {
        IncomesExcelFragment incomesExcelFragment = new IncomesExcelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesExcelFragment);
        ft.commit();
    }

    public void onDiagClc(View view) {
        IncomesDiagrammFragment incomesDiagrammFragment = new IncomesDiagrammFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesDiagrammFragment);
        ft.commit();
    }

    public void onSevenDaysClick(View view){
        IncomesListFragment incomesListFragment = new IncomesListFragment(1);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesListFragment);
        ft.commit();
    }
    public void onOneMonthClick(View view){
        IncomesListFragment incomesListFragment = new IncomesListFragment(2);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesListFragment);
        ft.commit();
    }
    public void onThreeMonthsClick(View view){
        IncomesListFragment incomesListFragment = new IncomesListFragment(3);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesListFragment);
        ft.commit();
    }

    // replace with a button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncomesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onIncCsv(View view) {
        exportIncCsv();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onIncXlsx(View view) {
        exportIncXlsx();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Успешный экспорт", Toast.LENGTH_SHORT);
        toast.show();
    }



    void exportIncCsv() {
        incomesData = new ArrayList<>();
        dbHelperINC = new DBHelper(this, DBHelper.STR_INC);
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
        new CsvRoutines(settings).writeAll(incomesData, Income.class, file, "type", "data", "summa");
    }

    private void exportIncXlsx() {
        incomesData = new ArrayList<>();
        dbHelperINC = new DBHelper(this, DBHelper.STR_INC);
        loadDb();

        String csvFile = "incomes.xlsx";
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


                for (int i = 0; i < incomesData.size(); i++) {
                    sheet.addCell(new Label(0, i + 1, incomesData.get(i).getData()));
                    sheet.addCell(new Label(1, i + 1, incomesData.get(i).getType()));
                    sheet.addCell(new Label(2, i + 1, String.valueOf(incomesData.get(i).getSumma())));

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
        SQLiteDatabase sqlLoad = dbHelperINC.getWritableDatabase();
        Cursor cursor = sqlLoad.query(DBHelper.STR_INC, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int summa = cursor.getColumnIndex(DBHelper.KEY_SUMMA);
            int type = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            do {
                incomesData.add(new Income(cursor.getString(date), cursor.getInt(summa), cursor.getString(type)));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBLOG", "NODATA");
        }
    }

}
