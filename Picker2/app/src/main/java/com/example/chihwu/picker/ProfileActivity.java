package com.example.chihwu.picker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

public class ProfileActivity extends AppCompatActivity {

    private TextView username_textview;
    private TextView profile_greeting_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        // retrieve the username from the intent just passed from the MainActivity
        String username = intent.getStringExtra("username");


        profile_greeting_textview = (TextView)findViewById(R.id.profile_greeting_msg);

        profile_greeting_textview.setText("Welcome back, " + username + ".");

    }
}
