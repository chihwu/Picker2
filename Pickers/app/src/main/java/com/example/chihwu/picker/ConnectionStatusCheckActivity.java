package com.example.chihwu.picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/*
 * This activity is created mainly for the internet connection checking requested from \
 * the MainActivity using intent.
 */
public class ConnectionStatusCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_status_check);

        // get NetworkInfo object
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        String connectionStatus = "";

        //check if the NetworkInfo object is null and if it is connected.
        if(networkInfo != null && networkInfo.isConnected())
        {
            connectionStatus = "You are connected to the network.";
            Intent returnIntent = getIntent();
            returnIntent.putExtra("result", connectionStatus);
            // return the resultCode of RESULT_OK to indicate the connection success
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else
        {
            connectionStatus = "You are not connected to the network.";
            Intent returnIntent = getIntent();
            returnIntent.putExtra("result", connectionStatus);
            // return the resultCode of RESULT_CANCELED to indicate the connection failure
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }



    }
}
