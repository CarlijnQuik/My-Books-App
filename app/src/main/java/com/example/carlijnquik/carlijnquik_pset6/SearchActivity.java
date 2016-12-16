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

    EditText etSearchRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // set EditText to empty
        etSearchRequest = (EditText) findViewById(R.id.etSearch_request);

        // magnifying glass on keyboard
        etSearchRequest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    search(textView);
                    return true;
                }
                return false;
            }
        });

        setMenu();

    }

    /**
     * Keeps user's last search when they come back to SearchActivity.
     **/
    @Override
    protected void onResume() {
        super.onResume();

    }

    public void search(View view) {
        // get search request
        String tag = etSearchRequest.getText().toString();
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

    /**
     * Initialize menu.
     **/
    public void setMenu(){

        ImageButton ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        ibSearch.setImageResource(R.drawable.this_act);

        findViewById(R.id.ibHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

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

    }

}
