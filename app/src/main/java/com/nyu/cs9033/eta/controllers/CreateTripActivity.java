package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTripActivity extends Activity {

    static final int CONTACT = 1;
    static final int LOCATION = 2;
    private static final String TAG = "CreateTripActivity";
    static private Trip trip = new Trip();
    public int count = 0;
    HashSet<String> phone_no = new HashSet<String>();
    String tripId;
    ArrayList<String> array_group = new ArrayList<String>();
    private Button createTripButton;
    private ArrayList<Person> group = new ArrayList<Person>();

    public void senddatatoserver(Trip trips, ArrayList<String> friends) {

        String friend = "";
        for (int k = 0; k < group.size(); k++) {
            Person p = group.get(k);
            String friendName = p.getName();
            String friendPhone = p.getPhoneNumber();
            friend += friendName + ":::" + friendPhone + "%%";
            array_group.add(friendName);
        }
        String locname = trips.getLocation();
        String address = trips.getLocation();
        String latitude = trips.getLocation();
        String longitude = trips.getLocation();
        String[] array_friends = new String[friends.size()];
        array_friends = friends.toArray(array_friends);
        Log.i("friends: ", array_friends.toString());
        JSONObject post_dict = new JSONObject();
        String[] array_loc = new String[4];
        array_loc[0] = locname;
        array_loc[1] = address;
        array_loc[2] = latitude;
        array_loc[3] = longitude;
        long epoch_time = System.currentTimeMillis() / 1000L;
        try {
            post_dict.put("command", "CREATE_TRIP");
            post_dict.put("location", array_loc);
            post_dict.put("datetime", epoch_time);
            post_dict.put("people", array_friends);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            PostData pd = new PostData();
            pd.execute(String.valueOf(post_dict));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip);

        createTripButton = (Button) findViewById(R.id.button_trip_create);
        createTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip1 = createTrip();
                saveTrip(trip1);
            }
        });

        final Button cancelTrip = (Button) findViewById(R.id.button_trip_cancel);
        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTrip();
            }
        });

        final Button addGroup = (Button) findViewById(R.id.button_add_people);
        addGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pickContactFromApp();
            }
        });

        final Button searchLocation = (Button) findViewById(R.id.button_add_location);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final EditText tripLocation = (EditText) findViewById(R.id.input_location);
                String location = tripLocation.getText().toString();

                final EditText tripFood = (EditText) findViewById(R.id.input_food);
                String food = tripFood.getText().toString();

                if (location.equals("") || food.equals("")) {
                    Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG).show();
                } else if (count == 0) {
                    LocationSearch();
                    count = 1;
                } else if (count == 1) {
                    tripLocation.setText("");
                    tripLocation.setEnabled(true);

                    tripFood.setText("");
                    tripFood.setEnabled(true);
                }
            }
        });
    }

    private void pickContactFromApp() {
        Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(pickContact, CONTACT);
    }
    // TODO - fill in here

    private void LocationSearch() {
        Uri locationURI = Uri.parse("location://com.example.nyu.hw3api");
        Intent pickLocation = new Intent(Intent.ACTION_VIEW, locationURI);

        final EditText triplocation = (EditText) findViewById(R.id.input_location);
        String tripLocation = triplocation.getText().toString();
        triplocation.setEnabled(false);

        final EditText tripfood = (EditText) findViewById(R.id.input_food);
        String tripFood = tripfood.getText().toString();
        tripfood.setEnabled(false);

        String searchString = tripLocation + "::" + tripFood;
        pickLocation.putExtra("searchVal", searchString);
        startActivityForResult(pickLocation, LOCATION);
    }

    /**
     * This method should be used to
     * instantiate a Trip model object.
     *
     * @return The Trip as represented
     * by the View.
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((resultCode != Activity.RESULT_OK) && (resultCode != Activity.RESULT_FIRST_USER))
            return;
        if (requestCode == CONTACT) {
            Uri contactUri = data.getData();
            String[] queryField = new String[]{
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.DATA1};

            Cursor cursor = this.getContentResolver().query(contactUri, queryField, null, null, null);
            if (cursor.getCount() == 0) {
                cursor.close();
                return;
            }
            Person new_person = new Person();
            final TextView friendList = (TextView) findViewById(R.id.input_friends);
            cursor.moveToFirst();
            String person = cursor.getString(0);
            String number = cursor.getString(1);
            if (!phone_no.contains(number)) {
                phone_no.add(number);
                new_person.setName(person);
                new_person.setPhoneNumber(number);
                group.add(new_person);
                friendList.setText(friendList.getText() + person + ", ");
            } else {
                Toast.makeText(getBaseContext(), "You have already selected this person", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }
        if (requestCode == LOCATION) {
            ArrayList arrayList = data.getStringArrayListExtra("retVal");
            String locationNameString = arrayList.get(0).toString();
            String locationAddress = arrayList.get(1).toString();
            Toast.makeText(CreateTripActivity.this, "Location Added!", Toast.LENGTH_SHORT).show();
            TextView locationText = (TextView) findViewById(R.id.input_location);
            locationText.setText(locationNameString);
            trip.setLocation(locationNameString);
            trip.setMeetingLocation(locationAddress);

            final TextView getLocationTrip = (TextView) findViewById(R.id.input_location);
            getLocationTrip.setText(locationNameString);
        }
    }

    public Trip createTrip() {

        // TODO - fill in here
        String name_trip;
        EditText tripname = (EditText) findViewById(R.id.input_name);
        name_trip = tripname.getText().toString();
        trip.setName(name_trip);

        String destination_trip = null;
        EditText tripdestination = (EditText) findViewById(R.id.input_location);
        destination_trip = tripdestination.getText().toString();

        String date_trip = null;
        EditText tripdate = (EditText) findViewById(R.id.input_date);
        date_trip = tripdate.getText().toString();
        trip.setDate(date_trip);

        String time_trip = null;
        EditText triptime = (EditText) findViewById(R.id.triptime);
        time_trip = triptime.getText().toString();
        trip.setMeetingTime(time_trip);

        String food_trip = null;
        EditText tripfood = (EditText) findViewById(R.id.input_food);
        food_trip = tripfood.getText().toString();

        String transport_trip = null;
        EditText triptransport = (EditText) findViewById(R.id.input_transport);
        transport_trip = triptransport.getText().toString();
        trip.setModeTransport(transport_trip);
        trip.setGroup(group);

        return trip;
    }

    public boolean validate() {

        boolean valid = true;
        com.nyu.cs9033.eta.models.Trip trip = null;
        EditText tripname = (EditText) findViewById(R.id.input_name);
        String name = tripname.getText().toString();
        EditText locationname = (EditText) findViewById(R.id.input_location);
        String location = locationname.getText().toString();
        EditText tripdate = (EditText) findViewById(R.id.input_date);
        EditText triptime = (EditText) findViewById(R.id.triptime);
        String time = triptime.getText().toString();
        String date = tripdate.getText().toString();
        TextView trippeople = (TextView) findViewById(R.id.input_friends);
        String people = trippeople.getText().toString();
        EditText triptransport = (EditText) findViewById(R.id.input_transport);
        String transport = triptransport.getText().toString();
        String TIME24HOURS_PATTERN =
                "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
        matcher = pattern.matcher(time);

        if (name.length() < 4 || name.length() > 10) {
            tripname.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }

        if (matcher.matches() == false || time.length() != 5) {
            triptime.setError("Incorrect Time");
            valid = false;
        }

        if (time.length() != 5) {
            triptime.setError("Format HH:MM");
            valid = false;
        }

        if (date != null || date.length() != 10) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(date);
            } catch (Exception e) {
                valid = false;
                tripdate.setError("Incorrect date");
            }
        }
        if (transport.length() < 3 || name.length() > 15) {
            triptransport.setError("between 3 and 15 alphanumeric characters");
            valid = false;
        }


        if (location.length() < 3 || location.length() > 50) {
            locationname.setError("between 3 and 50 alphanumeric characters");
            valid = false;
        }

        if (name.isEmpty()) {
            tripname.setError("Enter Name");
            valid = false;
        }
        if (location.isEmpty()) {
            locationname.setError("Enter Location");
            valid = false;
        }

        if (date.isEmpty()) {
            tripdate.setError("Enter Date");
            valid = false;
        }

        if (transport.isEmpty()) {
            triptransport.setError("Enter Transport");
            valid = false;
        }

        if (time.isEmpty()) {
            triptime.setError("Enter Time");
            valid = false;
        }

        if (people.isEmpty()) {
            trippeople.setError("Add atleast 1 Name");
            valid = false;
        }
        return valid;
    }

    /**
     * For HW2 you should treat this method as a
     * way of sending the Trip data back to the
     * main Activity.
     * <p>
     * Note: If you call finish() here the Activity
     * will end and pass an Intent back to the
     * previous Activity using setResult().
     *
     * @return whether the Trip was successfully
     * saved.
     */

    public boolean saveTrip(Trip trip) {

        // TODO - fill in here

        boolean flag;
        if (!validate()) {
            onCreateFailed();
            flag = false;
        } else {
            senddatatoserver(trip, array_group);
            flag = true;
        }

        return flag;
    }

    /**
     * This method should be used when a
     * user wants to cancel the creation of
     * a Trip.
     * <p>
     * Note: You most likely want to call this
     * if your activity dies during the process
     * of a trip creation or if a cancel/back
     * button event occurs. Should return to
     * the previous activity without a result
     * using finish() and setResult().
     */
    public void cancelTrip() {

        // TODO - fill in here

        Intent cancel = new Intent();
        setResult(RESULT_CANCELED, cancel);
        finish();
    }

    public void onCreateFailed() {
        Toast.makeText(getBaseContext(), "Create Trip failed", Toast.LENGTH_LONG).show();
        createTripButton.setEnabled(true);
    }

    class PostData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            tripId = null;
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
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
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                JsonResponse = buffer.toString();
                JSONObject jsonObject = new JSONObject(JsonResponse);
                Integer response_code = jsonObject.getInt("response_code");
                Integer trip_Id = jsonObject.getInt("trip_id");
                tripId = trip_Id.toString();
                Log.i("RESPONSE_CODE", "" + response_code);
                Log.i("TRIP_ID", "" + tripId);
                trip.setTripID(tripId);
                return tripId;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String tripid) {

            trip.setTripID(tripid);
            Intent temp = new Intent();
            temp.putExtra("Data", trip);
            setResult(RESULT_OK, temp);
            TripDatabaseHelper tripDatabaseHelper = new TripDatabaseHelper(getBaseContext());
            long count = tripDatabaseHelper.insertTrips(trip);
            tripDatabaseHelper.insertLocation(trip, count);
            tripDatabaseHelper.insertGroup(trip, count);
            Log.d("Create Trip", trip.toString());
            Toast.makeText(getBaseContext(), "Yaay ! Trip Created", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
