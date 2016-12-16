package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    ProgressBar progressbar;
    TextView tvCount;
    ListView found_list;
    Button show_more;
    Button previous;

    String API_BASE_URL = "http://openlibrary.org/";
    String SEARCH_URL = "search.json?q=";
    String page = "&page=";
    String query;
    int count;

    Context context;
    ArrayList<Book> books;
    String number_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // set counter to page 1
        count = 1;

        initializeViews();

        // get searchrequest
        Bundle extras = getIntent().getExtras();
        query = extras.getString("query", "");

        // set views
        progressbar.setVisibility(ProgressBar.VISIBLE);
        String message = "Searching for" + query;
        tvCount.setText(message);
        tvCount.setVisibility(TextView.INVISIBLE);

        // search
        String searchRequest = API_BASE_URL + SEARCH_URL + query + page + String.valueOf(count);
        RetrieveBooks retrieveBooks = new RetrieveBooks();
        retrieveBooks.execute(searchRequest);
    }

    public void initializeViews(){
        tvCount = (TextView) findViewById(R.id.tvCount);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        show_more = (Button) findViewById(R.id.show_more);
        previous = (Button) findViewById(R.id.previous);
        progressbar.setVisibility(ProgressBar.VISIBLE);
    }

    public void show_list(ArrayList<Book> books, String number) {

        // set views
        progressbar.setVisibility(ProgressBar.INVISIBLE);
        String message = number + " books found!";
        tvCount.setText(message);
        tvCount.setVisibility(TextView.VISIBLE);
        if (books.size() == 100) {
            show_more.setVisibility(Button.VISIBLE);
        }
        if (count > 1){
            previous.setVisibility(Button.VISIBLE);
        }

        // set list adapter
        found_list = (ListView) findViewById(R.id.found_list);
        BookAdapter bookAdapter = new BookAdapter(this, books);
        found_list.setAdapter(bookAdapter);

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

        // decide what the next button does
        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_results(count+=1);
            }
        });

        // decide what the previous button does
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_results(count-=1);
            }
        });
    }

    // go to detail activity
    public void show_details(String id, String title, String author) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("id", id);
        extras.putString("title", title);
        extras.putString("author", author);
        intent.putExtras(extras);
        startActivity(intent);

    }

    // get results on other page
    public void other_results(int count){
        progressbar.setVisibility(ProgressBar.VISIBLE);
        String searchRequest = API_BASE_URL + SEARCH_URL + query + page + String.valueOf(count);
        RetrieveBooks retrieveBooks = new RetrieveBooks();
        retrieveBooks.execute(searchRequest);
    }

    // retrieve info from API
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
                    number_found = data.getString("num_found");
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                show_list(books, number_found);
            }
        }

    }
}
