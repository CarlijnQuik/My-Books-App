package com.example.carlijnquik.carlijnquik_pset6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    // set fields of database schema
    private static String DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "Book";

    private String title_id = "title";
    private String author = "author";

    // constructor
    public DatabaseHelper(Context context, String DATABASE_NAME){
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    // onCreate
    public void onCreate(SQLiteDatabase sqlLiteDatabase){
        String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + title_id + " TEXT, " + author + " TEXT)";
        sqlLiteDatabase.execSQL(CREATE_TABLE);
    }

    // onUpgrade
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    // create
    public void create(Book book){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(title_id, book.title);
        values.put(author, book.author);
        db.insert(TABLE, null, values);
        db.close();
    }

    // read
    public ArrayList<HashMap<String, String>> read(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id , " + title_id + " , " + author + " FROM " + TABLE ;
        ArrayList<HashMap<String, String>> book_list = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> book_data = new HashMap<>();
                book_data.put("id", cursor.getString(cursor.getColumnIndex("_id")));
                book_data.put("name", cursor.getString(cursor.getColumnIndex(title_id)));
                book_data.put("author", cursor.getString(cursor.getColumnIndex(author)));
                book_list.add(book_data);

            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return book_list;
    }

    // update
    public void update(Book book){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(title_id, book.title);
        values.put(author, book.author);
        db.update(TABLE, values, "_id = ? ", new String[] {String.valueOf(book.id)});
        db.close();
    }

    // delete
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}


