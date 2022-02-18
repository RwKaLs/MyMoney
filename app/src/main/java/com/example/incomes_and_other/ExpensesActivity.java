package com.example.incomes_and_other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ExpensesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
    }

    public void onDiagClc(View view) {
        ExpensesDiagrammFragment expensesDiagrammFragment = new ExpensesDiagrammFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expensesDiagrammFragment);
        ft.commit();
    }

    public void onExportClc(View view) {
        ExpencesExcelFragment expencesExcelFragment = new ExpencesExcelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, expencesExcelFragment);
        ft.commit();
    }

    public void onListClc(View view) {
        IncomesListFragment incomesListFragment = new IncomesListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Exp, incomesListFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
        startActivity(intent);
    }
}