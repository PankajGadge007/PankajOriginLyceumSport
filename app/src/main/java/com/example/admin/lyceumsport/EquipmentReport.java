package com.example.admin.lyceumsport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by admin on 4/27/2016.
 */
public class EquipmentReport extends Activity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    String School, Date, Equipment,id;
    JSONObject jsonobject;
    JSONArray jsonarray;
    private JSONArray result;
    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<String> listItems = new ArrayList<>();
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageButton image1;

    MultiSelectionSpinner multiSelectionSpinner;

    EditText sessiondate;
    private ArrayList<String> school;
    private ArrayList<String> equipment;

    Spinner school1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eqipment);

        Bundle extras=getIntent().getExtras();
        id=extras.getString("id");

        school = new ArrayList<String>();
        equipment = new ArrayList<String>();
		multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.spinner4);

        //multiSelectionSpinner.setSelection(new int[]{2, 6});
        multiSelectionSpinner.setListener(this);

        //  batch=(Spinner) findViewById(R.id.spinner1);
        //   new DownloadJSON().execute();
        sessiondate = (EditText) findViewById(R.id.editText0);
        image1 = (ImageButton) findViewById(R.id.imageButton1);
        school1  = (Spinner) findViewById(R.id.spinner2);
        


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
        getSchool();
        getEquipment();




    }

    private void findViewsById() {
        EditText fromDateEtxt = (EditText) findViewById(R.id.editText0);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

    }

    private void setDateTimeField() {

        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (view == image1) {
                    fromDatePickerDialog.show();
                }
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                sessiondate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

  /*  @Override
    public void onClick(View view) {
        if (view == image1) {
            fromDatePickerDialog.show();
        }
    }*/

    private void getSchool() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_SCHOOL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getSchool(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSchool(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                school.add(json.getString(Config.TAG_SCHOOL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        school1.setAdapter(new ArrayAdapter<String>(EquipmentReport.this, android.R.layout.simple_spinner_dropdown_item, school));
    }

    private void getEquipment() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_EQUIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getEquipment(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getEquipment(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                equipment.add(json.getString(Config.TAG_EQUIP));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        multiSelectionSpinner.setItems(equipment);
        //Setting adapter to show the items in the spinner
       // multiSelectionSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, equipment));
    }
    public void signup(View v) {

        if (sessiondate.getText().toString().matches("") || school1.getSelectedItem().toString().matches("") || multiSelectionSpinner.getSelectedItem().toString().matches("") ) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid details",
                    Toast.LENGTH_LONG).show();
        } else if (sessiondate.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid MobileNumber",
                    Toast.LENGTH_LONG).show();
        }

        Date = sessiondate.getText().toString();
        School = school1.getSelectedItem().toString();
        Equipment = multiSelectionSpinner.getSelectedItem().toString();

        Toast.makeText(this, "Data Submitting...", Toast.LENGTH_SHORT).show();
        new StudentReg2(this).execute(Date,School,Equipment);
        Intent myIntent = new Intent(EquipmentReport.this, EducationalFragment.class);
        startActivity(myIntent);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

    // Download JSON file AsyncTask
    public class StudentReg2 extends AsyncTask<String, Void, String> {

        private Context context;

        public StudentReg2(Context context)
        {
            this.context = context;
        }
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String Date = arg0[0];
            String School = arg0[1];
            String Equipment = arg0[2];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?dates=" + URLEncoder.encode(Date, "UTF-8");
                data += "&school=" + URLEncoder.encode(School, "UTF-8");
                data += "&eqname=" + URLEncoder.encode(Equipment, "UTF-8");



                link = "http://www.lyceumssports.com/androidlogin/equipmentreport.php?id="+ id + data;

                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String jsonStr = result;
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    if (query_result.equals("SUCCESS")) {
                        Toast.makeText(context, "Data inserted successfully.", Toast.LENGTH_SHORT).show();
                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(context, "Data could not be inserted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Data Submitted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
