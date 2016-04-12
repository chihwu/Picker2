package com.example.chihwu.picker;




import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import android.view.View.OnClickListener;

import dataObjects.User;
import database.PickersDB;


import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;


//Known issues: Please note that for the content provider for the contacts to work properly,
// one needs to use a real android device rather than the emulator.
// In addition, the function for the Search tab is still not implemented yet as it requires the use
// GoogleMap API.


public class FunctionListFragment extends Fragment implements OnClickListener {

    private TextView functionTextView;
    private String currentTabTag;

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


    /*Variables for the search Tab*/
    private Button show_on_map_btn;  // for displaying Google map in a Web browser when clicked

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

        User searchedUser = MainActivity.pickersDB.getUser(userID);

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


        } else if (currentTabTag.equalsIgnoreCase("Search"))   //when users select the Search tab
        {
            view = inflater.inflate(R.layout.fragment_map, container, false);

            show_on_map_btn = (Button) view.findViewById(R.id.show_on_map_btn);
            show_on_map_btn.setOnClickListener(this);

        } else if (currentTabTag.equalsIgnoreCase("Contacts"))  //when users select the Contacts tab
        {
            view = inflater.inflate(R.layout.fragment_contacts, container, false);

            String[] columns = {
                    Data._ID,                  // primary key
                    Contacts.DISPLAY_NAME,     // person's name
                    Data.DATA1,                // phone number
                    Data.DATA2                 // phone type (mobile, home, work, etc.)
            };

            //String where = "("+Data.MIMETYPE + "='"+Phone.CONTENT_ITEM_TYPE+"')";
            //String orderBy = Contacts.TIMES_CONTACTED+" DESC";

            Cursor cursor = getActivity().getContentResolver().query(DATA_URI, columns, null, null, null);
            ArrayList<User> contactList = new ArrayList<User>();

            while (cursor.moveToNext())  //keeps going through all the contacts until it reaches the end
            {
                User user = new User();
                user.setUserName(cursor.getString(1));

                contactList.add(user);  // add each User object to the ArrayList object connactList for later displaying in a ListView
            }


            ArrayList<HashMap<String, String>> data = new ArrayList<>();

            for (User user : contactList) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("contact_name", user.getUserName());
                map.put("contact_email", user.getEmail());
                data.add(map);
            }

            int resource = R.layout.contacts_listview_item;
            String[] from = {"contact_name", "contact_email"};
            int[] to = {R.id.contact_name, R.id.contact_email};

            SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to);
            contact_list_view = (ListView) view.findViewById(R.id.contactsListView);
            contact_list_view.setAdapter(adapter);

        }


        //refreshFragment();

        return view;
    }

    /*
    public void refreshFragment() {
        String text = "This is the " + currentTabTag;
        //functionTextView.setText(text);
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        //refreshFragment();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.show_on_map_btn:
                String link = "https://www.google.com/maps";
                Uri uri = Uri.parse(link);
                // send a VIEW action to the target uri
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(viewIntent);
                break;
        }

    }
}
