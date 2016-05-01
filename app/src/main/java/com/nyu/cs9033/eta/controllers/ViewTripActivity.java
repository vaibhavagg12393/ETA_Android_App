package com.nyu.cs9033.eta.controllers;


import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewTripActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_trip);
		// TODO - fill in here

		Trip trip = getTrip(getIntent());
		viewTrip(trip);
	}

	/**
	 * Create a Trip object via the recent trip that
	 * was passed to TripViewer via an Intent.
	 *
	 * @param i The Intent that contains
	 * the most recent trip data.
	 *
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
	 * populate the View.
	 */
	public void viewTrip(Trip trip) {

		String name = trip.getName();
		String destination = trip.getLocation();
		String date = trip.getDate();
		String time = trip.getTime();
		String meeting = trip.getMeetingLocation();
		ArrayList<Person> group = trip.getGroup();


		String friends = "";

		for(int i=0; i<group.size(); i++)
		{
			if(i == group.size() - 1)
			{
				friends = friends+group.get(i).getName();
			}
			else
			{
				friends = friends+group.get(i).getName() + ", ";
			}
		}

		TextView trip_name_text = (TextView)findViewById(R.id.view_name_text);
		trip_name_text.setText(name);

		TextView trip_location_text = (TextView)findViewById(R.id.view_location_text);
		trip_location_text.setText(destination);

		TextView trip_date_text = (TextView)findViewById(R.id.view_date_text);
		trip_date_text.setText(date);

		TextView trip_time_text = (TextView)findViewById(R.id.view_time_text);
		trip_time_text.setText(time);

		TextView trip_meeting_text = (TextView)findViewById(R.id.view_transport_text);
		trip_meeting_text.setText(meeting);

		TextView trip_friends_text = (TextView)findViewById(R.id.view_friends_text);
		trip_friends_text.setText(friends);

		// TODO - fill in here
	}
}
