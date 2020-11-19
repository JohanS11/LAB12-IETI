package com.ieti.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ieti.myapplication.R;
import com.ieti.myapplication.TaskNewActivity;
import com.ieti.myapplication.model.LoginWrapper;
import com.ieti.myapplication.model.Token;
import com.ieti.myapplication.services.AuthService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view) {
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        if (email.getText().toString().isEmpty()) {
            email.setError("Email cannot be empty");
        }
        if (email.getText().toString().isEmpty()) {
            password.setError("Password cannot be empty");
        }else{

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/10.0.2.2:8080") //localhost for emulator
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthService authService = retrofit.create(AuthService.class);
            executorService.execute(authenticate(authService,email,password));
        }
    }

    private void saveToken(Token token) {
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TOKEN_KEY",token.getAccessToken());
        editor.commit();
    }



    private Runnable authenticate(AuthService authService, EditText email, EditText password) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Token> response =authService.login(new LoginWrapper(email.getText().toString(), password.getText().toString())).execute();
                    Token token = response.body();
                    saveToken(token);
                    if (token!=null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), TaskNewActivity.class);
                                startActivity(intent);
                            }
                        });
                        finish();
                    }
                    else{
                        email.setError("Verify Your email!");
                        password.setError("verify Your Password!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

    }



}
