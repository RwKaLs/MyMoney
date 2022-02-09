package com.example.incomes_and_other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    EditText edMail, edPassword;
    Button btnReg;
    TextView tvInfo;
    DatabaseReference dbReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initElements();
        if (hasConnection(this)) {
            setFirebase();
            View.OnClickListener onclck = view -> {
                if (view.getId() == R.id.btn_reg) {
                    if (!String.valueOf(edMail.getText()).equals("") && !String.valueOf(edPassword.getText()).equals("")) {
                        String email = String.valueOf(edMail.getText());
                        String password = String.valueOf(edPassword.getText());
                        tvInfo.setText("Регистрация...");
                        Toast.makeText(this, email, Toast.LENGTH_LONG).show();
                        if (password.length() < 6) {
                            tvInfo.setText("Пароль не должен содержать меньше 6 знаков!");
                        } else if (!isValidEmail(email)) {
                            tvInfo.setText("Неверный формат почты!");
                        } else {
                            mAuth.createUserWithEmailAndPassword(email, password);
                            mAuth.signOut();
                            mAuth.signInWithEmailAndPassword(email, password);
                            currentUser = mAuth.getCurrentUser();
                            while (currentUser == null){
                                mAuth.signOut();
                                mAuth.signInWithEmailAndPassword(email, password);
                                currentUser = mAuth.getCurrentUser();
                            }
                            dbReference.push().setValue(currentUser.getUid());
                            Intent iToLogin = new Intent(RegistrationActivity.this, LoginActivity.class);
                            iToLogin.putExtra("LOGIN", email);
                            iToLogin.putExtra("PASSWORD", password);
                            startActivity(iToLogin);
                        }
                    } else {
                        tvInfo.setText("Поля пустые!");
                    }
                }
            };
            btnReg.setOnClickListener(onclck);
        } else {
            tvInfo.setText("Нет Интернет-соединения!");
        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void initElements(){
        edMail = findViewById(R.id.edMail);
        edPassword = findViewById(R.id.edPass);
        btnReg = findViewById(R.id.btn_reg);
        tvInfo = findViewById(R.id.tv_wrong_in);
    }
    private void setFirebase(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();
    }
    public static boolean hasConnection(final Context context)
    {
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