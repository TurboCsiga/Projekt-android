package com.example.projekt;

import static android.media.CamcorderProfile.get;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekt.databinding.ActivityMainBinding;
import com.example.projekt.databinding.ItemsListItemBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.1.45:8000/api/";

    ActivityMainBinding binding;
    List<Item> items = new ArrayList<>();

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FrameLayout frameLayout;

    Item actual;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.adatok.setAdapter(new ItemAdapter());
        drawerLayout = binding.drawerLayout;
        toolbar = binding.toolbar;
        navigationView = binding.navigationView;
        frameLayout = binding.fragmentContainer;
        prefs = MainActivity.this.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        RequestTask task = new RequestTask();
        task.execute();


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.loginPage:
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                    break;
                case R.id.profilePage:
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    finish();
                    break;
                case R.id.registerPage:
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                    finish();
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private class ItemAdapter extends ArrayAdapter<Item>{
        public ItemAdapter() {
            super(MainActivity.this, R.layout.items_list_item, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            RequestTask2 task2 = new RequestTask2();
            ItemsListItemBinding listItemBinding = ItemsListItemBinding.inflate(getLayoutInflater());
            actual = items.get(position);
            listItemBinding.name.setText(actual.getName());
            listItemBinding.price.setText(String.format(" %d", actual.getPrice()));
            listItemBinding.imgView.setImageURI(Uri.parse(String.format(" %d", actual.getImage())));
            listItemBinding.btnBuy.setOnClickListener(v -> {
                    task2.execute();
            });
            return listItemBinding.getRoot().getRootView();

        }
    }

    private class RequestTask2 extends  AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            String data = String.format("[{\"item_id\":\"%s\",\"quantity\":\"%s\"}]", actual.getIdAsString(), "1");
            Log.e("OrderRequest", "doInBackground: " + data );
            try {
                response = RequestHandler.postWithAuth(
                        "http://192.168.1.45:8000/api/orders/new",
                        data,
                        prefs.getString("token", null)
                );
            } catch (IOException e) {
                Log.d("Hiba", e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Toast.makeText(getApplicationContext(), "Order sent", Toast.LENGTH_SHORT).show();
            Log.e("OrderResponsedCode", "onPostExecute: " + response.getResponseCode() );
        }
    }

    private class RequestTask  extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
           Response response = null;
            try {
                response = RequestHandler.get("http://192.168.1.45:8000/api/items");
            } catch (IOException e) {
                Log.d("Hiba", e.toString());
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            binding.progressBar.setVisibility(View.GONE);
            Gson converter = new Gson();
            Item[] itemArray = converter.fromJson(response.getContent(), Item[].class);
            items.clear();
            items.addAll(Arrays.asList(itemArray));
        }


    }
}