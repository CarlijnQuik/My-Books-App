package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // welcome user
        SharedPreferences prefs = this.getSharedPreferences("user", this.MODE_PRIVATE);
        String name = prefs.getString("name", "");
        TextView menu_title = (TextView) findViewById(R.id.menu_title);
        menu_title.setText("Welcome " + name + " !");

        // decide what clicking the search_button does
        ImageButton search_button = (ImageButton) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_search = new Intent(MenuActivity.this, SearchActivity.class);
                startActivity(go_to_search);

            }
        });

        // decide what clicking the books_button does
        ImageButton books_button = (ImageButton) findViewById(R.id.books_button);
        books_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go_to_books = new Intent(MenuActivity.this, BooksActivity.class);
                startActivity(go_to_books);

            }
        });
    }
}
