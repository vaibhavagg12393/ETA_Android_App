package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Trip implements Parcelable {

    /**
     * Parcelable creator. Do not modify this function.
     */

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel p) {
            return new Trip(p);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
    public static int counter = 0;
    private String name;
    private String destination;
    private String date;
    private String meetingTime;
    private String meetingLocation;
    private String modeTransport;
    private ArrayList<Person> group;
    private double alt;
    private String locprovider;
    private String trips_id;

    /**
     * Create a Trip model object from a Parcel. This
     * function is called via the Parcelable creator.
     *
     * @param p The Parcel used to populate the
     *          Model fields.
     */

    public Trip(Parcel p) {

        // TODO - fill in here

        this.trips_id = p.readString();
        this.name = p.readString();
        this.destination = p.readString();
        this.date = p.readString();
        this.meetingTime = p.readString();
        this.meetingLocation = p.readString();
        this.modeTransport = p.readString();
        this.group = p.createTypedArrayList(Person.CREATOR);
        this.alt = p.readDouble();
        this.locprovider = p.readString();
        counter++;
    }

    /**
     * Create a Trip model object from arguments
     *
     * @param name Add arbitrary number of arguments to
     *             instantiate Trip class based on member variables.
     */
    public Trip(String trips_id, String name, String destination, String date, String modeTransport, String meetingTime, ArrayList<Person> group) {

        // TODO - fill in here, please note you must have more arguments here
        this.trips_id = trips_id;
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.meetingTime = meetingTime;
        this.modeTransport = modeTransport;
        this.group = group;
    }

    public Trip() {

    }

    /**
     * Serialize Trip object by using writeToParcel.
     * This function is automatically called by the
     * system when the object is serialized.
     *
     * @param dest  Parcel object that gets written on
     *              serialization. Use functions to write out the
     *              object stored via your member variables.
     * @param flags Additional flags about how the object
     *              should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     *              In our case, you should be just passing 0.
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // TODO - fill in here
        dest.writeString(trips_id);
        dest.writeString(name);
        dest.writeString(destination);
        dest.writeString(date);
        dest.writeString(meetingTime);
        dest.writeString(meetingLocation);
        dest.writeString(modeTransport);
        dest.writeTypedList(group);
        dest.writeDouble(alt);
        dest.writeString(locprovider);
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getName() {
        return name;
    }

    /**
     * Feel free to add additional functions as necessary below.
     */

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return destination;
    }

    public void setLocation(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return meetingTime;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getModeTransport() {
        return modeTransport;
    }

    public void setModeTransport(String modeTransport) {
        this.modeTransport = modeTransport;
    }

    public ArrayList<Person> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<Person> group) {
        this.group = group;
    }

    public double getAlt() {
        return alt;
    }

    public String getLocprovider() {
        return locprovider;
    }

    public String getTripID() {
        return trips_id;
    }

    public void setTripID(String trips_id) {
        this.trips_id = trips_id;
    }

    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
