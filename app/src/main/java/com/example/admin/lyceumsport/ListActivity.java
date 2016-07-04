package com.example.admin.lyceumsport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ListActivity extends Activity {
    String date;
    String str_date,id ;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ListAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras=getIntent().getExtras();
        id=extras.getString("id");

        date = getIntent().getStringExtra("date");
        Log.d("date", date + " Akhtar");
        try {
            str_date = getDate(date);
            Log.d("str_date",""+str_date);} catch (ParseException e) {
            e.printStackTrace();
        }
        new DownloadJSON().execute();
    }
    public String getDate(String string) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = originalFormat.parse(string);
        String formattedDate = targetFormat.format(date);
        return formattedDate;
    }
    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ListActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL("http://www.lyceumssports.com/androidlogin/schedulefetch.php?id=&date="+id+str_date);
            try {
                //JSONObject Jasonobject = new JSONObject("result");

                jsonarray = jsonobject.getJSONArray(str_date);

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("grade", jsonobject.getString("grade"));
                    map.put("section", jsonobject.getString("section"));
                    map.put("dateto", jsonobject.getString("dateto"));
                    map.put("datefrom", jsonobject.getString("datefrom"));
                    arraylist.add(map);
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListAdapter(ListActivity.this, arraylist);

            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
}
