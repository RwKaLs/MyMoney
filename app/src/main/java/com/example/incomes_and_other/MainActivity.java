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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.incomes_and_other.loginlogic.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button toInc, toExp, saveInc, saveExp;
    ImageButton toSettings;
    EditText edSumInc, edSumExp, edDateInc, edDateExp;
    ArrayList<Income> incomesData; // get it from DB
    ArrayList<Expense> expensesData; // get it from DB
    Spinner spinnerInc, spinnerExp;
    TextView tv_Balance;
    int balance;
    SharedPreferences isAccount; // for saving userId and balance
    private DatabaseReference dbRef;
    int isIn; // is User in account
    String uId;
    private final String ISIN = "ISUSER";
    private final String SPUID = "UID";
    private final String BALANCE = "BALANCE";
    String userID = "LALALA";
    private static final String SALARY = "salary";
    private static final String PRESENT = "present";
    private static final String OTHER = "other";
    private static final String PRODUCTS = "products";
    private static final String ZKH = "ZKH";
    private static final String HEALTH = "health";
    private static final String CLOTHES = "clothes";
    private static final String ENTERTAINMENT = "entertainment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        if (getIntent().getIntExtra("LETSOUT", 0) == 1){
            DBHelper incOut = new DBHelper(this, DBHelper.STR_INC);
            DBHelper expOut = new DBHelper(this, DBHelper.STR_EXP);
            SharedPreferences.Editor saveAcc = isAccount.edit();
            saveAcc.clear();
            saveAcc.putInt(ISIN, 0);
            saveAcc.putInt(BALANCE, 0);
            saveAcc.putString(SPUID, "");
            saveAcc.apply();
            SQLiteDatabase sqlINC = incOut.getWritableDatabase();
            SQLiteDatabase sqlEXP = expOut.getWritableDatabase();
            sqlINC.delete(DBHelper.STR_INC, null, null);
            sqlEXP.delete(DBHelper.STR_EXP, null, null);
            Intent reloadSout = new Intent(MainActivity.this, MainActivity.class);
            startActivity(reloadSout);
        }
        if (getIntent().getIntExtra("NEWBALANCE", -1000000) != -1000000){
            balance = getIntent().getIntExtra("NEWBALANCE", -1000000);
            SharedPreferences.Editor saveAcc = isAccount.edit();
            saveAcc.putInt(BALANCE, balance);
            saveAcc.apply();
        }
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
            } else {
                Toast.makeText(this, "Нет подключения к Интернету", Toast.LENGTH_LONG).show();
            }
        }
        loadData();
        setContentView(R.layout.activity_main);
        initElements();

        @SuppressLint("NonConstantResourceId") View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.btn_toInc:
                    Intent intent = new Intent(MainActivity.this, IncomesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_toExp:
                    Intent intent1 = new Intent(MainActivity.this, ExpensesActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btn_settings:
//                    SharedPreferences.Editor saveAcc = isAccount.edit();
//                    saveAcc.putInt(ISIN, 0);
//                    saveAcc.putString(SPUID, "");
//                    saveAcc.apply();
//                    Intent ireturn = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(ireturn);
                    Intent iSettings = new Intent(MainActivity.this, SettingsActivity.class);
                    iSettings.putExtra("UID", uId);
                    startActivity(iSettings);
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
        toSettings.setOnClickListener(onClickListener);
        saveInc.setOnClickListener(onClickListener);
        saveExp.setOnClickListener(onClickListener);

    }
//

    @SuppressLint("SetTextI18n")
    private void initElements(){
        toInc = findViewById(R.id.btn_toInc);
        toExp = findViewById(R.id.btn_toExp);
        toSettings = findViewById(R.id.btn_settings);
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
        tv_Balance = findViewById(R.id.tv_Balance);
        double infl = 9.5; // will get from FireBase
        @SuppressLint("DefaultLocale") String show1 = String.format("%.2f", balance * (100 - (Math.pow(infl, (1.0/12.0)))) / 100);
        @SuppressLint("DefaultLocale") String show2 = String.format("%.2f", balance * (100 - (Math.pow(infl, (0.25)))) / 100);
        @SuppressLint("DefaultLocale") String show3 = String.format("%.2f", balance * (100 - (Math.pow(infl, (0.5)))) / 100);
        @SuppressLint("DefaultLocale") String show4 = String.format("%.2f", balance * (100 - infl) / 100);
        tv_Balance.setText("Баланс : " + balance + "\nЧерез 1 мес: " + show1 + "\nЧерез 3 мес: " + show2 + "\nЧерез 6 мес: " + show3 + "\nЧерез год: " + show4);
    }

    private void loadData(){
        isAccount = getPreferences(MODE_PRIVATE);
        isIn = isAccount.getInt(ISIN, 0);
        uId = isAccount.getString(SPUID, "");
        balance = isAccount.getInt(BALANCE, 0);
    }

    @SuppressLint("SetTextI18n")
    private void saveInDb(int typeOp){
        if (typeOp == 0){
            if (!String.valueOf(edSumInc.getText()).equals("") && !String.valueOf(edDateInc.getText()).equals("")){
                if (String.valueOf(edDateInc.getText()).matches("\\d{2}-\\d{2}-\\d{4}")){
                    if (Double.parseDouble(String.valueOf(edSumInc.getText())) % 1 == 0){
                        String date = String.valueOf(edDateInc.getText());
                        int summa = Integer.parseInt(String.valueOf(edSumInc.getText()));
                        String type = String.valueOf(spinnerInc.getSelectedItem());
                        switch (type){
                            case ("Зарплата"):
                                type = SALARY;
                                break;
                            case ("Подарок"):
                                type = PRESENT;
                                break;
                            case ("Другое"):
                                type = OTHER;
                                break;
                        }
                        edDateInc.getText().clear();
                        edSumInc.getText().clear();
                        balance += summa;
                        SharedPreferences.Editor saveAcc = isAccount.edit();
                        saveAcc.putInt(BALANCE, balance);
                        saveAcc.apply();
                        tv_Balance.setText("Баланс : " + balance);
                        DBHelper dbHelperINC = new DBHelper(this, DBHelper.STR_INC);
                        SQLiteDatabase databaseInc = dbHelperINC.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.KEY_DATE, date);
                        contentValues.put(DBHelper.KEY_SUMMA, summa);
                        contentValues.put(DBHelper.KEY_TYPE, type);
                        databaseInc.insert(DBHelper.STR_INC, null, contentValues);
                        //databaseInc.execSQL("INSERT INTO " + DBHelper.STR_INC + " (" + DBHelper.KEY_DATE + ", " + DBHelper.KEY_SUMMA + ", " +
                                //DBHelper.KEY_TYPE + ") VALUES(" + date + ", " + summa + ", " + type + ");");
                        contentValues.clear();
                        dbHelperINC.close();
                        databaseInc.close();
                        if (hasConnection(this)) {
                            saveFBINC(date, summa, type);
                        }
                    } else {
                        Toast.makeText(this, "Некорректная сумма!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Некорректная дата!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Неверный ввод!", Toast.LENGTH_LONG).show();
            }
        } else if (typeOp == 1){
            if (!String.valueOf(edSumExp.getText()).equals("") && !String.valueOf(edDateExp.getText()).equals("")){
                if (String.valueOf(edDateExp.getText()).matches("\\d{2}-\\d{2}-\\d{4}")) {
                    if (Double.parseDouble(String.valueOf(edSumExp.getText())) % 1 == 0) {
                        if (!String.valueOf(edSumExp.getText()).equals("") && !String.valueOf(edDateExp.getText()).equals("")) {
                            String date = String.valueOf(edDateExp.getText());
                            int summa = Integer.parseInt(String.valueOf(edSumExp.getText()));
                            String type = String.valueOf(spinnerExp.getSelectedItem());
                            switch (type){
                                case ("Продукты"):
                                    type = PRODUCTS;
                                    break;
                                case ("ЖКХ"):
                                    type = ZKH;
                                    break;
                                case ("Здоровье"):
                                    type = HEALTH;
                                    break;
                                case ("Одежда"):
                                    type = CLOTHES;
                                    break;
                                case ("Развлечения"):
                                    type = ENTERTAINMENT;
                                    break;
                                case ("Другое"):
                                    type = OTHER;
                                    break;
                            }
                            edDateExp.getText().clear();
                            edSumExp.getText().clear();
                            balance -= summa;
                            SharedPreferences.Editor saveAcc = isAccount.edit();
                            saveAcc.putInt(BALANCE, balance);
                            saveAcc.apply();
                            tv_Balance.setText("Баланс : " + balance);
                            DBHelper dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
                            SQLiteDatabase databaseExp = dbHelperEXP.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBHelper.KEY_DATE, date);
                            contentValues.put(DBHelper.KEY_SUMMA, summa);
                            contentValues.put(DBHelper.KEY_TYPE, type);
                            databaseExp.insert(DBHelper.STR_EXP, null, contentValues);
                            //databaseExp.execSQL("INSERT INTO " + DBHelper.STR_EXP + " (" + DBHelper.KEY_DATE + ", " + DBHelper.KEY_SUMMA + ", " +
                                    //DBHelper.KEY_TYPE + ") VALUES(" + date + ", " + summa + ", " + type + ");");
                            contentValues.clear();
                            dbHelperEXP.close();
                            databaseExp.close();
                            if (hasConnection(this)) {
                                saveFBEXP(date, summa, type);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Некорректная сумма!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Некорректная дата!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Неверный ввод!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void saveFBINC(String date, int summa, String type){
        dbRef = FirebaseDatabase.getInstance("https://exxx-cacff-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        Date now = new Date();
        dbRef.child(uId).child("Incomes").child(now.toString()).setValue(new Income(date, summa, type));
    }
    private void saveFBEXP(String date, int summa, String type){
        dbRef = FirebaseDatabase.getInstance("https://exxx-cacff-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User");
        Date now = new Date();
        dbRef.child(uId).child("Expenses").child(now.toString()).setValue(new Expense(date, summa, type));
    }
    // future
    @SuppressLint("Recycle")
    private void loadDb(){
        DBHelper dbHelperINC = new DBHelper(this, DBHelper.STR_INC);
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
        DBHelper dbHelperEXP = new DBHelper(this, DBHelper.STR_EXP);
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
