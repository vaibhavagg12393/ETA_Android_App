package com.nyu.cs9033.eta.controllers;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GetLocation extends Service implements LocationListener {

    private final Context context;
    Location location;
    boolean mNetworkEnable = false;
    boolean boolean_get_loc = false;
    protected LocationManager locationManager;
    double latitude;
    double longitude;
    boolean mGPSEnable = false;
    private static final long DISTANCE_INTERVAL = 10;
    private static final long TIME_INTERVAL = 1000 * 60 * 1;

    public void alertBox(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS not enabled, Proceed to Settings ?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            mGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            mNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!mGPSEnable && !mNetworkEnable) {
            }else {
                this.boolean_get_loc = true;
                if(mNetworkEnable) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, TIME_INTERVAL, DISTANCE_INTERVAL, this);
                    if(locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(mGPSEnable){
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_INTERVAL, DISTANCE_INTERVAL, this);
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }
    @Override
    public void onLocationChanged(Location location) {
    }
    public GetLocation(Context context){
        this.context = context;
        getLocation();
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    public double getLatitude(){
        if (location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public boolean locAllowed(){
        return this.boolean_get_loc;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
