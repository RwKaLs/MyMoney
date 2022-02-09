package com.example.incomes_and_other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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

    public void onListClc(View view) {
        SpisokFragment2 spisokFragment2 = new SpisokFragment2();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, spisokFragment2);
        ft.commit();
    }

    public void onImportClc(View view) {
        ImportFragment2 importFragment2 = new ImportFragment2();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, importFragment2);
        ft.commit();
    }

    public void onDiagClc(View view) {
        DiagrammFragment2 diagrammFragment2 = new DiagrammFragment2();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Cont_Inc, diagrammFragment2);
        ft.commit();
    }
}
