package com.example.chihwu.picker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

/*
 *  Known Issue:  This activity is created mainly for users to view their login history which is saved in a text file.
 */
public class LoginHistoryActivity extends AppCompatActivity {

    // Post: The user login history will be populated on the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_history);

        TextView loginHistory = (TextView)findViewById(R.id.login_history);

        try
        {
            FileInputStream fis;
            fis = openFileInput("user_signin.txt");
            byte[] reader = new byte[fis.available()];

            while(fis.read(reader) != -1)
            {

            }

            CharSequence text1 =new String(reader);
            loginHistory.setText(text1);

        }
        catch(IOException e)
        {

        }

    }
}
