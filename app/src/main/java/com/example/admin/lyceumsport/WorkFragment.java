package com.example.admin.lyceumsport;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by Ratan on 7/29/2015.
 */
@TargetApi(3)
public class WorkFragment extends Activity {
    private static final String tag = "WorkFragment";
    JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    String id;
    CalendarView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workchart);
        Bundle extras=getIntent().getExtras();
        id=extras.getString("id");

        CalendarView.count = 0;

        cv = ((CalendarView) findViewById(R.id.calendar_view));

        new DownloadJSON().execute();
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(WorkFragment.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(WorkFragment.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Fetching Schedule");
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
                    .getJSONfromURL("http://www.lyceumssports.com/androidlogin/datecount.php?id="+id);

            try {
                //JSONObject Jasonobject = new JSONObject("result");

                jsonarray = jsonobject.getJSONArray("result");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("day", jsonobject.getString("day"));
                    map.put("noofscedule", jsonobject.getString("noofscedule"));
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

            mProgressDialog.dismiss();
            ArrayList<String> array = new ArrayList<>();
            HashSet<Date> events = new HashSet<>();
            for(int i =0; i<arraylist.size();i++)
            {
                String date_string = arraylist.get(i).get("day");
                try {
                    events.add(getDatefRom(date_string));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                array.add(arraylist.get(i).get("noofscedule"));
                Log.d(date_string + "", "date");
                Log.d(arraylist.get(i).get("noofscedule"), "noofscedule");
            }
            cv.updateCalendar(events, array);
        }
    }
    public Date getDatefromString(String string)
    {

        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        return date;
    }

    public Date getDatefRom(String string) throws ParseException {
        SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try
        {
            date = form.parse(string);
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
        String newDateStr = postFormater.format(date);
        return postFormater.parse(newDateStr);
    }
}