package com.example.carlijnquik.carlijnquik_pset6;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Retrieves string of data from the URL requested
 **/

public class HttpRequestHelper {

    protected static synchronized String download(String... params) {
        String result = "";

        try{
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");

            // get response code
            Integer responsecode = urlConnection.getResponseCode();

            // if 200-300, read inputstream
            if(200 <= responsecode && responsecode <= 299){
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while((line = br.readLine())!= null){
                    result += line;
                }
            }

        }catch(Exception e){
            Log.e("Connection failed",e.getMessage(),e);
        }

        return result;

    }

}
