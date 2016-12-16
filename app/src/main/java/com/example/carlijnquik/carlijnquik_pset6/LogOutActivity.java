package com.example.carlijnquik.carlijnquik_pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Enables user to log out.
 * Source used: https://github.com/firebase/quickstart-android/tree/master/auth/app/src/main.
 **/

public class LogOutActivity extends AppCompatActivity {

    Button Log_Out;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        // initialize firebase
        mAuth = FirebaseAuth.getInstance();

        // initialize listener
        Log_Out = (Button) findViewById(R.id.log_out_button);
        Log_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

}
