package com.example.admin.lyceumsport;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentAttendance extends Activity {

    String Batch,Date;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<String> listItems=new ArrayList<>();
    DatePicker date;

    Spinner batch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentattendance);
       // date = (DatePicker) findViewById(R.id.date);
      //  batch=(Spinner) findViewById(R.id.spinner1);
        new DownloadJSON().execute();

    }

    public void signup(View v) {


       // Date = date.getCalendarView().toString();
        Batch = batch.getSelectedItem().toString();

        Toast.makeText(this, "Data Submitting...", Toast.LENGTH_SHORT).show();
       // new StudentAtt2(this).execute(Date,Batch);
    }
    // Download JSON file AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            arraylist = new ArrayList<HashMap<String, String>>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL("http://www.lyceumssports.com/androidlogin/batch.php");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("result");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    map.put("batch", jsonobject.getString("batch"));
                    // Populate spinner with country names
                    listItems.add(jsonobject.optString("batch"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            // Spinner adapter
            batch=(Spinner)findViewById(R.id.spinner1);
            batch.setAdapter(new ArrayAdapter<String>(StudentAttendance.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    listItems));
        }
    }



}