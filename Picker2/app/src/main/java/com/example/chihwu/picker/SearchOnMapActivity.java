package com.example.chihwu.picker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;
import android.content.pm.PackageManager;

import dataObjects.User;


/*
 *   Known Issue:  This activity is used to display the user's current location along with other nearby users' locations
 *   on the Google Map using Google Map Android API
 */
public class SearchOnMapActivity extends AppCompatActivity implements LocationListener {

    private GoogleMap map;
    private FragmentManager manager;
    private SupportMapFragment supportMapfragment;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_on_map);

        if(locationManager == null)
        {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }


        PackageManager pm = getApplicationContext().getPackageManager();
        int hasPerm = pm.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext().getPackageName());
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSION GRANTED", "We got the permission!!!!!!!!!!!!!");

            //note that here we try to use both Network and GPS for detecting the location updates just in case either of them might not work all the time
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

        }




        if(map == null)
        {

            manager = getSupportFragmentManager();
            supportMapfragment = (SupportMapFragment)manager.findFragmentById(R.id.map);
            map = supportMapfragment.getMap();
        }

        if(map != null)
        {
            map.getUiSettings().setZoomControlsEnabled(true);  // allow zoomin
            map.getUiSettings().setCompassEnabled(true);   // display compass widget
            map.getUiSettings().setZoomGesturesEnabled(true);  // allow users to use gesture to manipulate the map
        }



    }

    // This method is triggered whenever there is an update in users' location
    @Override
    public void onLocationChanged(Location location) {

        if(map != null)
        {
            // here we zoom in to the user's current location
            map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(16.5f).bearing(0).tilt(25).build()));


        }

        if(map != null)
        {
            map.clear();
            // here we add a marker for the user's current location
            map.addMarker(
                    new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here now."));

        }

        for(User user : FunctionListFragment.usersList)
        {


            // here we add more markers for other users nearby on the map
            map.addMarker(
                    new MarkerOptions().position(new LatLng(user.getCurrentLat(), user.getCurrentLong())).title(user.getUserName() + " is here now.").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }


        Log.i("LOCATION ", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //Toast.makeText(this, "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(), Toast.LENGTH_LONG).show();
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
