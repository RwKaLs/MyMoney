package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button toInc, toExp, sOut, saveInc, saveExp;
    EditText edSumInc, edSumExp, edDateInc, edDateExp;
    ArrayList<Income> incomesData; // get it from DB
    ArrayList<Expense> expensesData; // get it from DB
    Spinner spinnerInc, spinnerExp;
    SharedPreferences isAccount; // for saving userId
    DBHelper dbHelperINC, dbHelperEXP;
    ExpensesProvider expProv;
    IncomesProvider incProv;
    TextView testDb;
    int isIn; // is User in account
    String uId;
    private final String ISIN = "ISUSER";
    private final String SPUID = "UID";
    String userID = "LALALA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        if (getIntent().getStringExtra("USER") != null){
            userID = getIntent().getStringExtra("USER");
            //Toast.makeText(this, String.valueOf(userID), Toast.LENGTH_LONG).show();
            SharedPreferences.Editor saveAcc = isAccount.edit();
            saveAcc.putInt(ISIN, 1);
            saveAcc.putString(SPUID, userID);
            saveAcc.apply();
        } else if (isIn == 0){
            if (hasConnection(this)) {
                Intent iLog = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(iLog);
            }
        }
        setContentView(R.layout.activity_main);
        initElements();

        @SuppressLint("NonConstantResourceId") View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.btn_toInc:
                    Intent intent = new Intent(MainActivity.this, Incomes.class);
                    startActivity(intent);
                    break;
                case R.id.btn_toExp:
                    Intent intent1 = new Intent(MainActivity.this, Expenses.class);
                    startActivity(intent1);
                    break;
                case R.id.signout:
                    SharedPreferences.Editor saveAcc = isAccount.edit();
                    saveAcc.putInt(ISIN, 0);
                    saveAcc.putString(SPUID, "");
                    saveAcc.apply();
                    Intent ireturn = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(ireturn);
                    break;
                case R.id.save_Inc:
                    saveInDb(0);
                    break;
                case R.id.save_Exp:
                    saveInDb(1);
                    break;
            }
        };
        toInc.setOnClickListener(onClickListener);
        toExp.setOnClickListener(onClickListener);
        sOut.setOnClickListener(onClickListener);
        saveInc.setOnClickListener(onClickListener);
        saveExp.setOnClickListener(onClickListener);
    }

    private void initElements(){
        toInc = findViewById(R.id.btn_toInc);
        toExp = findViewById(R.id.btn_toExp);
        sOut = findViewById(R.id.signout);
        edSumInc = findViewById(R.id.ed_sumInc);
        edSumExp = findViewById(R.id.ed_sumExp);
        edDateInc = findViewById(R.id.ed_dateInc);
        edDateExp = findViewById(R.id.ed_dateExp);
        saveInc = findViewById(R.id.save_Inc);
        saveExp = findViewById(R.id.save_Exp);
        spinnerInc = findViewById(R.id.spinner_Inc);
        spinnerExp = findViewById(R.id.spinner_Exp);
        incomesData = new ArrayList<>();
        expensesData = new ArrayList<>();
        dbHelperINC = new DBHelper(this, DBHelper.STR_INC);
        dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
        testDb = findViewById(R.id.testDb);
        loadDb();

    }
    private void loadData(){
        isAccount = getPreferences(MODE_PRIVATE);
        isIn = isAccount.getInt(ISIN, 0);
        uId = isAccount.getString(SPUID, "");
    }

    private void saveInDb(int typeOp){
        if (typeOp == 0){
            if (!String.valueOf(edSumInc.getText()).equals("") && !String.valueOf(edDateInc.getText()).equals("")){
                String date = String.valueOf(edDateInc.getText());
                int summa = Integer.parseInt(String.valueOf(edSumInc.getText()));
                String type = String.valueOf(spinnerInc.getSelectedItem());
                SQLiteDatabase databaseInc = dbHelperINC.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_DATE, date);
                contentValues.put(DBHelper.KEY_SUMMA, summa);
                contentValues.put(DBHelper.KEY_TYPE, type);
                databaseInc.insert(DBHelper.STR_INC, null, contentValues);
                loadDb();
            } else {
                Toast.makeText(this, "Неверный ввод!", Toast.LENGTH_LONG).show();
            }
        } else if (typeOp == 1){
            if (!String.valueOf(edSumExp.getText()).equals("") && !String.valueOf(edDateExp.getText()).equals("")){
                String date = String.valueOf(edDateExp.getText());
                int summa = Integer.parseInt(String.valueOf(edSumExp.getText()));
                String type = String.valueOf(spinnerExp.getSelectedItem());
                SQLiteDatabase databaseExp = dbHelperEXP.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_DATE, date);
                contentValues.put(DBHelper.KEY_SUMMA, summa);
                contentValues.put(DBHelper.KEY_TYPE, type);
                databaseExp.insert(DBHelper.STR_EXP, null, contentValues);
                loadDb();
            } else {
                Toast.makeText(this, "Неверный ввод!", Toast.LENGTH_LONG).show();
            }
        }
    }
    // now just for incomes
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
        setProviders();
    }
    public void setProviders(){
        incProv = () -> incomesData;
        expProv = () -> expensesData;
        testDb.setText(incomesData.toString() + "\n" + expensesData.toString());
    }
    public static boolean hasConnection(final Context context)
    {
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

