package com.example.incomes_and_other.loginlogic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.incomes_and_other.MainActivity;
import com.example.incomes_and_other.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText edMail;
    EditText edPassword;
    Button btnIn;
    Button btnReg;
    TextView tv_wrong;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initElements();
        if (getIntent().getStringExtra("LOGIN") != null){
            if (getIntent().getStringExtra("PASSWORD") != null){
                edMail.setText(String.valueOf(getIntent().getStringExtra("LOGIN")));
                edPassword.setText(String.valueOf(getIntent().getStringExtra("PASSWORD")));
            }
        }
        setFirebase();
        if (currentUser != null){
            Intent iToMain = new Intent(LoginActivity.this, MainActivity.class);
            iToMain.putExtra("USER", currentUser.getUid());
            startActivity(iToMain);
        }
        @SuppressLint("NonConstantResourceId") View.OnClickListener onclck = view -> {
            switch (view.getId()){
                case R.id.sign_in:
                    if (!String.valueOf(edMail.getText()).equals("") && !String.valueOf(edPassword.getText()).equals("")) {
                        signIn();
                    } else {
                        tv_wrong.setText("Поля пустые!");
                    }
                    break;
                case R.id.sign_up:
                    Intent iToReg = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(iToReg);
                    break;
            }
        };
        btnIn.setOnClickListener(onclck);
        btnReg.setOnClickListener(onclck);
    }
    private void signIn(){
        String login = String.valueOf(edMail.getText());
        String password = String.valueOf(edPassword.getText());
        tv_wrong.setText("Вход...");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(task -> {
            currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent iToMain = new Intent(LoginActivity.this, MainActivity.class);
                iToMain.putExtra("USER", currentUser.getUid());
                startActivity(iToMain);
            }
        });
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent iToMain = new Intent(LoginActivity.this, MainActivity.class);
            iToMain.putExtra("USER", currentUser.getUid());
            startActivity(iToMain);
        } else {
            tv_wrong.setText("Неверный логин или пароль!");
        }
    }
    private void setFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        currentUser = mAuth.getCurrentUser();
    }
    private void initElements(){
        edMail = findViewById(R.id.edMail);
        edPassword = findViewById(R.id.edPass);
        btnIn = findViewById(R.id.sign_in);
        btnReg = findViewById(R.id.sign_up);
        tv_wrong = findViewById(R.id.tv_wrong_pass);
    }
}
