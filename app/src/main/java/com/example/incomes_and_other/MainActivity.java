package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button toInc, toExp, sOut;
    SharedPreferences isAccount; // for saving userId
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
            Toast.makeText(this, String.valueOf(userID), Toast.LENGTH_LONG).show();
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

        toInc = findViewById(R.id.button4);
        toExp = findViewById(R.id.button5);
        sOut = findViewById(R.id.signout);

        @SuppressLint("NonConstantResourceId") View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.button4:
                    Intent intent = new Intent(MainActivity.this, Incomes.class);
                    startActivity(intent);
                    break;
                case R.id.button5:
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
            }
        };
        toInc.setOnClickListener(onClickListener);
        toExp.setOnClickListener(onClickListener);
        sOut.setOnClickListener(onClickListener);
    }

    private void loadData(){
        isAccount = getPreferences(MODE_PRIVATE);
        isIn = isAccount.getInt(ISIN, 0);
        uId = isAccount.getString(SPUID, "");
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

