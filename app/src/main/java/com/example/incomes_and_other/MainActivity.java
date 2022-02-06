package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button toInc;
    Button toExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toInc = (Button) findViewById(R.id.button4);
        toExp = (Button) findViewById(R.id.button5);

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
            }
        };

        toInc.setOnClickListener(onClickListener);
        toExp.setOnClickListener(onClickListener);
    }


}