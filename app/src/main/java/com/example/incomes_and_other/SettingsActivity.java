package com.example.incomes_and_other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    Button btn_logout, btn_toFB, btn_fromFB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initElements();
        View.OnClickListener onclck = view -> {
            switch (view.getId()){
                case R.id.btn_logout:
                    Intent iLogout = new Intent(SettingsActivity.this, MainActivity.class);
                    iLogout.putExtra("LETSOUT", 1);
                    startActivity(iLogout);
                    break;
                case R.id.btn_getFromFirebase:
                    break;
                case R.id.btn_putInFirebase:
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
}