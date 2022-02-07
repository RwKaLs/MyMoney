package com.example.incomes_and_other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Incomes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes);
    }

    public void onBackClc(View view){
        Intent intent = new Intent(Incomes.this, MainActivity.class);
        startActivity(intent);
    }
}
// lazylist
