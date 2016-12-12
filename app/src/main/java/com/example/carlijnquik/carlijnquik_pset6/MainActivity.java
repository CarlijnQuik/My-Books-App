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

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    String name;
    String gender;
    EditText name_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        name_input = (EditText) findViewById(R.id.name_input);
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

        // Check which checkbox was clicked
        switch ((view).getId()) {
            case R.id.male:
                if (checked)
                    gender = "Male";
                break;
            case R.id.female:
                if (checked)
                    gender = "Female";
                break;
            case R.id.other:
                if (checked)
                    gender = "Other";
                break;
        }
    }

    public void Go(View view) {
        name = name_input.getText().toString();

        if ( !(name.length() == 0)){
            // save user
            prefs.edit().putString("name", name).apply();
            prefs.edit().putString("gender", gender).apply();

            // ask user whether the input is correct
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            forward();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            name_input.setText("");
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Is " + name + " your name?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        else {
            Toast toast = Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
