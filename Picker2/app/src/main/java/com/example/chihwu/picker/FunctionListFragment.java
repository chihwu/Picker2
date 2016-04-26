package com.example.chihwu.picker;





import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import android.view.View.OnClickListener;
import android.location.LocationListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;
import android.location.Criteria;
import dataObjects.User;
import utility.util;
import database.PickersDB;


import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


//Known issues: Please note that for the content provider for the contacts to work properly,
// one needs to use a real android device rather than the emulator.


public class FunctionListFragment extends Fragment implements OnClickListener, LocationListener, OnItemClickListener {

    private TextView functionTextView;
    private String currentTabTag;
    private LocationManager locationManager;

    /*Variables for Profile Info Tab*/
    private TextView user_name_txtView;
    private TextView user_firstName_txtView;
    private TextView user_lastName_txtView;
    private TextView user_password_txtView;
    private TextView user_email_txtView;
    private TextView user_dob_txtView;
    private TextView user_intro_txtView;
    /*****/

    /*Variables for the contacts Tab*/
    private final Uri DATA_URI = Data.CONTENT_URI;  //the CONTENT_URI is a constant containing the Contacts ContentProvider's info from the class android.provider.ContactsContract
    private ListView contact_list_view;  // for displaying all the contacts in a ListView in the tab Contacts of this fragment
    /****/


    /*Variables for the Contacts Tab*/
    private AnimationDrawable d;
    public static ArrayList<User> usersList;  // this list is static and will be shared with other activities so far to share the info of other users of the application.
    /****/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabHost tabHost = (TabHost) container.getParent().getParent();
        currentTabTag = tabHost.getCurrentTabTag();
        View view = null;

        Intent intent = getActivity().getIntent();
        // retrieve the username from the intent just passed from the MainActivity
        String username = intent.getStringExtra("username");
        int userID = intent.getIntExtra("userID", -1);

        /*find the id of the user who is logging in*/
        User searchedUser = MainActivity.pickersDB.getUser(userID);


        // here we create 4 users with static data
        usersList = new ArrayList<>();
        User user1 = new User(10,"John Wesley", "John", "Wesley", "","johnwesley@bu.edu","6/13/1984","",42.424851337047545,-71.06131841156281);
        User user2 = new User(11,"Karen Wang", "Karen", "Wang", "","karenwang@bu.edu","12/12/1990","",42.413851337047245,-71.03231841157281);
        User user3 = new User(12,"Josh Donn", "Josh", "Done", "","joshdonn@gmail.com","3/12/1981","",42.524851436047545,-71.00132841256281);
        User user4 = new User(13,"Rachel Lin", "Rachel", "Lin", "","rachellin@outlook.com","5/4/1986","",42.414851437047645,-71.06131831157281);

        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);
        usersList.add(user4);

        /* Here we created an alarm in order to test the function where a new user appears and a Notification is supposed to be sent to notify the situation */
        Intent alarmIntent = new Intent(getActivity(), NewNearbyUserAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
        AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5*1000, pendingIntent);  // here we set up the setting that we will receive notification of new user nearby in 5 seconds after the switch to the ProfileActivity
        /********/

        // here I created three different tabs
        if (currentTabTag.equalsIgnoreCase("Profile"))   //when users select the Profile tab
        {

            view = inflater.inflate(R.layout.fragment_profile_info, container, false);

            user_name_txtView = (TextView) view.findViewById(R.id.username_info_txtView);
            user_firstName_txtView = (TextView) view.findViewById(R.id.firtname_info_txtView);
            user_lastName_txtView = (TextView) view.findViewById(R.id.lastname_info_txtView);
            user_password_txtView = (TextView) view.findViewById(R.id.password_info_txtView);
            user_email_txtView = (TextView) view.findViewById(R.id.email_info_txtView);
            user_dob_txtView = (TextView) view.findViewById(R.id.dob_info_txtView);
            user_intro_txtView = (TextView) view.findViewById(R.id.introduction_info_txtView);

            user_name_txtView.setText(searchedUser.getUserName());
            user_firstName_txtView.setText(searchedUser.getFirstName());
            user_lastName_txtView.setText(searchedUser.getLastName());
            user_password_txtView.setText(searchedUser.getPassword());
            user_email_txtView.setText(searchedUser.getEmail());
            user_dob_txtView.setText(searchedUser.getDateOfBirth());
            user_intro_txtView.setText(searchedUser.getIntroduction());

            functionTextView = (TextView) view.findViewById(R.id.function_textView);


        }
        else if (currentTabTag.equalsIgnoreCase("Contacts"))  //when users select the Contacts tab
        {

             /*try to get the current location of the current logged-in user*/
            if(locationManager == null)
            {
                locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            }

            // note that here we try to check if the proper permissions are granted before we can use the GPS and Network connection for location detection
            PackageManager pm = getContext().getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getContext().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION GRANTED", "We got the permission!!!!!!!!!!!!!");
                //note that here we try to use both Network and GPS for detecting the location updates just in case either of them might not work all the time
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }


            view = inflater.inflate(R.layout.fragment_contacts, container, false);


            /* Populate the contact list with other users' data below */
            ArrayList<HashMap<String, String>> data = new ArrayList<>();

            for (User user : usersList) {

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("contact_name", user.getUserName());
                map.put("contact_email", user.getEmail());
                data.add(map);

            }

            int resource = R.layout.contacts_listview_item;
            String[] from = {"contact_name", "contact_email"};
            int[] to = {R.id.contact_name, R.id.contact_email};

            SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to);
            /*********/

            // here we try to load the Refresh button identified by the Refresh icon, which uses some animation from AnimationDrawable class.
            Button refreshBtn=(Button)view.findViewById(R.id.refresh_button);
            refreshBtn.setOnClickListener(this);

            d =(AnimationDrawable)refreshBtn.getCompoundDrawables()[0];
            /*********/



            contact_list_view = (ListView)view.findViewById(R.id.contactsListView);
            contact_list_view.setAdapter(adapter);

            contact_list_view.setOnItemClickListener(this);


        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            // when users click the refresh button, the distance info of each other user is recalculated.
            case R.id.refresh_button:
                d.start();  // where users click the refresh button, the animation for this button will start

                // Next we will re-calculate and reload the distance data of each nearby users.
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);

                PackageManager pm = getContext().getPackageManager();
                int hasPerm = pm.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getContext().getPackageName());
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {

                    // note that here we only try to update our location just once to prevent unnecessary continuous updating in the thread.
                    locationManager.requestSingleUpdate(criteria, new LocationListener() {

                        @Override
                        public void onLocationChanged(Location location) {

                            Log.i("Single Location Update" , Double.toString(location.getLatitude())+", "+Double.toString(location.getLongitude()));
                            d.stop();
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                            // TODO Auto-generated method stub

                        }

                    }, null);
                }

                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Log.i("ITEM CLICKED", Integer.toString(position));
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra("user_index",position);
        startActivity(intent);
    }



    // This method is triggered whenever there is an update in users' location
    @Override
    public void onLocationChanged(Location location) {

        for (int index=0 ; index<usersList.size(); index++) {
            View view = contact_list_view.getChildAt(index);
            TextView distance_txt_view = (TextView)view.findViewById(R.id.contact_distance);

            // here we use an utility function created ourselves to calculate the user's distance with other nearby users using the latitude and logitude
            double distance = util.distance(location.getLatitude(),location.getLongitude(),usersList.get(index).getCurrentLat(),usersList.get(index).getCurrentLong(),"K");
            distance_txt_view.setText(String.format("%.4f", distance)+" km");
        }

        Log.i("LOCATION ", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //Toast.makeText(getActivity(), "Latitude:" + Double.toString(location.getLatitude()) + ", Longitude:" + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

}
