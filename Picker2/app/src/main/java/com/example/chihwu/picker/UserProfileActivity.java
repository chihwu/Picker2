package com.example.chihwu.picker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import dataObjects.User;
import org.w3c.dom.Text;

// This is an activity users will see when they click each user in the contacts list display when they visit the ProfileActivity
public class UserProfileActivity extends AppCompatActivity {

    private TextView usernameTxtView;
    private TextView firstnameTxtView;
    private TextView lastnameTxtView;
    private TextView dobTxtView;
    private TextView emailTxtView;
    private TextView descriptionTxtView;

    //Post: The info of an user will be display in the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usernameTxtView = (TextView)findViewById(R.id.username_info_txtView);
        firstnameTxtView = (TextView)findViewById(R.id.firtname_info_txtView);
        lastnameTxtView = (TextView)findViewById(R.id.lastname_info_txtView);
        dobTxtView = (TextView)findViewById(R.id.dob_info_txtView);
        emailTxtView = (TextView)findViewById(R.id.email_info_txtView);
        descriptionTxtView = (TextView)findViewById(R.id.introduction_info_txtView);

        Intent intent = getIntent();
        int selectedUserIndex = intent.getIntExtra("user_index", 0);
        User user = FunctionListFragment.usersList.get(selectedUserIndex);

        usernameTxtView.setText(user.getUserName());
        firstnameTxtView.setText(user.getFirstName());
        lastnameTxtView.setText(user.getLastName());
        dobTxtView.setText(user.getDateOfBirth());
        emailTxtView.setText(user.getEmail());
        descriptionTxtView.setText(user.getIntroduction());
    }
}
