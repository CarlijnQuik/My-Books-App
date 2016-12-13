package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.carlijnquik.carlijnquik_pset6.R.id.searchrequest;

public class SearchActivity extends AppCompatActivity {
    public RetrieveBooks http;
    ProgressBar progressbar;
    String API_BASE_URL = "http://openlibrary.org/";
    String SEARCH_URL = "search.json?q=";
    ListView found_list;
    String page = "&page=";
    EditText search;
    String query;
    TextView tvCount;
    Button show_more;
    Button previous;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tvCount = (TextView) findViewById(R.id.tvCount);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        show_more = (Button) findViewById(R.id.show_more);
        previous = (Button) findViewById(R.id.previous);
        search = (EditText) findViewById(searchrequest);
        String tag = search.getText().toString();
        query = Uri.encode(tag);


    }

    public void Search(View view) {

        tvCount.setVisibility(TextView.INVISIBLE);
        count = 1;
        String tag = search.getText().toString();
        query = Uri.encode(tag);

        if ( !(query.length() == 0)) {
            progressbar.setVisibility(ProgressBar.VISIBLE);
            String searchRequest = API_BASE_URL + SEARCH_URL + query + page + String.valueOf(count);
            Log.d("viewrequest", searchRequest);
            RetrieveBooks retrieveBooks = new RetrieveBooks(this);
            retrieveBooks.execute(searchRequest);
        }
        else{
            Toast toast = Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    public void other_results(int count){
        progressbar.setVisibility(ProgressBar.VISIBLE);
        String searchRequest = API_BASE_URL + SEARCH_URL + query + page + String.valueOf(count);
        RetrieveBooks retrieveBooks = new RetrieveBooks(this);
        retrieveBooks.execute(searchRequest);
        Log.d("viewrequest2", searchRequest);
        Log.d("viewcount", String.valueOf(count));
    }

    public void show_list(ArrayList<Book> books, String number) {

        search.setText("");
        progressbar.setVisibility(ProgressBar.INVISIBLE);

        // set the listadapter
        found_list = (ListView) findViewById(R.id.found_list);
        BookAdapter bookAdapter = new BookAdapter(this, books);
        found_list.setAdapter(bookAdapter);

        String message = number + " books found!";
        tvCount.setText(message);
        tvCount.setVisibility(TextView.VISIBLE);

        if (books.size() == 100) {
            show_more.setVisibility(Button.VISIBLE);
        }
        if (count > 1){
            previous.setVisibility(Button.VISIBLE);
        }

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

        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_results(count+=1);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_results(count-=1);
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
