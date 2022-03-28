package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    Button btn_logout, btn_toFB, btn_fromFB;
    private DatabaseReference fbReference;
    private DBHelper dbHelperINC, dbHelperEXP;
    private String userId;
    private ArrayList<Income> incomesData;
    private ArrayList<Expense> expensesData;
    int balanceret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        balanceret = 0;
        if (getIntent().getStringExtra("UID") != null){
            userId = getIntent().getStringExtra("UID");
        }
        initElements();
        @SuppressLint("NonConstantResourceId") View.OnClickListener onclck = view -> {
            switch (view.getId()){
                case R.id.btn_logout:
                    Intent iLogout = new Intent(SettingsActivity.this, MainActivity.class);
                    iLogout.putExtra("LETSOUT", 1);
                    startActivity(iLogout);
                    break;
                case R.id.btn_getFromFirebase:
                    if (hasConnection(this)){
                        getFromFB();
                    } else {
                        Toast.makeText(this, "Нет подключения к Интернету!", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.btn_putInFirebase:
                    if (hasConnection(this)){
                        putIntoDB();
                    } else {
                        Toast.makeText(this, "Нет подключения к Интернету!", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        };
        btn_logout.setOnClickListener(onclck);
        btn_toFB.setOnClickListener(onclck);
        btn_fromFB.setOnClickListener(onclck);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent iToMain = new Intent(SettingsActivity.this, MainActivity.class);
        if (balanceret != 0){
            iToMain.putExtra("NEWBALANCE", balanceret);
        }
        startActivity(iToMain);
    }

    private void initElements(){
        btn_logout = findViewById(R.id.btn_logout);
        btn_toFB = findViewById(R.id.btn_putInFirebase);
        btn_fromFB = findViewById(R.id.btn_getFromFirebase);
        incomesData = new ArrayList<>();
        expensesData = new ArrayList<>();
        fbReference = FirebaseDatabase.getInstance("https://exxx-cacff-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        dbHelperINC = new DBHelper(this, DBHelper.STR_INC);
        dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
    }

    private void getFromFB(){
        SQLiteDatabase sqlINC = dbHelperINC.getWritableDatabase();
        SQLiteDatabase sqlEXP = dbHelperEXP.getWritableDatabase();
        sqlINC.delete(DBHelper.STR_INC, null, null);
        sqlEXP.delete(DBHelper.STR_EXP, null, null);
        ValueEventListener vEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.child(userId).child("Incomes").getChildren()){
                    Income income = ds.getValue(Income.class);
                    assert income != null;
                    balanceret += income.getSumma();
                    putINCIntoSQLite(income);
                }
                for (DataSnapshot ds: snapshot.child(userId).child("Expenses").getChildren()){
                    Expense expense = ds.getValue(Expense.class);
                    assert expense != null;
                    balanceret -= expense.getSumma();
                    putEXPIntoSQLite(expense);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        fbReference.addValueEventListener(vEL);
    }

    private void putIntoDB(){
        getFromSQLite();
        fbReference.child(userId).removeValue();
        fbReference.child(userId).removeValue();
        int j = 0;
        for (Income i: incomesData){
            Date now = new Date();
            fbReference.child(userId).child("Incomes").child(now + String.valueOf(j)).setValue(i);
            j++;
        }
        for (Expense i: expensesData){
            Date now = new Date();
            fbReference.child(userId).child("Expenses").child(now + String.valueOf(j)).setValue(i);
            j++;
        }
    }

    private void putINCIntoSQLite(Income newIncome){
        SQLiteDatabase databaseInc = dbHelperINC.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DATE, newIncome.getData());
        contentValues.put(DBHelper.KEY_SUMMA, newIncome.getSumma());
        contentValues.put(DBHelper.KEY_TYPE, newIncome.getType());
        databaseInc.insert(DBHelper.STR_INC, null, contentValues);
        contentValues.clear();
    }

    private void putEXPIntoSQLite(Expense newExpense){
        SQLiteDatabase databaseExp = dbHelperEXP.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DATE, newExpense.getData());
        contentValues.put(DBHelper.KEY_SUMMA, newExpense.getSumma());
        contentValues.put(DBHelper.KEY_TYPE, newExpense.getType());
        databaseExp.insert(DBHelper.STR_EXP, null, contentValues);
        contentValues.clear();
    }

    @SuppressLint("Recycle")
    private void getFromSQLite(){
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
        sqlLoad = dbHelperEXP.getWritableDatabase();
        cursor = sqlLoad.query(DBHelper.STR_EXP, null, null, null, null, null, null);
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

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }
}