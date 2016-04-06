package com.example.chihwu.picker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.google.tabmanager.TabManager;

public class ProfileActivity extends FragmentActivity {

    private TextView username_textview;
    private TextView profile_greeting_textview;
    private TabHost tabHost;
    private TabManager tabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the tab set up
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

        String[] tabTitles = {"Profile", "Search", "Contacts"};

        for(String tabTitle : tabTitles)
        {
            TabSpec tabSpec = tabHost.newTabSpec(tabTitle);
            tabSpec.setIndicator(tabTitle);
            tabManager.addTab(tabSpec, FunctionListFragment.class, null);
        }





//        profile_greeting_textview = (TextView)findViewById(R.id.profile_greeting_msg);
//
//        profile_greeting_textview.setText("Welcome back, " + username + ".");


        if(savedInstanceState != null)
        {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());
    }

}
