package com.example.carlijnquik.carlijnquik_pset6;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

/**
 * Controls the progress dialog in the log in activity
 * Source: https://github.com/firebase/quickstart-android/tree/master/auth/app/src/main
 **/

public class LogInProgressDialog extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();

    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();

    }

}
