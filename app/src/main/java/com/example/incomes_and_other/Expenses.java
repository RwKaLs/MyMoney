package com.example.incomes_and_other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Expenses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
    }

    public void onBackClc2(View view){
        Intent intent = new Intent(Expenses.this, MainActivity.class);
        startActivity(intent);
    }
}