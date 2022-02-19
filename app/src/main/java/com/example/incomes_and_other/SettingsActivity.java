package com.example.incomes_and_other;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    Button btn_logout, btn_toFB, btn_fromFB;
    private FirebaseAuth fAuth;
    protected FirebaseUser fUser;
    private DatabaseReference fbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
    private void initElements(){
        btn_logout = findViewById(R.id.btn_logout);
        btn_toFB = findViewById(R.id.btn_putInFirebase);
        btn_fromFB = findViewById(R.id.btn_getFromFirebase);
    }

    private void getFromFB(){

    }

    private void putIntoDB(){

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