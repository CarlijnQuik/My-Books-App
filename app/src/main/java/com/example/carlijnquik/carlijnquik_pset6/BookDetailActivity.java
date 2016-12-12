package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static com.example.carlijnquik.carlijnquik_pset6.R.id.ivBookCover;

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
        not_available = "Info not available";

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id", "");
        title = extras.getString("title", "");
        tvTitle.setText(title);
        author = extras.getString("author", "");
        tvAuthor.setText(author);

        String request_details = "http://openlibrary.org/" + "books/" + id + ".json";
        Log.d("viewlink1", request_details);
        RetrieveDetails retrieveDetails = new RetrieveDetails(this);
        retrieveDetails.execute(request_details);

        Picasso.with(this).load(Uri.parse("http://covers.openlibrary.org/b/olid/" + id + "-L.jpg?default=false")).error(R.drawable.nocover).into(ivBookCover);
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
}
