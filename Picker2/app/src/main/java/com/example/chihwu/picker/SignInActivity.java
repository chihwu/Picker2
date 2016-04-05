package com.example.chihwu.picker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;

import dataObjects.User;
import database.PickersDB;

public class SignInActivity extends AppCompatActivity implements OnClickListener{

    private SharedPreferences savedValues;
    private TextView username_textview;
    private Button signinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username_textview = (TextView)findViewById(R.id.username_editTxt);

        // create a SharedPreferences instance to save user input when coming back to this activity
        savedValues = getSharedPreferences("SavedUsername", MODE_PRIVATE);

        signinBtn = (Button)findViewById(R.id.signin_btn);
        signinBtn.setOnClickListener(this);

        PickersDB db = MainActivity.pickersDB;
        User user = db.getUser("Raymond");
        Log.i("IN SIGNIN ", user.getUserName());
    }



    @Override
    public void onClick(View v){

        SharedPreferences.Editor saver = savedValues.edit();
        saver.putString("username", username_textview.getText().toString());
        saver.commit();

        switch(v.getId()) {
            case R.id.signin_btn:
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                // save the username just input by the user in the intent object so that the value can be passed to the next activity
                intent.putExtra("username", username_textview.getText().toString());
                startActivity(intent);
                break;

        }

    }

    @Override
    public void onResume()
    {
        super.onResume();

        // when this activity is back onto the foreground, make sure the username can be printed again in the username_textview widget
        String stored_username = savedValues.getString("username", "");
        username_textview.setText(stored_username);
    }
}
