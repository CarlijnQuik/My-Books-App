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

    String not_available;
    String id;
    String title;
    String author;

    DatabaseReference dataRef;
    FirebaseUser user;

    String API_BASE_URL = "http://openlibrary.org/";
    String json = ".json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dataRef = FirebaseDatabase.getInstance().getReference();

        initializeViews();
        setMenu();
        retrieveDetails();

    }

    /**
     * Initialize views in the layout.
     **/
    private void initializeViews(){

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        ImageView ivBookCover = (ImageView) findViewById(R.id.ivBookCover);

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
            TextView tvPublisher = (TextView) findViewById(R.id.tvPublisher);
            TextView tvPageCount = (TextView) findViewById(R.id.tvPageCount);

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
                        String key_works = key.getString("key");

                        // get description using key
                        retrieveDescription(key_works);
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
    public void retrieveDescription(String key){
        String request_description = API_BASE_URL + key + json;
        RetrieveDescription retrieveDescription = new RetrieveDescription(this);
        retrieveDescription.execute(request_description);

    }

    class RetrieveDescription extends AsyncTask<String, Void, String> {

        BookDetailActivity activity;
        Context context;
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);

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

    /**
     * Initialize menu.
     **/
    private void setMenu(){

        TextView tvAdd = (TextView) findViewById(R.id.tvSearch);
        tvAdd.setText(R.string.add);

        ImageButton ibAdd = (ImageButton) findViewById(R.id.ibSearch);
        ibAdd.setImageResource(android.R.drawable.ic_input_add);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                book.id = id;
                book.title = title;
                book.author = author;
                book.firebasekey = dataRef.child("Books").push().getKey();
                dataRef.child("Users").child(user.getUid()).child("Books").child(book.firebasekey).setValue(book);

                // notify user
                Toast.makeText(getApplicationContext(), "Book added to list!", Toast.LENGTH_SHORT).show();
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

        findViewById(R.id.ibHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

}
