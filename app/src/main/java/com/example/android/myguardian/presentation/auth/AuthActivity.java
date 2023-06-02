package com.example.android.myguardian.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.myguardian.R;
import com.example.android.myguardian.presentation.news.NewsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private static final String LOG_TAG = AuthActivity.class.getName();

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);
        auth = FirebaseAuth.getInstance();

        Button authBtn = findViewById(R.id.auth_btn); //добавляем кнопку авторизации
        authBtn.setOnClickListener(v -> {
            signIn();
        });
        Button regBtn = findViewById(R.id.reg_btn); //добавляем кнопку регистрации
        regBtn.setOnClickListener(v -> {
            register();
        });

        FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                updateUI();
            }
        };
        auth.addAuthStateListener(authListener);
    }

    private void signIn() {
        EditText login = findViewById(R.id.editText_login); //поле ввода логина
        EditText password = findViewById(R.id.editText_password); // поле ввода пароля
        if(login.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Поля не должны быть пустыми!", Toast.LENGTH_SHORT).show();
            return;
        }
        String fieldLogin = login.getText().toString();
        String fieldPassword = password.getText().toString();

        auth.signInWithEmailAndPassword(fieldLogin, fieldPassword).addOnCompleteListener(task -> {
           if(!task.isSuccessful()) { //если логина и пароля нет в системе, тост с требованием регистрации
               Toast.makeText(getApplicationContext(), "Доступ запрещен!\nВыполните регистрацию...", Toast.LENGTH_SHORT).show();
               return;
           }
           Toast.makeText(getApplicationContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
           updateUI();

           // todo save session and restore session if enter in activity
        });
    }

    private void register() {
        EditText login = findViewById(R.id.editText_login);
        EditText password = findViewById(R.id.editText_password);
        if(login.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Поля не должны быть пустыми!", Toast.LENGTH_SHORT).show();
            return;
        }
        String fieldLogin = login.getText().toString();
        String fieldPassword = password.getText().toString();

        auth.createUserWithEmailAndPassword(fieldLogin, fieldPassword).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Что-то не так :(\nЛогин должен содержать в себе E-Mail!", Toast.LENGTH_SHORT).show();
                return;
            }
            login.getText().clear();
            password.getText().clear();
            Toast.makeText(getApplicationContext(), "Аккаунт создан!\nПопробуйте теперь авторизироваться!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI() {
        startActivity(new Intent(this, NewsActivity.class));
        finish();
    }

}
