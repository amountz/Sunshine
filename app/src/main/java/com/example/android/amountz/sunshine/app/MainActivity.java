package com.example.android.amountz.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Uri geoUriStringFromZipCode(String zipcode) {
        final Geocoder geocoder = new Geocoder(this);
        String geoString = "geo:37.4,-122.1"; //default map to 94043

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(zipcode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                geoString = "geo:" + address.getLatitude() + "," + address.getLongitude();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't get geocode from zipcode!");
            e.printStackTrace();
        }

        Log.v(LOG_TAG, geoString);
        return Uri.parse(geoString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map_location) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String location = prefs.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoUriStringFromZipCode(location));
            if (intent.resolveActivity(getPackageManager()) != null) {
                // make sure there is an intent to handle this before launching
                startActivity(intent);
            }
            else Log.e(LOG_TAG, "Can't resolve activity to launch map intent");
        }
        return super.onOptionsItemSelected(item);
    }
}
