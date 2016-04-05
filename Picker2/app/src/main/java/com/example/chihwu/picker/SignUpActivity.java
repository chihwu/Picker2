package com.example.chihwu.picker;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import dataObjects.User;
import database.PickersDB;


public class SignUpActivity extends FragmentActivity implements OnClickListener {

    private Button dob_btn;
    private Button submit_btn;
    private EditText user_name_txtField;
    private EditText user_firstname_txtField;
    private EditText user_lastname_txtField;
    private EditText user_password_txtField;
    private EditText user_email_txtField;
    private Button user_dob_btn;
    private EditText user_intro_txtField;
    private PickersDB pickersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        pickersDB = MainActivity.pickersDB;

        submit_btn = (Button)findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this);


        user_name_txtField = (EditText)findViewById(R.id.username_editTxt);
        user_firstname_txtField = (EditText)findViewById(R.id.firtname_editTxt);
        user_lastname_txtField = (EditText)findViewById(R.id.lastname_editTxt);
        user_password_txtField = (EditText)findViewById(R.id.password_editTxt);
        user_email_txtField = (EditText)findViewById(R.id.email_editTxt);
        user_dob_btn = (Button)findViewById(R.id.dob_btn);
        user_intro_txtField = (EditText)findViewById(R.id.introduction_editTxt);




    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.dob_btn:
                showDatePickerDialog(v);
                break;
            case R.id.submit_btn:
                signUpUser();
                break;
        }

    }

    public void showDatePickerDialog(View v) {
          Log.i("INFO", "SAY Hi");
          DialogFragment datePickerFragment = new DatePickerFragment();

          datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void signUpUser()
    {

        String user_name = user_name_txtField.getText().toString();
        String user_firstname = user_firstname_txtField.getText().toString();
        String user_lastname = user_lastname_txtField.getText().toString();
        String user_password = user_password_txtField.getText().toString();
        String user_email = user_email_txtField.getText().toString();
        String user_dob = user_dob_btn.getText().toString();
        String user_intro = user_intro_txtField.getText().toString();

        User user = new User(user_name, user_firstname, user_lastname, user_password, user_email, user_dob, user_intro);

        try
        {
            pickersDB.insertUser(user);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "New User Is Created Successfully.",Toast.LENGTH_LONG).show();
        }

    }
}
