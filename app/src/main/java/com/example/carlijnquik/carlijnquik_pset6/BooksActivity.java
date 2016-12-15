package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class BooksActivity extends AppCompatActivity {

    public FirebaseDatabase database;
    public DatabaseReference dataRef;
    ListView myBooks;
    ArrayList<Book> books;
    String name;
    ImageButton ibHome;
    ImageButton ibMyBooks;
    ImageButton ibSearch;
    ImageButton ibLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        books = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();
        SharedPreferences prefs = this.getSharedPreferences("user", this.MODE_PRIVATE);
        name = prefs.getString("name", "");
        ibMyBooks = (ImageButton) findViewById(R.id.ibMyBooks);
        ibMyBooks.setImageResource(R.drawable.this_act);

        myBooks = (ListView) findViewById(R.id.my_books);
        final BookAdapter bookAdapter = new BookAdapter(this, books);
        myBooks.setAdapter(bookAdapter);

        // views
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        ibLogOut = (ImageButton) findViewById(R.id.ibLogOut);


        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home_Clicked();
            }
        });
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_Clicked();
            }
        });
        ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log_Out_Clicked();
            }
        });

        Query myBooksQuery = dataRef.child("Users").child(name).child("Books").orderByKey();

        myBooksQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Book book = dataSnapshot.getValue(Book.class);
                books.add(book);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Book newBook = dataSnapshot.getValue(Book.class);
                String commentkey = dataSnapshot.getKey();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName){
                Book movedBook = dataSnapshot.getValue(Book.class);
                String commentkey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        myBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book this_book = (Book) parent.getAdapter().getItem(position);
                String book_id = this_book.getId();
                String book_title = this_book.getTitle();
                String book_author = this_book.getAuthor();
                show_details(book_id, book_title, book_author);
            }
        });

        myBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Book this_book = (Book) parent.getAdapter().getItem(position);
                dataRef.child("Users").child(name).child("Books").child(this_book.firebasekey).removeValue();
                books.remove(this_book);
                bookAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    public void show_details(String id, String title, String author) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("id", id);
        extras.putString("title", title);
        extras.putString("author", author);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void Home_Clicked(){
        Intent goToSearch = new Intent(this, MenuActivity.class);
        startActivity(goToSearch);
    }
    public void Search_Clicked(){
        Intent goToSearch = new Intent(this, SearchActivity.class);
        startActivity(goToSearch);
    }
    public void Log_Out_Clicked(){
        Intent goToUser = new Intent(this, LogOutActivity.class);
        startActivity(goToUser);
    }

}






