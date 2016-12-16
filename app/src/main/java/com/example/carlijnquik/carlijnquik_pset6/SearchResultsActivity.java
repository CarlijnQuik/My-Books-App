package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity that shows the list of searchresults a user requested.
 */

public class SearchResultsActivity extends AppCompatActivity {

    ProgressBar progressbar;
    TextView tvCount;
    String API_BASE_URL = "http://openlibrary.org/";
    String SEARCH_URL = "search.json?q=";
    String page = "&page=";
    String query;
    int count;
    Context context;
    ArrayList<Book> books;
    String numberFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // set counter to page 1
        count = 1;

        initializeViews();

        // get query and search
        query = getIntent().getExtras().getString("query", "");
        search(API_BASE_URL + SEARCH_URL + query + page + String.valueOf(count));
    }

    public void initializeViews(){
        String message = "Searching for" + query;
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCount.setText(message);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        progressbar.setVisibility(ProgressBar.VISIBLE);

    }

    public void search(String searchRequest){
        RetrieveBooks retrieveBooks = new RetrieveBooks();
        retrieveBooks.execute(searchRequest);
    }

    /**
     * API request for list of books.
     **/
    public class RetrieveBooks extends AsyncTask<String, Void, String> {

        // doInBackground
        protected String doInBackground(String... params){
            return HttpRequestHelper.download(params);
        }

        //onPostExecute()
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.length() == 0){
                Toast.makeText(context, "Error loading books", Toast.LENGTH_LONG).show();
            }
            else {
                books = new ArrayList<>();
                try {
                    // load data from JSONObject to ArrayList
                    JSONObject data = new JSONObject(result);
                    JSONArray docs = data.getJSONArray("docs");
                    for (int i = 0; i < docs.length(); i++) {
                        JSONObject bookJSON = docs.getJSONObject(i);
                        Book book = Book.fromJson(bookJSON);
                        if(book != null) {
                            books.add(book);
                        }
                    }
                    numberFound = data.getString("num_found");
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                // convert results to ListView
                showList(books, numberFound);
            }
        }

    }

    public void showList(ArrayList<Book> books, String number) {

        // update layout views
        Button next = (Button) findViewById(R.id.next);
        Button previous = (Button) findViewById(R.id.previous);
        progressbar.setVisibility(ProgressBar.INVISIBLE);
        ListView listFound = (ListView) findViewById(R.id.found_list);
        String message = number + " books found!";
        tvCount.setText(message);
        if (books.size() == 100) {
            next.setVisibility(Button.VISIBLE);
        }
        if (count > 1){
            previous.setVisibility(Button.VISIBLE);
        }

        // set list adapter
        BookAdapter bookAdapter = new BookAdapter(this, books);
        listFound.setAdapter(bookAdapter);

        // decide what clicking a book does
        listFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Book book = (Book) parent.getAdapter().getItem(position);
                String id = book.getId();
                String title = book.getTitle();
                String author = book.getAuthor();
                goToDetails(id, title, author);
            }
        });

        // decide what the next button does
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPage(count+=1);
            }
        });

        // decide what the previous button does
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPage(count-=1);
            }
        });
    }

    // get results on other page
    public void switchPage(int count){
        progressbar.setVisibility(ProgressBar.VISIBLE);
        search(API_BASE_URL + SEARCH_URL + query + page + String.valueOf(count));
    }

    public void goToDetails(String id, String title, String author) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("id", id);
        extras.putString("title", title);
        extras.putString("author", author);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
