package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputEmail, inputPassword, inputPassword2;
    private Button buttonRegister, backBtn;
    private String username, email, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerBackMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(registerBackMainIntent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = inputName.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString();
                password2 = inputPassword2.getText().toString();
                if(username.isEmpty() || username.length() < 5 || username.length() > 20) {
                    Toast.makeText(RegisterActivity.this, "The username can be a minimum of 5 and a maximum of 20 characters", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty() || email.length()  > 255) {
                    Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty() || password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "The password can be at least 8 characters long", Toast.LENGTH_SHORT).show();
                }
                else if(!password2.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "The password must match", Toast.LENGTH_SHORT).show();
                }
                else {
                    new UserTask().execute();
                }
            }
        });
    }

    private void init() {
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPassword2 = findViewById(R.id.inputPassword2);
        buttonRegister = findViewById(R.id.btnRegister);
        backBtn = findViewById(R.id.registerBackMainBtn);
    }

    private class UserTask extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {

            Response response = null;
            try {
                String data = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"password_confirmation\":\"%s\"}",
                        username, email, password, password2);
                response = RequestHandler.post(MainActivity.URL + "register", data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Log.e("RegisterResponseCode", "onPostExecute: " + response.getResponseCode());
            Log.e("RegisterResponseCode", "onPostExecute: " + response.getContent());
            if (response == null || response.getResponseCode() >= 400){
                Toast.makeText(RegisterActivity.this, "An error occurred during registration", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(RegisterActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            }
            Intent backToMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(backToMainIntent);
            finish();
        }
    }
}