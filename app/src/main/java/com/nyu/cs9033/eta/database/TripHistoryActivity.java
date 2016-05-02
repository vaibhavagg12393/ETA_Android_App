package com.nyu.cs9033.eta.database;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.controllers.ViewTripActivity;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;

/**
 * Created by Vaibhav on 03-15-2016.
 */
public class TripHistoryActivity extends Activity {

    String trip_name;
    private TripDatabaseHelper pullTrips;
    private Trip trip = new Trip();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        displayTrip();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_trip_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionsettings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayTrip() {

        TripDatabaseHelper tripDatabaseHelper = new TripDatabaseHelper(getBaseContext());
        SQLiteDatabase liteDatabase = tripDatabaseHelper.getReadableDatabase();
        Cursor cursor = liteDatabase.rawQuery("select * from " + TripDatabaseHelper.TRIP_TABLE, null);

        String[] columns = new String[]{
                TripDatabaseHelper.TRIP_NO,
                TripDatabaseHelper.TRIP_NAME,
                TripDatabaseHelper.TRIP_DESTINATION
        };
        int[] list_details = new int[]{
                R.id.list_trip_id,
                R.id.list_trip_name,
                R.id.list_trip_location
        };
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_trip_list, cursor, columns, list_details, 0);
        final GridView gridview = (GridView) findViewById(R.id.grid_view);
        gridview.setAdapter(simpleCursorAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) gridview.getItemAtPosition(position);

                trip_name = cursor.getString(cursor.getColumnIndexOrThrow(TripDatabaseHelper.TRIP_NO));
                TripDatabaseHelper db1 = new TripDatabaseHelper(getBaseContext());
                SQLiteDatabase db = db1.getReadableDatabase();
                Cursor cursor2 = db.rawQuery("select * from " + TripDatabaseHelper.TRIP_TABLE + " where " + TripDatabaseHelper.TRIP_NO +
                        " = " + trip_name, null);
                for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {

                    //Log.i("trip_history_activity", cursor2.getString(0)+cursor2.getString(1)+cursor2.getString(2)+cursor2.getString(3)+cursor2.getString(4)+cursor2.getString(5)+cursor2.getString(6)+cursor2.getString(7));
                    trip.setTripID(cursor2.getString(1));
                    trip.setName(cursor2.getString(2));
                    trip.setDate(cursor2.getString(3));
                    trip.setMeetingTime(cursor2.getString(4));
                    trip.setLocation(cursor2.getString(5));
                    trip.setMeetingLocation(cursor2.getString(6));
                }
                Cursor cursor1 = db.rawQuery("select * from " + TripDatabaseHelper.PERSON_TABLE
                        + " where " + TripDatabaseHelper.PERSON_TRIP_NO + " = " + trip_name, null);
                ArrayList<Person> friends = new ArrayList<Person>();
                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                    Person p = new Person();
                    p.setName(cursor1.getString(1));
                    p.setPhoneNumber(cursor1.getString(6));
                    friends.add(p);
                }
                trip.setGroup(friends);
                Intent intent = new Intent(getBaseContext(), ViewTripActivity.class);
                intent.putExtra("trip", trip);
                intent.putExtra("tripID", trip_name);
                startActivity(intent);
            }
        });
    }
}
