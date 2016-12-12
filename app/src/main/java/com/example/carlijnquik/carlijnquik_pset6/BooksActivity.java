package com.example.carlijnquik.carlijnquik_pset6;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BooksActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // get user data
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String gender = prefs.getString("gender", "");

        // create database
        String databaseName = name+gender;
        databaseHelper = new DatabaseHelper(this, databaseName);
    }
}
