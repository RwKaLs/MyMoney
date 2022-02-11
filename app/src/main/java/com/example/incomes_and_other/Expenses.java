package com.example.incomes_and_other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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

    public void onDiagClc(View view) {
        DiagrammFragment diagrammFragment = new DiagrammFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, diagrammFragment);
        ft.commit();
    }

    public void onImprtClc(View view) {
        ImportFragment importFragment = new ImportFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, importFragment);
        ft.commit();
    }

    public void onListClc(View view) {
        SpisokFragment spisokFragment = new SpisokFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, spisokFragment);
        ft.commit();
    }
}