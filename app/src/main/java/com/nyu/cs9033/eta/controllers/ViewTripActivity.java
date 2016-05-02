package com.nyu.cs9033.eta.controllers;


import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewTripActivity extends Activity {

    public static final String BLANK = "";
    Button start;
    Button reached;
    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_trip);
        // TODO - fill in here

        Trip trip = getTrip(getIntent());
        viewTrip(trip);
        mSharedPreferences = getSharedPreferences("activeData", Context.MODE_PRIVATE);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean active = mSharedPreferences.getBoolean("active", false);
                String active_trip = mSharedPreferences.getString("active_trip_id", BLANK);
                String id = Integer.toString(findViewById(R.id.view_trip_id).getId());
                String current_trip = id.substring(4);

                if (active == true && !current_trip.equals(active_trip)) {
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    Toast.makeText(ViewTripActivity.this, "Previous trip stopped. This trip started", Toast.LENGTH_LONG).show();
                    mEditor.putString("active_trip_id", current_trip);
                    mEditor.commit();
                } else if (active == false) {
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    Toast.makeText(ViewTripActivity.this, "Trip started. Lets go !", Toast.LENGTH_LONG).show();
                    mEditor.putString("active_trip_id", current_trip);
                    mEditor.putBoolean("active", true);
                    mEditor.commit();
                }
                start.setEnabled(false);

                GetLocation gps = new GetLocation(ViewTripActivity.this);
                if (!gps.locAllowed()) {
                    gps.alertBox();
                }
                Intent gps_intent = new Intent(getApplicationContext(), GPSChecker.class);
                PendingIntent pi = PendingIntent.getService(getApplicationContext(), 2222, gps_intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Calendar calendar = Calendar.getInstance();
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            }
        });
        reached.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean active = mSharedPreferences.getBoolean("active", false);
                String activetripid = mSharedPreferences.getString("active_trip_id", BLANK);
                String id = Integer.toString(findViewById(R.id.view_trip_id).getId());
                String presenttripid = id.substring(4);

                if (active == true && activetripid.equals(presenttripid)) {
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString("active_trip_id", BLANK);
                    e.putBoolean("active", false);
                    e.commit();
                    Toast.makeText(ViewTripActivity.this, "Destination is here. HURRAY !", Toast.LENGTH_LONG).show();
                    stopService(new Intent(getApplicationContext(), GPSChecker.class));
                    start.setEnabled(true);
                } else {
                    Toast.makeText(ViewTripActivity.this, "Sorry ! Not yet started", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Create a Trip object via the recent trip that
     * was passed to TripViewer via an Intent.
     *
     * @param i The Intent that contains
     *          the most recent trip data.
     * @return The Trip that was most recently
     * passed to TripViewer, or null if there
     * is none.
     */
    public Trip getTrip(Intent i) {

        // TODO - fill in here
        Trip trip = i.getParcelableExtra("trip");
        return trip;
    }

    /**
     * Populate the View using a Trip model.
     *
     * @param trip The Trip model used to
     *             populate the View.
     */
    public void viewTrip(Trip trip) {

        String tripid = trip.getTripID();
        String name = trip.getName();
        String destination = trip.getLocation();
        String date = trip.getDate();
        String time = trip.getTime();
        String transport = trip.getMeetingLocation();
        ArrayList<Person> group = trip.getGroup();
        //Log.i("viewtrip_activity: ",tripid+name+destination+date+time+transport);
        String friends = "";
        for (int i = 0; i < group.size(); i++) {
            if (i == group.size() - 1) {
                friends = friends + group.get(i).getName();
            } else {
                friends = friends + group.get(i).getName() + ", ";
            }
        }
        TextView trip_id_text = (TextView) findViewById(R.id.view_id_text);
        trip_id_text.setText(tripid);

        TextView trip_name_text = (TextView) findViewById(R.id.view_name_text);
        trip_name_text.setText(name);

        TextView trip_location_text = (TextView) findViewById(R.id.view_location_text);
        trip_location_text.setText(destination);

        TextView trip_date_text = (TextView) findViewById(R.id.view_date_text);
        trip_date_text.setText(date);

        TextView trip_time_text = (TextView) findViewById(R.id.view_time_text);
        trip_time_text.setText(time);

        TextView trip_transport_text = (TextView) findViewById(R.id.view_transport_text);
        trip_transport_text.setText(transport);

        TextView trip_friends_text = (TextView) findViewById(R.id.view_friends_text);
        trip_friends_text.setText(friends);

        start = (Button) findViewById(R.id.button_start_trip);
        reached = (Button) findViewById(R.id.button_reached_trip);
        start.setVisibility(View.VISIBLE);
        reached.setVisibility(View.VISIBLE);
        mSharedPreferences = getSharedPreferences("activeData", Context.MODE_PRIVATE);
        // TODO - fill in here
    }
}
