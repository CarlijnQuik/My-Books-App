package com.example.carlijnquik.carlijnquik_pset6;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    String name;
    String gender;
    EditText name_input;
    Book book;
    FirebaseDatabase database;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("user", this.MODE_PRIVATE);
        name_input = (EditText) findViewById(R.id.name_input);
        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();
        forward();
    }

    // check whether user exists, if so go to menu
    public void forward(){
        name = prefs.getString("name", "");
        if ( !(name.length() == 0)){
            Intent go_to_menu = new Intent(this, MenuActivity.class);
            startActivity(go_to_menu);
            finish();
        }
    }

    // save users gender
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
        name = name_input.getText().toString();
        book.firebasekey = dataRef.child("Users").child(name).child("Books").push().getKey();
        dataRef.child("Users").child(name).child("Books").child(book.firebasekey).setValue(book);
    }

    public void Go(View view) {
        name = name_input.getText().toString();

        if ( !(name.length() == 0)){
            // save user
            prefs.edit().putString("name", name).apply();

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
                            name_input.setText("");
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Is " + name + " your chosen username?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        else {
            Toast toast = Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
