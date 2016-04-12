package com.example.chihwu.picker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.TextView;
import android.view.Menu;
import android.net.Uri;
import android.widget.Toast;

import dataObjects.User;
import database.PickersDB;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button signin_button;
    private Button signup_button;
    private static final String TAG = "MainActivity";
    public static PickersDB pickersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin_button = (Button)findViewById(R.id.signin_btn);
        signin_button.setOnClickListener(this);

        signup_button = (Button)findViewById(R.id.signup_btn);
        signup_button.setOnClickListener(this);


        // Create the database PickersDB
        pickersDB = new PickersDB(this);
        Log.i("DATABASE CREATED : ",pickersDB.toString());


    }

    @Override
    public void onClick(View v){

        Log.i(TAG, "Widget ID: " + v.getId());


        switch(v.getId())
        {
            case R.id.signin_btn:
                Intent intent1 = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent1);
                break;
            case R.id.signup_btn:
                Intent intent2 = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent2);
                break;
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();


    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Create a menu in the actionbar for displaying settings options.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_contact: //call the application author via my phone number using the CALL_PHONE permission.

                String number = "tel:508-873-6360";
                Uri callUri = Uri.parse(number);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, callUri);
                startActivity(callIntent);
                return true;
            case R.id.menu_connection:   // show the network connection status when selected
                Intent connectionStatusCheckIntent = new Intent(this, ConnectionStatusCheckActivity.class);
                startActivityForResult(connectionStatusCheckIntent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) // the requestCode of 1 is for network connection status checking
        {
            if(resultCode == Activity.RESULT_OK) // if the connection is successful
            {
                String result=data.getStringExtra("result");
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) // if the connection is not successful
            {
                String result=data.getStringExtra("result");
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
