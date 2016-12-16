package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity that shows the details of a book and enables the user to add it to his/her list.
 */

public class BookDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvAuthor;
    TextView tvPublisher;
    TextView tvPageCount;
    TextView tvDescription;
    ImageView ivBookCover;

    ImageButton ibHome;
    ImageButton ibMyBooks;
    ImageButton ibLogOut;
    ImageButton ibAdd;
    TextView tvAdd;

    String not_available;
    String key_works;
    String id;
    String title;
    String author;

    FirebaseDatabase database;
    DatabaseReference dataRef;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String API_BASE_URL = "http://openlibrary.org/";
    String json = ".json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initializeViews();
        getExtras();
        retrieveDetails();
        initializeFirebase();
        setMenuListeners();
    }

    /**
     * Initialize views in the layout.
     **/
    private void initializeViews(){
        // book details
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        // menu buttons
        ibAdd = (ImageButton) findViewById(R.id.ibSearch);
        ibAdd.setImageResource(android.R.drawable.ic_input_add);
        tvAdd = (TextView) findViewById(R.id.tvSearch);
        tvAdd.setText(R.string.add);
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        ibHome.setImageResource(R.drawable.this_act);
        ibMyBooks = (ImageButton) findViewById(R.id.ibMyBooks);
        ibLogOut = (ImageButton) findViewById(R.id.ibLogOut);
    }

    /**
     * Get intent with information about id, title and author and set views.
     **/
    public void getExtras(){
        Bundle extras = getIntent().getExtras();
        title = extras.getString("title", "");
        author = extras.getString("author", "");
        id = extras.getString("id", "");
        tvTitle.setText(title);
        tvAuthor.setText(author);
        Picasso.with(this).load(Uri.parse(API_BASE_URL + "b/olid/" + id + "-L.jpg?default=false")).error(R.drawable.nocover).into(ivBookCover);
    }

    public void retrieveDetails(){
        String request_details = API_BASE_URL + "books/" + id + json;
        RetrieveDetails retrieveDetails = new RetrieveDetails(this);
        retrieveDetails.execute(request_details);
    }

    /**
     * API request for publishers, number of pages and key to lookup the description.
     * Conversion to views.
     **/
    class RetrieveDetails extends AsyncTask<String, Void, String> {
        BookDetailActivity activity;
        Context context;

        public RetrieveDetails(BookDetailActivity activity) {
            this.activity = activity;
            this.context = this.activity.getApplicationContext();
        }

        // doInBackground
        protected String doInBackground(String... params) {
            return HttpRequestHelper.download(params);
        }

        // onPostExecute()
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            not_available = "Info not available";

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
                        retrieve_description(key_works);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Use key to lookup description.
     **/
    public void retrieve_description(String key){
        String request_description = "http://openlibrary.org" + key + ".json";
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

        // doInBackGround
        protected String doInBackground(String... params){
            return HttpRequestHelper.download(params);
        }

        // onPostExecute()
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.length() == 0){
                Toast.makeText(context, "Error loading books", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    // load data from JSONObject to ArrayList
                    JSONObject data = new JSONObject(result);
                    if (data.has("description")){
                        final JSONObject description = data.getJSONObject("description");
                        String story = description.getString("value");
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

    public void initializeFirebase(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();
    }

    /**
     * Initialize menu.
     **/
    private void setMenuListeners(){
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd();
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

    public void onAdd() {
        // add book to firebase database
        Book book = new Book();
        book.id = id;
        book.title = title;
        book.author = author;
        book.firebasekey = dataRef.child("Books").push().getKey();
        dataRef.child("Users").child(user.getUid()).child("Books").child(book.firebasekey).setValue(book);

        // notify user
        Toast.makeText(this, "Book added to list!", Toast.LENGTH_SHORT).show();
    }
    public void myBooksClicked(){
        Intent goToBooks = new Intent(this, BooksActivity.class);
        startActivity(goToBooks);
    }
    public void logOutClicked(){
        Intent goToLogOut = new Intent(this, LogOutActivity.class);
        startActivity(goToLogOut);
    }

}
