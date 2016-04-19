package com.example.chihwu.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.google.tabmanager.TabManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.telephony.TelephonyManager;

//Known issues: Profile Activity contains the FunctionListFragment and now displays an action bar for menu

public class ProfileActivity extends AppCompatActivity implements OnItemClickListener{

    private TextView username_textview;
    private TextView profile_greeting_textview;
    private TabHost tabHost;
    private TabManager tabManager;

    private BatteryStatusReceiver batteryStatusReceiver; //A Broadcast instance used to detect the broadcast for current battery status
    private IntentFilter intentFilter;



    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the tab set up
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

        String[] tabTitles = {"Profile", "Contacts"};

        for(String tabTitle : tabTitles)
        {
            TabSpec tabSpec = tabHost.newTabSpec(tabTitle);
            tabSpec.setIndicator(tabTitle);
            tabManager.addTab(tabSpec, FunctionListFragment.class, null);
        }



        if(savedInstanceState != null)
        {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }



        // I created an actionbar for displaying menu in this activity as the tabs function used in this activity prevents a default actionbar from displaying
        // change the color of the default action bar to our dark blue theme
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Pickers </font>"));


        batteryStatusReceiver = new BatteryStatusReceiver();  // instantiate our inner class BatteryStatusReceiver for receiving the broadcast for the device's battery status update
        intentFilter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");

        registerReceiver(batteryStatusReceiver, intentFilter);  // register our Broadcase Receiver for specific "ACTION_POWER_CONNECTED" Intent action
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_settings, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:  // accessing the SearchOnMapActivity for displaying the map
                Intent searchOnMapIntent = new Intent(this, SearchOnMapActivity.class);
                startActivity(searchOnMapIntent);
                return true;
            case R.id.login_history:  // accessing the LoginHistoryActivity for showing login history
                Intent checkLoginHistoryIntent = new Intent(this, LoginHistoryActivity.class);
                startActivity(checkLoginHistoryIntent);
                return true;
            case R.id.invite_friends:   // accessing the AddFriendsActivity for contacts list
                Intent invitationIntent = new Intent(this, AddFriendsActivity.class);
                startActivity(invitationIntent);
                return true;
            case R.id.menu_contact: //call the application author via my phone number using the CALL_PHONE permission.

                TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if(telMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
                    String number = "tel:508-873-6360";
                    Uri callUri = Uri.parse(number);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, callUri);
                    startActivity(callIntent);
                }

                return true;
            case R.id.menu_connection:   // show the network connection status when selected
                Intent connectionStatusCheckIntent = new Intent(this, ConnectionStatusCheckActivity.class);
                startActivityForResult(connectionStatusCheckIntent, 1);
                return true;
            case R.id.our_website:  // access the website of our school
                new WebAccessTask().execute();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {

    }

    // This class is used to receive the Broadcast for device's current battery status
    private class BatteryStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Toast.makeText(ProfileActivity.this, "The device is connected to a power supply.", Toast.LENGTH_SHORT).show();
        }

    }

    // This class is used for using multi-threading to access the web when users click the "Our Website" item in the Menu in the Actionbar
    private class WebAccessTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            String link = "http://www.bu.edu";
            Uri uri = Uri.parse(link);
            // send a VIEW action to the target uri
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(viewIntent);
            return link;
        }

        @Override
        protected void onPostExecute(String link)
        {
            Toast.makeText(ProfileActivity.this, "Connecting to "+link, Toast.LENGTH_SHORT).show();
        }

    }
}
