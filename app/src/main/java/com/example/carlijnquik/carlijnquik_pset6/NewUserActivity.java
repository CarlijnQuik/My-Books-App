package com.example.carlijnquik.carlijnquik_pset6;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity that let's a new user pick an example book to add to his or her list.
 */

public class NewUserActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dataRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        initializeFirebase();

    }

    public void initializeFirebase(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();
    }

    public void onRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();
        book = new Book();
        // Check which checkbox was clicked
        switch ((view).getId()) {
            case R.id.potter:
                if (checked)
                    book.id = "OL23999169M";
                    book.title = "Harry Potter and the prisoner of Azkaban";
                    book.author = "J. K. Rowling";
                break;
            case R.id.benjamin:
                if (checked)
                    book.id = "OL7576605M";
                    book.title = "The Autobiography of Benjamin Franklin";
                    book.author = "Benjamin Franklin";
                break;
            case R.id.tiger:
                if (checked)
                    book.id = "OL17793259M";
                    book.title = "The Teeth of the Tiger";
                    book.author = "Maurice Leblanc";
                break;
        }

    }

    public void Go(View view) {

            // add chosen book to database
            dataRef.child("Users").child(user.getUid()).child("Books").child(book.id).setValue(book);

            // ask user whether the input is correct
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button_custom clicked
                            forward();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button_custom clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to continue?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
    }

    public void forward(){
        Intent go_to_menu = new Intent(this, HomeActivity.class);
        startActivity(go_to_menu);
        finish();
    }

}
