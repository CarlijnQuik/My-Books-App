package com.example.carlijnquik.carlijnquik_pset6;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this_activity file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Source: https://github.com/firebase/quickstart-android/tree/master/auth/app/src/main
 * Controls the log in activity so multiple users can use the app
 * Edited by Carlijn Quik in december 2016
 */

public class LogInActivity extends LogInProgressDialog implements
        View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initalizeFirebase();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        findViewById(R.id.bLogIn).setOnClickListener(this);
        findViewById(R.id.bCreateAccount).setOnClickListener(this);

    }

    public void initalizeFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // user is logged in
                    forwardExistingUser();
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.bCreateAccount) {
            createAccount(etEmail.getText().toString(), etPassword.getText().toString());
        } else if (i == R.id.bLogIn) {
            logIn(etEmail.getText().toString(), etPassword.getText().toString());
        }

    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // start create user with email
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // if sign in fails display a message, if sign in succeeds notify auth state listener
                        if (!task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, R.string.auth_failed, LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            forwardNewUser();
                        }

                        hideProgressDialog();
                    }
                });

    }

    private void logIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // start log in with email
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message, if sign in succeeds notify auth state listener
                        if (!task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, R.string.auth_failed, LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            forwardExistingUser();
                        }

                        hideProgressDialog();
                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;

    }

    public void forwardNewUser() {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
        finish();

    }

    public void forwardExistingUser() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

}