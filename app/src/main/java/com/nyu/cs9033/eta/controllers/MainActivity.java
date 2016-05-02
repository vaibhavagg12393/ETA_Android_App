package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.database.TripHistoryActivity;
import com.nyu.cs9033.eta.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
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

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final String DEFAULT = "trip_id";
    JSONArray json_array_distance_left;
    JSONArray json_array_time_left;
    JSONArray people;
    String trip_id;
    LinearLayout linearLayout;
    TextView textView;
    Intent viewTripActivity;
    Intent createTripActivity;
    Boolean isActive;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences("activeData", Context.MODE_PRIVATE);
        trip_id = mSharedPreferences.getString("active_trip_id", DEFAULT);
        isActive = mSharedPreferences.getBoolean("active", false);
        linearLayout = (LinearLayout) findViewById(R.id.view_main_activity);
        textView = new TextView(MainActivity.this);
        if (isActive == true && !trip_id.equals("")) {

            postData(trip_id);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        //get reference to input elements and define there listeners here only
        Button createTrip = (Button) findViewById(R.id.button_create_trip);
        createTripActivity = new Intent(this, CreateTripActivity.class);
        createTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startCreateTripActivity();
                // Perform action on click
            }
        });


        viewTripActivity = new Intent(this, TripHistoryActivity.class);
        Button viewTrip = (Button) findViewById(R.id.button_view_trip);
        viewTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startViewTripActivity();
                // Perform action on click
            }
        });
        mSharedPreferences = getSharedPreferences("activeData", Context.MODE_PRIVATE);
        trip_id = mSharedPreferences.getString("active_trip_id", DEFAULT);
        isActive = mSharedPreferences.getBoolean("active", false);
        linearLayout = (LinearLayout) findViewById(R.id.view_main_activity);
        textView = new TextView(MainActivity.this);
        if (isActive == true) {

            postData(trip_id);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        // TODO - fill in here
    }

    /**
     * This method should start the
     * Activity responsible for creating
     * a Trip.
     */
    public void startCreateTripActivity() {
        startActivity(createTripActivity);
        // TODO - fill in here
    }

    /**
     * This method should start the
     * Activity responsible for viewing
     * a Trip.
     */

    public void startViewTripActivity() {

        startActivity(viewTripActivity);


        // TODO - fill in here
    }

    public void startTripHistoryActivity(View view) {

        // TODO - fill in here
        Intent intent = new Intent(this, TripHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSharedPreferences = getSharedPreferences("activeData", Context.MODE_PRIVATE);
        trip_id = mSharedPreferences.getString("active_trip_id", DEFAULT);
        isActive = mSharedPreferences.getBoolean("active", false);
        linearLayout = (LinearLayout) findViewById(R.id.view_main_activity);
        textView = new TextView(MainActivity.this);
        if (isActive == true) {

            postData(trip_id);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    /* POST request to WEB SERVER */
    public void postData(String trip_id) {
        //function in the activity that corresponds to the layout button

        JSONObject post_dict = new JSONObject();


        try {
            post_dict.put("command", "TRIP_STATUS");
            post_dict.put("trip_id", Integer.parseInt(trip_id));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            new PostDataClass().execute(String.valueOf(post_dict));
            //call to async class
        }


    }

    class PostDataClass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
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
                writer.write(JsonDATA);
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                br = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while ((inputLine = br.readLine()) != null)
                    buffer.append(inputLine + "\n");
                JsonResponse = buffer.toString();
                Log.e("TRIP STATUS DATA", JsonResponse);
                JSONObject jsonObject = new JSONObject(JsonResponse);
                json_array_distance_left = jsonObject.getJSONArray("distance_left");
                json_array_time_left = jsonObject.getJSONArray("time_left");
                people = jsonObject.getJSONArray("people");
                return JsonResponse;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            textView = new TextView(MainActivity.this);
            linearLayout = (LinearLayout) findViewById(R.id.view_main_activity);
            if (((LinearLayout) linearLayout).getChildCount() > 0)
                ((LinearLayout) linearLayout).removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 4, 0, 4);
            textView.setLayoutParams(params);
            String main_activity_text = "";
            try {
                if (json_array_distance_left != null) {
                    for (int i = 0; i < json_array_distance_left.length(); i++) {
                        main_activity_text += people.getString(i) + "-->" + json_array_distance_left.getString(i) + " miles, " + Integer.parseInt(json_array_time_left.getString(i)) / 60 + " secs away<br/>";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textView.setText(Html.fromHtml(main_activity_text));
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setPadding(18, 18, 18, 18);
            linearLayout.addView(textView);
            linearLayout.setVisibility(View.VISIBLE);
        }

    }
}
