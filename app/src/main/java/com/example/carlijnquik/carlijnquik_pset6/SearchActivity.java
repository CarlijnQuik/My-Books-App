package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public RetrieveBooks http;
    ProgressBar progressbar;
    String API_BASE_URL = "http://openlibrary.org/";
    String SEARCH_URL = "search.json?q=";
    ListView found_list;
    EditText search;
    TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tvCount = (TextView) findViewById(R.id.tvCount);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    public void Search(View view) {

        search = (EditText) findViewById(R.id.searchrequest);
        String tag = search.getText().toString();
        String query = Uri.encode(tag);
        tvCount.setVisibility(TextView.INVISIBLE);

        if ( !(query.length() == 0)) {
            progressbar.setVisibility(ProgressBar.VISIBLE);
            String searchRequest = API_BASE_URL + SEARCH_URL + query;
            RetrieveBooks retrieveBooks = new RetrieveBooks(this);
            retrieveBooks.execute(searchRequest);
        }
        else{
            Toast toast = Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void show_list(ArrayList<Book> books) {

        search.setText("");
        progressbar.setVisibility(ProgressBar.INVISIBLE);

        // set the listadapter
        found_list = (ListView) findViewById(R.id.found_list);
        BookAdapter bookAdapter = new BookAdapter(this, books);
        found_list.setAdapter(bookAdapter);

        int count = books.size();
        String message = String.valueOf(count) + " books found! (max 25)";
        tvCount.setText(message);
        tvCount.setVisibility(TextView.VISIBLE);

        // decide what clicking a book does
        found_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book this_book = (Book) parent.getAdapter().getItem(position);
                String book_id = this_book.getId();
                String book_title = this_book.getTitle();
                String book_author = this_book.getAuthor();
                show_details(book_id, book_title, book_author);

            }
        });

    }

    public void show_details(String id, String title, String author) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("id", id);
        extras.putString("title", title);
        extras.putString("author", author);
        intent.putExtras(extras);
        startActivity(intent);

    }



}
