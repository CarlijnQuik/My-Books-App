package com.example.carlijnquik.carlijnquik_pset6;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Activity that shows the list of books the user has compiled.
 */

public class BooksActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dataRef;
    FirebaseAuth mAuth;
    FirebaseUser user;

    ImageButton ibHome;
    ImageButton ibMyBooks;
    ImageButton ibSearch;
    ImageButton ibLogOut;
    ListView listOfBooks;

    ArrayList<Book> books;
    Book this_book;
    AlertDialog.Builder builder;
    BookAdapter bookAdapter;
    Query myBooksQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        books = new ArrayList<>();

        initializeViews();
        initializeFirebase();
        setAdapter();
        setListeners();
    }

    /**
     * Initializes views in the layout.
     **/
    public void initializeViews(){
        listOfBooks = (ListView) findViewById(R.id.my_books);

        // menu buttons
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        ibLogOut = (ImageButton) findViewById(R.id.ibLogOut);
        ibMyBooks = (ImageButton) findViewById(R.id.ibMyBooks);
        ibMyBooks.setImageResource(R.drawable.this_act);
    }

    public void initializeFirebase(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();
        myBooksQuery = dataRef.child("Users").child(user.getUid()).child("Books").orderByKey();
    }

    /**
     * Set the listadapter.
     **/
    public void setAdapter(){
        bookAdapter = new BookAdapter(this, books);
        listOfBooks.setAdapter(bookAdapter);
    }

    public void setListeners(){
        // firebase database and adapter
        myBooksQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Book book = dataSnapshot.getValue(Book.class);
                books.add(book);
                bookAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName){
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // list of books in adapter
        listOfBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                this_book = (Book) parent.getAdapter().getItem(position);
                getBookDetails();
            }
        });

        builder = new AlertDialog.Builder(this);
        listOfBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                this_book = (Book) parent.getAdapter().getItem(position);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button_custom clicked
                                dataRef.child("Users").child(user.getUid()).child("Books").child(this_book.firebasekey).removeValue();
                                books.remove(this_book);
                                bookAdapter.notifyDataSetChanged();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button_custom clicked
                                break;
                        }
                    }
                };
                builder.setMessage("Are you sure you want to delete this book?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });

        // menu
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeClicked();
            }
        });
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchClicked();
            }
        });
        ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutClicked();
            }
        });
    }

    /**
     * Initialize click.
     **/
    public void getBookDetails(){
        String book_id = this_book.getId();
        String book_title = this_book.getTitle();
        String book_author = this_book.getAuthor();
        goToDetails(book_id, book_title, book_author);
    }
    public void goToDetails(String id, String title, String author) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("id", id);
        extras.putString("title", title);
        extras.putString("author", author);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * Initialize menu.
     **/
    public void homeClicked(){
        Intent goToSearch = new Intent(this, HomeActivity.class);
        startActivity(goToSearch);
    }
    public void searchClicked(){
        Intent goToSearch = new Intent(this, SearchActivity.class);
        startActivity(goToSearch);
    }
    public void logOutClicked(){
        Intent goToUser = new Intent(this, LogOutActivity.class);
        startActivity(goToUser);
    }

}






