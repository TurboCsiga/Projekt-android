package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.projekt.databinding.ActivityMainBinding;
import com.example.projekt.databinding.ActivityProfileBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private List<User> users = new ArrayList<>();
    private SharedPreferences prefs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RequestTask task = new RequestTask();
        task.execute();
        RequestTask2 task2 = new RequestTask2();

        prefs = ProfileActivity.this.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        binding.profileBackMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileBackMainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(profileBackMainIntent);
                finish();
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task2.execute();
            }
        });
    }


    private class RequestTask extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                String token = prefs.getString("token", null);
                response = RequestHandler.getWithAuth(
                        "http://192.168.1.45:8000/api/user",
                            token
                        );
            } catch (IOException e) {
                Log.d("Hiba", e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            User user= converter.fromJson(response.getContent(), User.class);
            binding.usernameTextView.setText(user.getName());
            binding.emailTextView.setText(user.getEmail());
            binding.gunauthorizedTextView.setText(user.getGun_authorized() == 0 ? "false" : "true");
        }
    }

    private class RequestTask2 extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                String token = prefs.getString("token", null);
                response = RequestHandler.logOutPost(
                        "http://192.168.1.45:8000/api/logout",
                        token
                );
            } catch (IOException e) {
                Log.d("Hiba", e.toString());
            }
            return response;
        }
    }

}