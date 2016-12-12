package com.example.carlijnquik.carlijnquik_pset6;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RetrieveBooks extends AsyncTask<String, Void, String>{
    SearchActivity activity;
    Context context;
    ArrayList<Book> books;

    public RetrieveBooks(SearchActivity activity){
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
            books = new ArrayList<>();
            try {
                // load data from JSONObject to ArrayList
                JSONObject data = new JSONObject(result);
                JSONArray docs = data.getJSONArray("docs");
                for (int i = 0; i < 25; i++) {
                    JSONObject bookJSON = docs.getJSONObject(i);
                    Book book = Book.fromJson(bookJSON);
                    if(book != null) {
                        books.add(book);
                    }
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
            activity.show_list(books);
        }
    }

}