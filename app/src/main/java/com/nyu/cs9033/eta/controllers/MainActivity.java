package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.database.TripHistoryActivity;
import com.nyu.cs9033.eta.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO - fill in here
	}

	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startCreateTripActivity(View view) {

		// TODO - fill in here
		Intent intent = new Intent(this, CreateTripActivity.class);
		startActivityForResult(intent, 1);
	}

	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startTripHistoryActivity(View view) {

		// TODO - fill in here
		Intent intent = new Intent(this, TripHistoryActivity.class);
		startActivity(intent);
	}

	/**
	 * Receive result from CreateTripActivity here.
	 * Can be used to save instance of Trip object
	 * which can be viewed in the ViewTripActivity.
	 *
	 * Note: This method will be called when a Trip
	 * object is returned to the main activity.
	 * Remember that the Trip will not be returned as
	 * a Trip object; it will be in the persisted
	 * Parcelable form. The actual Trip object should
	 * be created and saved in a variable for future
	 * use, i.e. to view the trip.
	 *
	 */
}
