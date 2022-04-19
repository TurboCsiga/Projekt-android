package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.IOException;




public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private String email, password;
    private SharedPreferences sharedPreferences;
    private Token token;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = LoginActivity.this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        init();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginBackMainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginBackMainIntent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString();
                if(email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Az email nem lehet üres", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty() || password.length() < 8) {
                    Toast.makeText(LoginActivity.this, "A jelszó minimum 8 karakterből állhat!", Toast.LENGTH_SHORT).show();
                }
                else {
                    new LoginTask().execute();
                }
            }
        });
    }

    private void init() {
        editTextEmail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.btnLogin);
        backBtn = findViewById(R.id.loginBackMainBtn);
    }

    private class LoginTask extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {

            Response response = null;
            try {
                String data = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
                response = RequestHandler.post(MainActivity.URL + "login", data);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response == null){
                Toast.makeText(LoginActivity.this, "Hiba történt a bejelentkezés során", Toast.LENGTH_SHORT).show();
            }
            else if (response.getResponseCode() >= 400) {
                Toast.makeText(LoginActivity.this, response.getContent(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                try {
                    Gson gson = new Gson();
                    UserResponse userResponse = gson.fromJson(response.getContent(), UserResponse.class);
                    Log.e("USerResponse", "onPostExecute: " + userResponse.toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", userResponse.getToken());
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent backToMainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(backToMainIntent);
                finish();
            }
        }
    }
}