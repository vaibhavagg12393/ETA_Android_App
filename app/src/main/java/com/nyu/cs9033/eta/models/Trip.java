package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Trip implements Parcelable {

	private String name;
	private String destination;
	private String date;
	private String meetingTime;
	private String meetingLocation;
	private String modeTransport;
	private ArrayList<Person> group;
	private double lat;
	private double longi;
	private double alt;
	private String locprovider;
	public static int counter =0;

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

	/**
	 * Create a Trip model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 *
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */

	public Trip(Parcel p) {

		// TODO - fill in here

		this.name = p.readString();
		this.destination = p.readString();
		this.date = p.readString();
		this.meetingTime = p.readString();
		this.meetingLocation = p.readString();
		this.modeTransport = p.readString();
		this.group = p.createTypedArrayList(Person.CREATOR);
		this.lat = p.readDouble();
		this.longi = p.readDouble();
		this.alt = p.readDouble();
		this.locprovider = p.readString();
		counter++;
	}

	/**
	 * Create a Trip model object from arguments
	 *
	 * @param name  Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 */
	public Trip(String name, String destination, String date,String meetingTime, String meetingLocation, String modeTransport,ArrayList<Person> group, double lat, double longi, double alt, String locprovider) {

		// TODO - fill in here, please note you must have more arguments here
		this.name = name;
		this.destination = destination;
		this.date = date;
		this.meetingTime = meetingTime;
		this.meetingLocation = meetingLocation;
		this.modeTransport = modeTransport;
		this.group = group;
		this.lat = lat;
		this.longi = longi;
		this.alt = alt;
		this.locprovider = locprovider;
	}

	public Trip() {

	}
	/**
	 * Serialize Trip object by using writeToParcel. 
	 * This function is automatically called by the
	 * system when the object is serialized.
	 *
	 * @param dest Parcel object that gets written on 
	 * serialization. Use functions to write out the
	 * object stored via your member variables. 
	 *
	 * @param flags Additional flags about how the object 
	 * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
	 * In our case, you should be just passing 0.
	 */

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		// TODO - fill in here
		dest.writeString(name);
		dest.writeString(destination);
		dest.writeString(date);
		dest.writeString(meetingTime);
		dest.writeString(meetingLocation);
		dest.writeString(modeTransport);
		dest.writeTypedList(group);
		dest.writeDouble(lat);
		dest.writeDouble(longi);
		dest.writeDouble(alt);
		dest.writeString(locprovider);
	}

	/**
	 * Feel free to add additional functions as necessary below.
	 */

	public void setName(String name)
	{
		this.name = name;
	}

	public void setLocation(String destination)
	{
		this.destination = destination;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public void setMeetingTime(String meetingTime)
	{
		this.meetingTime = meetingTime;
	}

	public void setMeetingLocation(String meetingLocation)
	{
		this.meetingLocation = meetingLocation;
	}

	public void setModeTransport(String modeTransport)
	{
		this.modeTransport = modeTransport;
	}

	public void setGroup(ArrayList<Person> group)
	{
		this.group = group;
	}

	public void setLatitude(double lat) { this.lat = lat; }

	public void setLongitude(double longi) { this.longi = lat; }

	public String getName()
	{
		return name;
	}

	public String getLocation()
	{
		return destination;
	}

	public String getDate()
	{
		return date;
	}

	public String getTime()
	{
		return meetingTime;
	}

	public String getMeetingLocation()
	{
		return meetingLocation;
	}

	public String getModeTransport()
	{
		return modeTransport;
	}

	public ArrayList<Person> getGroup()
	{
		return group;
	}

	public double getLatitude() { return lat; }

	public double getLongitude() { return longi; }

	public double getAlt() { return alt; }

	public String getLocprovider() { return locprovider; }

	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}
}
