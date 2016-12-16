package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that enables the user to enter a search request.
 */

public class SearchActivity extends AppCompatActivity {

    ImageButton ibHome;
    ImageButton ibMyBooks;
    ImageButton ibSearch;
    ImageButton ibLogOut;
    ImageButton ibRequest;
    EditText etSearch_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setListeners();

    }

    public void initializeViews(){
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        ibMyBooks = (ImageButton) findViewById(R.id.ibMyBooks);
        ibLogOut = (ImageButton) findViewById(R.id.ibLogOut);
        ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        ibRequest = (ImageButton) findViewById(R.id.ibRequest);
        etSearch_request = (EditText) findViewById(R.id.etSearch_request);
        ibSearch.setImageResource(R.drawable.this_act);

        // set EditText to empty
        etSearch_request.setText("");

        // magnifying glass on keyboard
        etSearch_request.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    Search(textView);
                    return true;
                }
                return false;
            }
        });
    }

    public void setListeners(){
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeClicked();
            }
        });
        ibMyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBooksClicked();
            }
        });
        ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutClicked();
            }
        });
    }

    /**
     * Keeps user's last search when they come back to SearchActivity.
     **/
    @Override
    protected void onResume() {
        super.onResume();

    }

    public void Search(View view) {
        // get search request
        String tag = etSearch_request.getText().toString();
        String query = Uri.encode(tag);

        // go to results
        if ( !(query.length() == 0)) {
            Intent goToSearchResults = new Intent(this, SearchResultsActivity.class);
            goToSearchResults.putExtra("query", query);
            startActivity(goToSearchResults);
        }
        else{
            Toast toast = Toast.makeText(this, "Please enter a searchrequest", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void myBooksClicked(){
        Intent goToBooks = new Intent(this, BooksActivity.class);
        startActivity(goToBooks);
    }
    public void homeClicked(){
        Intent goToHome = new Intent(this, HomeActivity.class);
        startActivity(goToHome);
    }
    public void logOutClicked(){
        Intent goToLogOut = new Intent(this, LogOutActivity.class);
        startActivity(goToLogOut);
    }
}
