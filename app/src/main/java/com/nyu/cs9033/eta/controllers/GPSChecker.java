package com.nyu.cs9033.eta.controllers;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPSChecker extends Service {

    public double double_latitude = 0.0, double_longitude = 0.0;
    public Handler h = null;
    GetLocation mGPS;
    public static Runnable mRunnable = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        h = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                mGPS = new GetLocation(GPSChecker.this);
                if (!mGPS.locAllowed()) {
                    Toast.makeText(getApplicationContext(), "GPS required.", Toast.LENGTH_LONG).show();

                } else {
                    double latitude = mGPS.getLatitude();
                    double longitude = mGPS.getLongitude();
                    if (latitude != double_latitude || longitude != double_longitude) {
                        double_latitude = latitude;
                        double_longitude = longitude;
                        Toast.makeText(getApplicationContext(), "Location updated", Toast.LENGTH_LONG).show();
                        postData(latitude, longitude);
                    } else {
                        Log.i("Else", "Error");
                    }
                }
                h.postDelayed(mRunnable, 10000);
            }
        };
        h.postDelayed(mRunnable, 0);
    }

    @Override
    public void onStart(Intent intent, int startid) {
    }

    @Override
    public void onDestroy() {
        h.removeCallbacks(mRunnable);
        Toast.makeText(this, "Stopped", Toast.LENGTH_LONG).show();
    }

    public void postData(double lati, double longi) {

        long epoch_time = System.currentTimeMillis() / 1000L;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "UPDATE_LOCATION");
            jsonObject.put("latitude", lati);
            jsonObject.put("longitude", longi);
            jsonObject.put("datetime", epoch_time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject.length() > 0) {
            new PostDataClass().execute(String.valueOf(jsonObject));
        }
    }

    class PostDataClass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String mResult = null;
            String mData = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;
            try {
                URL url = new URL("http://cs9033-homework.appspot.com");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(mData);
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = br.readLine()) != null)
                    buffer.append(inputLine + "\n");
                mResult = buffer.toString();
                return mResult;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (final IOException e) {
                        Log.e("ERROR:", "Error");
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
        }
    }
}
