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
import android.widget.TextView;

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

    DatabaseReference dataRef;
    FirebaseUser user;
    ListView listOfBooks;
    ArrayList<Book> books;
    Book book;
    AlertDialog.Builder builder;
    BookAdapter bookAdapter;
    Query myBooksQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        books = new ArrayList<>();

        // initialize firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataRef = FirebaseDatabase.getInstance().getReference();
        myBooksQuery = dataRef.child("Users").child(user.getUid()).child("Books").orderByKey();

        // set the list adapter
        listOfBooks = (ListView) findViewById(R.id.my_books);
        bookAdapter = new BookAdapter(this, books);
        listOfBooks.setAdapter(bookAdapter);

        setMenu();
        setInstruction();
        setListeners();

    }

    public void setInstruction(){
        TextView tvInstruction = (TextView) findViewById(R.id.tvInstruction);
        if(books.isEmpty()){
            tvInstruction.setText(R.string.empty_list);
        }
        else{
            tvInstruction.setText(R.string.delete_instruction);
        }
    }

    public void setListeners(){
        // firebase database and adapter
        myBooksQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Book book = dataSnapshot.getValue(Book.class);
                books.add(book);
                bookAdapter.notifyDataSetChanged();
                setInstruction();
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

        // clicking a book
        listOfBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book = (Book) parent.getAdapter().getItem(position);

                // sent user to book details
                goToDetails();
            }
        });

        builder = new AlertDialog.Builder(this);
        listOfBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                book = (Book) parent.getAdapter().getItem(position);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button_custom clicked
                                dataRef.child("Users").child(user.getUid()).child("Books").child(book.firebasekey).removeValue();
                                books.remove(book);
                                bookAdapter.notifyDataSetChanged();
                                setInstruction();
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

    }

    public void goToDetails(){
        String id = book.getId();
        String title = book.getTitle();
        String author = book.getAuthor();
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
    public void setMenu(){

        ImageButton ibMyBooks = (ImageButton) findViewById(R.id.ibMyBooks);;
        ibMyBooks.setImageResource(R.drawable.this_act);

        findViewById(R.id.ibHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        findViewById(R.id.ibSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        findViewById(R.id.ibLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LogOutActivity.class));
            }
        });

    }

}






