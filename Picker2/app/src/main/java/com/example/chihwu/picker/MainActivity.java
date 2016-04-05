package com.example.chihwu.picker;

import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.TextView;

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

        User user = pickersDB.getUser("Raymond");
        Log.i("IN MAIN ", user.getUserName());
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
}
