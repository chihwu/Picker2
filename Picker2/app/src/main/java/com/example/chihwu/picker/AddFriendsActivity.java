package com.example.chihwu.picker;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


import dataObjects.User;

/*
 *  Known Issue: This activity is created for users to invite their friends from their google contacts
 *  to use this application though an email message.
 */

public class AddFriendsActivity extends AppCompatActivity implements OnItemClickListener {

    /*Variables for the contacts Tab*/
    private final Uri DATA_URI = ContactsContract.Data.CONTENT_URI;  //the CONTENT_URI is a constant containing the Contacts ContentProvider's info from the class android.provider.ContactsContract
    private ListView contact_list_view;  // for displaying all the contacts in a ListView in the tab Contacts of this fragment
    private ArrayList<User> contactList;
    private ArrayList<HashMap<String, String>> data;
    /****/

    // Post: The users' contacts will be populated on the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contacts);

        ListView contactsListView = (ListView)findViewById(R.id.contactsListView);
        contactsListView.setOnItemClickListener(this);

        String[] columns = {
                ContactsContract.Data._ID,                  // primary key
                ContactsContract.Contacts.DISPLAY_NAME,     // person's name
                ContactsContract.Data.DATA1,                // phone number
                ContactsContract.Data.DATA2                 // phone type (mobile, home, work, etc.)
        };

        //String where = "("+Data.DATA1 + " IS NOT NULL)";

        //String orderBy = Contacts.TIMES_CONTACTED+" DESC";

        Cursor cursor = getContentResolver().query(DATA_URI, columns, null, null, null);
        contactList = new ArrayList<User>();

        while (cursor.moveToNext())  //keeps going through all the contacts until it reaches the end
        {
            User user = new User();
            user.setUserName(cursor.getString(1));

            //Log.i("1", cursor.getString(1));

            if(cursor.getString(2) != null) {
                //Log.i("2", cursor.getString(2));
                user.setEmail(cursor.getString(2));
            }



            contactList.add(user);  // add each User object to the ArrayList object connactList for later displaying in a ListView
        }


        data = new ArrayList<>();

        for (User user : contactList) {
            if(user.getEmail() != null && !user.getEmail().trim().equalsIgnoreCase("") && user.getEmail().indexOf("@")>0)   // we want to make sure to only save the contacts with valid email address so that we can contact them.
            {
                HashMap<String, String> map = new HashMap<String, String>();
                //Log.i("contact_name", user.getUserName());
                //Log.i("contact_email", user.getEmail());
                map.put("contact_name", user.getUserName());  // here we display both user's username and email in the contact list
                map.put("contact_email", user.getEmail());
                data.add(map);
            }

        }

        int resource = R.layout.contacts_listview_item;
        String[] from = {"contact_name", "contact_email"};
        int[] to = {R.id.contact_name, R.id.contact_email};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        contact_list_view = (ListView)findViewById(R.id.contactsListView);
        contact_list_view.setAdapter(adapter);
    }


    // This method is triggered when users click a user from the contact list
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Log.i("POSITION", Integer.toString(position));

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        Log.i("EMAIL ", contactList.get(position).getUserName());
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{data.get(position).get("contact_email")});  // set up the receiver's emaill address
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation To Use Pickers");  // set up the email's subject
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Come to use Pickers!!! I am using it.");   // set up the receiver's emaill content

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
