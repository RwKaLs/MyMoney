package com.example.incomes_and_other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class IncomesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes);
    }

    public void onBackClc(View view){
        Intent intent = new Intent(IncomesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onListClc(View view) {
        IncomesListFragment incomesListFragment = new IncomesListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesListFragment);
        ft.commit();
    }

    public void onImportClc(View view) {
        IncomesExcelFragment incomesExcelFragment = new IncomesExcelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesExcelFragment);
        ft.commit();
    }

    public void onDiagClc(View view) {
        IncomesDiagrammFragment incomesDiagrammFragment = new IncomesDiagrammFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, incomesDiagrammFragment);
        ft.commit();
    }
}