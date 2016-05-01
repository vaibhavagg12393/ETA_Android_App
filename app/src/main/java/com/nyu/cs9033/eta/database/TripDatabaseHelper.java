package com.nyu.cs9033.eta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;

/**
 * Created by Vaibhav on 03-15-2016.
 */

public class TripDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eta_trips";

    public static final String TRIP_TABLE = "trips";
    public static final String TRIP_NAME = "trips_name";
    public static final String TRIP_NO = "_id";
    public static final String TRIP_DATE ="date";
    public static final String TRIP_TIME = "meettime";
    public static final String TRIP_DESTINATION = "trips_destination";
    public static final String TRIP_TRANSPORT = "modeoftrans";
    public static final String TRIP_MEETLOC = "meetlocation";

    public static final String PERSON_TABLE = "person";
    public static final String PERSON_NAME = "person_name";
    public static final String PERSON_TRIP_NO = "_id";
    public static final String PERSON_AGE ="age";
    public static final String PERSON_LATITUDE = "person_lat";
    public static final String PERSON_LONGITUDE = "person_long";
    public static final String PERSON_ALTITUDE = "person_alt";
    public static final String PERSON_HOME = "address";
    public static final String PERSON_PHONE_NO = "cellno";

    public static final String LOCATION_TABLE = "location";
    public static final String LOCATION_TRIP_NO = "_id";
    public static final String LOCATION_TIME = "timestamp";
    public static final String LOCATION_LATITUDE = "latitude";
    public static final String LOCATION_LONGITUDE = "longitude";
    public static final String LOCATION_ALTITUDE = "altitude";
    public static final String LOCATION_PROVIDER = "provider";


    public TripDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create the table to store trips
        sqLiteDatabase.execSQL("create table " + TRIP_TABLE + "("
                + TRIP_NO + " integer primary key autoincrement, "
                + TRIP_NAME + " varchar(50), "
                + TRIP_DATE + " date, "
                + TRIP_TIME + " time, "
                + TRIP_DESTINATION + " text,"
                + TRIP_TRANSPORT + " text, "
                + TRIP_MEETLOC + " text)");


        //Create table to store persons
        sqLiteDatabase.execSQL("create table " + PERSON_TABLE + "("
                + PERSON_TRIP_NO + " integer references trip(_id), "
                + PERSON_NAME + " varchar(100), "
                + PERSON_AGE + "integer"
                + PERSON_LATITUDE + " real, "
                + PERSON_LONGITUDE + " real, "
                + PERSON_ALTITUDE + " real, "
                + PERSON_HOME + " varchar(100), "
                + PERSON_PHONE_NO + " varchar(20))");


        //Create table to store locations
        sqLiteDatabase.execSQL("create table " + LOCATION_TABLE + "("
                + LOCATION_TRIP_NO + " integer references trip(_id), "
                + LOCATION_TIME + " varchar(20), "
                + LOCATION_LATITUDE + " real, "
                + LOCATION_LONGITUDE + " real, "
                + LOCATION_ALTITUDE + " real, "
                + LOCATION_PROVIDER + " varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop Previous Tables if any

        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);

        onCreate(db);

    }

    public long insertTrips(Trip trip)
    {
        ContentValues c = new ContentValues();
        c.put(TRIP_NAME, trip.getName());
        c.put(TRIP_DATE, trip.getDate());
        c.put(TRIP_TIME, trip.getTime());
        c.put(TRIP_DESTINATION, trip.getLocation());
        c.put(TRIP_TRANSPORT, trip.getModeTransport());
        c.put(TRIP_MEETLOC, trip.getMeetingLocation());

        return getWritableDatabase().insert(TRIP_TABLE,null,c);
    }

    public void insertLocation(Trip trip, long count){
        ContentValues c = new ContentValues();


        c.put(LOCATION_TRIP_NO, count);
        c.put(LOCATION_TIME, trip.getTime());
        c.put(LOCATION_LATITUDE, trip.getLatitude());
        c.put(LOCATION_LONGITUDE, trip.getLongitude());
        c.put(LOCATION_ALTITUDE, trip.getAlt());
        c.put(LOCATION_PROVIDER, trip.getLocprovider());

        getWritableDatabase().insert(LOCATION_TABLE, null, c);

    }

    public void insertGroup(Trip trip, long count){
        ContentValues c = new ContentValues();

        ArrayList<Person> group = trip.getGroup();

        for(int i = 0; i<group.size(); i++)
        {
            c.put(PERSON_TRIP_NO, count);
            c.put(PERSON_NAME, group.get(i).getName());
            c.put(PERSON_PHONE_NO, group.get(i).getPhoneNumber());
            getWritableDatabase().insert(PERSON_TABLE, null, c);
        }
    }


}
