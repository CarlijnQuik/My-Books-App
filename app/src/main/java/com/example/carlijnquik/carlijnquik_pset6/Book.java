package com.example.carlijnquik.carlijnquik_pset6;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Book object
 */

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

    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + id + "-M.jpg?default=false";
    }

    public String getLargeCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + id + "-L.jpg?default=false";
    }

    /**
     * Obtain data from JSON Object and add to book where possible.
     **/
    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            if (jsonObject.has("cover_edition_key")) {
                book.id = jsonObject.getString("cover_edition_key");
            } else if (jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.id = ids.getString(0);
            } else {
                book.id = null;
            }

            if (jsonObject.has("title_suggest")) {
                book.title = jsonObject.getString("title_suggest");
            } else {
                book.title = null;
            }

            if (jsonObject.has("author_name")) {
                JSONArray authors = jsonObject.getJSONArray("author_name");
                book.author = authors.getString(0);
            } else {
                book.author = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return book;
    }

}
