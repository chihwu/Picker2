package com.example.chihwu.picker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import dataObjects.User;
import database.PickersDB;

public class SignInActivity extends AppCompatActivity implements OnClickListener{

    private SharedPreferences savedValues;
    private TextView email_textview;
    private Button signinBtn;
    private PickersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email_textview = (TextView)findViewById(R.id.email_editTxt);

        // create a SharedPreferences instance to save user input when coming back to this activity
        savedValues = getSharedPreferences("SavedUsername", MODE_PRIVATE);

        signinBtn = (Button)findViewById(R.id.signin_btn);
        signinBtn.setOnClickListener(this);

        db = MainActivity.pickersDB;

    }



    @Override
    public void onClick(View v){

        SharedPreferences.Editor saver = savedValues.edit();
        saver.putString("email", email_textview.getText().toString());
        saver.commit();

        switch(v.getId()) {
            case R.id.signin_btn:
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);

                String inputUserEmail = email_textview.getText().toString();
                User user = db.getUserByEmail(inputUserEmail);
                if(user == null)
                {
                    Toast.makeText(this, "No matching user is found.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(this, "Sign In Successfully.", Toast.LENGTH_SHORT).show();
                    // save the username just input by the user in the intent object so that the value can be passed to the next activity
                    intent.putExtra("username", user.getUserName());
                    intent.putExtra("userID", user.getId());

                    FileOutputStream fos;
                    try
                    {
                        fos = openFileOutput("user_signin.txt", Context.MODE_APPEND);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String signin_record = user.getUserName() + " signed in on "+dateFormat.format(date)+"\n";
                        Log.i("IMPORTANT INFO ",signin_record);
                        Log.i("IMPORTANT INFO ",signin_record.getBytes().toString());
                        fos.write(signin_record.getBytes());
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(this, "User signin record is not saved successfully.", Toast.LENGTH_SHORT).show();
                    }

                    try
                    {
                        FileInputStream fis;
                        fis = openFileInput("user_signin.txt");
                        byte[] reader = new byte[fis.available()];

                        while(fis.read(reader) != -1)
                        {

                        }

                        CharSequence text1 =new String(reader);
                        Toast.makeText(this, text1, Toast.LENGTH_SHORT).show();

                    }
                    catch(IOException e)
                    {

                    }

                    startActivity(intent);
                }

                break;

        }

    }

    @Override
    public void onResume()
    {
        super.onResume();

        // when this activity is back onto the foreground, make sure the username can be printed again in the username_textview widget
        String stored_email = savedValues.getString("email", "");
        email_textview.setText(stored_email);
    }
}
