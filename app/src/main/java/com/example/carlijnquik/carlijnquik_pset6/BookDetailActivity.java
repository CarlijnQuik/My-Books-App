package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvPublisher;
    TextView tvPageCount;
    TextView tvDescription;
    ImageView ivBookCover;
    String not_available;
    String key_works;
    String id;
    String title;
    String author;
    ImageView books;
    ImageButton ibAdd;
    FirebaseDatabase database;
    DatabaseReference dataRef;
    String name;
    ImageButton ibHome;
    ImageButton ibMyBooks;
    ImageButton ibSearch;
    ImageButton ibLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        ibAdd = (ImageButton) findViewById(R.id.ibSearch);
        ibAdd.setImageResource(android.R.drawable.ic_input_add);
        TextView tvAdd = (TextView) findViewById(R.id.tvSearch);
        tvAdd.setText("Add Book");
        not_available = "Info not available";

        // views
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        ibMyBooks = (ImageButton) findViewById(R.id.ibMyBooks);
        ibLogOut = (ImageButton) findViewById(R.id.ibLogOut);

        ibHome.setImageResource(R.drawable.this_act);
        ibMyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                My_Books_Clicked();
            }
        });
        ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log_Out_Clicked();
            }
        });
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd();
            }
        });

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id", "");
        title = extras.getString("title", "");
        tvTitle.setText(title);
        author = extras.getString("author", "");
        tvAuthor.setText(author);

        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();
        SharedPreferences prefs = this.getSharedPreferences("user", this.MODE_PRIVATE);
        name = prefs.getString("name", "");

        String request_details = "http://openlibrary.org/" + "books/" + id + ".json";
        RetrieveDetails retrieveDetails = new RetrieveDetails(this);
        retrieveDetails.execute(request_details);

        Picasso.with(this).load(Uri.parse("http://covers.openlibrary.org/b/olid/" + id + "-L.jpg?default=false")).error(R.drawable.nocover).into(ivBookCover);


    }

    public void onAdd() {
        Book book = new Book();
        book.id = id;
        book.title = title;
        book.author = author;
        book.firebasekey = dataRef.child("Books").push().getKey();
        dataRef.child("Users").child(name).child("Books").child(book.firebasekey).setValue(book);
        Toast toast = Toast.makeText(this, "Book added to list!", Toast.LENGTH_SHORT);
        toast.show();
    }

    class RetrieveDetails extends AsyncTask<String, Void, String> {
        BookDetailActivity activity;
        Context context;

        public RetrieveDetails(BookDetailActivity activity) {
            this.activity = activity;
            this.context = this.activity.getApplicationContext();
        }

        protected String doInBackground(String... params) {
            return HttpRequestHelper.download(params);
        }

        //onPostExecute()
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() == 0) {
                Toast.makeText(context, "Error loading books", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject identifiers = new JSONObject(result);
                    if (identifiers.has("publishers")) {
                        final JSONArray publishers = identifiers.getJSONArray("publishers");
                        tvPublisher.setText(publishers.getString(0));
                    } else {
                        tvPublisher.setText(not_available);
                    }
                    if (identifiers.has("number_of_pages")) {
                        String pages = identifiers.getString("number_of_pages") + " pages";
                        tvPageCount.setText(pages);
                    }
                    else{
                        tvPageCount.setText(not_available);
                    }
                    if (identifiers.has("works")){
                        final JSONArray works = identifiers.getJSONArray("works");
                        JSONObject key = works.getJSONObject(0);
                        key_works = key.getString("key");
                        execute_details(key_works);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void execute_details (String key){
        String request_description = "http://openlibrary.org" + key + ".json";
        Log.d("viewlink2", request_description);
        RetrieveDescription retrieveDescription = new RetrieveDescription(this);
        retrieveDescription.execute(request_description);

    }

    class RetrieveDescription extends AsyncTask<String, Void, String> {
        BookDetailActivity activity;
        Context context;

        public RetrieveDescription(BookDetailActivity activity){
            this.activity = activity;
            this.context = this.activity.getApplicationContext();
        }

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
                try {
                    // load data from JSONObject to ArrayList
                    JSONObject data = new JSONObject(result);
                    Log.d("viewdata", data.toString());
                    if (data.has("description")){
                        final JSONObject description = data.getJSONObject("description");
                        String story = description.getString("value");
                        Log.d("viewstory", story);
                        tvDescription.setText(story);
                    } else{
                        tvDescription.setText(not_available);
                    }

                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void My_Books_Clicked(){
        Intent goToBooks = new Intent(this, BooksActivity.class);
        startActivity(goToBooks);
    }
    public void Search_Clicked(){
        Intent goToSearch = new Intent(this, SearchActivity.class);
        startActivity(goToSearch);
    }
    public void Log_Out_Clicked(){
        Intent goToLogOut = new Intent(this, LogOutActivity.class);
        startActivity(goToLogOut);
    }

}
