package com.example.carlijnquik.carlijnquik_pset6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    public String id = null;
    public String title = null;
    public String author = null;
    public String firebasekey = null;

    public String getId() {

        return this.id;
    }

    public String getTitle() {

        return this.title;
    }

    public String getAuthor() {

        return this.author;
    }

    public String getFirebasekey(){
        return this.firebasekey;
    }

    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + id + "-M.jpg?default=false";
    }

    public String getLargeCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + id + "-L.jpg?default=false";
    }

    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // get book id
            if (jsonObject.has("cover_edition_key")) {
                book.id = jsonObject.getString("cover_edition_key");
            } else if (jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.id = ids.getString(0);
            }
            else {
                book.id = null;
            }

            // get book title
            if (jsonObject.has("title_suggest")){
                book.title = jsonObject.getString("title_suggest");
            } else{
                book.title = null;
            }

            // get author
            if (jsonObject.has("author_name")){
                JSONArray authors = jsonObject.getJSONArray("author_name");
                book.author = authors.getString(0);
            } else{
                book.author = null;
            }

        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return book;
    }
}
