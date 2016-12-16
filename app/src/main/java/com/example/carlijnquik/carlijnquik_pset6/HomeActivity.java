package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * The home screen from where a user can choose what to do.
 **/

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setMenu();
    }

    /**
     * Initialize menu
     */
    public void setMenu(){

        ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome);
        ibHome.setImageResource(R.drawable.this_act);

        findViewById(R.id.ibMyBooks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BooksActivity.class));
            }
        });

        findViewById(R.id.ibLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LogOutActivity.class));
            }
        });

        findViewById(R.id.ibSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
    }

}
