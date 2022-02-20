package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExpencesExcelFragment extends Fragment {

    private final static String FILE_NAME = "expenses.csv";
    private DBHelper dbHelperEXP;
    private ArrayList<Expense> expensesData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expensesData = new ArrayList<>();
        dbHelperEXP = new DBHelper(this.getContext(), DBHelper.STR_EXP);
        loadDb();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_expenses_excel, container, false);
    }

//    public void exportExpCsv(View view){
//
//        FileOutputStream fos = null;
//        try {
//            //EditText textBox = findViewById(R.id.editor);
//            //String text = textBox.getText().toString();
//
//
//
//            fos = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            fos.write(text.getBytes());
//            Toast.makeText(this.getContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();
//        }
//        catch(IOException ex) {
//
//            Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        finally{
//            try{
//                if(fos!=null)
//                    fos.close();
//            }
//            catch(IOException ex){
//
//                Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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