package com.ieti.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    private final ExecutorService executorService = Executors.newFixedThreadPool(1);


    public void onLogin(View view) {
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        if (email.getText().toString().isEmpty()) {
            email.setError("Email cannot be empty");
        }
        if (email.getText().toString().isEmpty()) {
            password.setError("Password cannot be empty");
        }
    }
}
