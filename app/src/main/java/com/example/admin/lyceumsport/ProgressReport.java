package com.example.admin.lyceumsport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ProgressReport extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageButton image1;
    EditText sessiondate, module, atten, comm;
    ArrayAdapter<String> adapter;
    //Declaring an Spinner
    private Spinner spinnerSchoolName, Program1, Batch1, Session1, Act1, Acttyp1, spinnEquip;

    //An ArrayList for Spinner Items
    private ArrayList<String> school;
    private ArrayList<String> programme;
    private ArrayList<String> batch;
    private ArrayList<String> session;
    private ArrayList<String> equipment;
    private ArrayAdapter<String> adapterEquip;
    String SessionDat, SchoolName, Program, Batch, Module, Atten, Session, Activity, Activitytyp, Equp, Comm, id;
    String toa1, loc1;
    String acttype[] = {"---Select---", "Indoor", "Outdoor"};
    String act[] = {"---Select---", "1", "2", "3", "4", "5"};


    //JSON Array
    private JSONArray result;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");

        image1 = (ImageButton) findViewById(R.id.imageButton1);
        school = new ArrayList<String>();
        programme = new ArrayList<String>();
        batch = new ArrayList<String>();
        session = new ArrayList<String>();
        equipment = new ArrayList<String>();

        sessiondate = (EditText) findViewById(R.id.edtDate);
        module = (EditText) findViewById(R.id.editTextMod);
        atten = (EditText) findViewById(R.id.editTextAttendance);
        comm = (EditText) findViewById(R.id.editTextComment);
        //Initializing Spinner
        spinnerSchoolName = (Spinner) findViewById(R.id.spinnerSchool);
        Program1 = (Spinner) findViewById(R.id.spinnerProgram);
        Batch1 = (Spinner) findViewById(R.id.spinnerBatch);
        Session1 = (Spinner) findViewById(R.id.spinnerSession);
        spinnEquip = (Spinner) findViewById(R.id.spinnerEquipment);
        Act1 = (Spinner) findViewById(R.id.spinnerActivity);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner, act);
        Act1.setAdapter(adapter);
        Acttyp1 = (Spinner) findViewById(R.id.spinnerActiType);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner, acttype);
        Acttyp1.setAdapter(adapter);
        Act1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                toa1 = item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        Acttyp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                loc1 = item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        //This method will fetch the data from the URL
        getProgrammme();
        getBatch();
        getSession();
//        getEquipment();

        spinnerSchoolName.setOnItemSelectedListener(null);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();
        setDateTimeField();
    }

    private void findViewsById() {
        EditText fromDateEtxt = (EditText) findViewById(R.id.edtDate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

    }

    private void setDateTimeField() {
        image1.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                sessiondate.setText(dateFormatter.format(newDate.getTime()));
                if (!(school.size() > 0)) {
                    getSchool();
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if (view == image1) {
            fromDatePickerDialog.show();
        }
    }

    private void getProgrammme() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_PROGRAM,
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
                            getProgramme(result);
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

    private void getProgramme(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                programme.add(json.getString(Config.TAG_PROGRAM));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        Program1.setAdapter(new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, programme));
    }

    private void getBatch() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_BATCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("_PR_Response=", response);
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getBatch(result);
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

    private void getBatch(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                batch.add(json.getString(Config.TAG_BATCH));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        Batch1.setAdapter(new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, batch));
    }

    private void getSession() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_SESSION,
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
                            getSession(result);
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

    private void getSession(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                session.add(json.getString(Config.TAG_SESSION));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        Session1.setAdapter(new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, session));
    }

    private void getEquipment(final String id, final String date, final String schoolname) {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_FET_EQUIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("Parameters", "id=" + id + " date=" + date + " school=" + schoolname);
                        Log.w("Response", response);
                        JSONObject j;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            getEquipment(result);
                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("date", date);
                params.put("school", schoolname);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ProgressReport.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getEquipment(JSONArray j) {
        //Traversing through all the items in the json array
        if (equipment.size() > 0) {
            equipment.clear();
        }
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                if (json.has("msg")) {
                    Toast.makeText(ProgressReport.this, "No Equipment", Toast.LENGTH_LONG).show();
                }
                //Adding the name of the student to array list
                equipment.add(json.getString(Config.TAG_EQUIP));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        spinnEquip.setAdapter(new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, equipment));
        //Setting adapter to show the items in the spinner
    }

    public void signup(View v) {

        if (sessiondate.getText().toString().matches("") || spinnerSchoolName.getSelectedItem().toString().matches("") || Program1.getSelectedItem().toString().matches("") || Batch1.getSelectedItem().toString().matches("") || module.getText().toString().matches("") ||
                atten.getText().toString().matches("") || Session1.getSelectedItem().toString().matches("") || Act1.getSelectedItem().toString().matches("")
                || Acttyp1.getSelectedItem().toString().matches("") || spinnEquip.getSelectedItem().toString().matches("") || comm.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid details",
                    Toast.LENGTH_LONG).show();
        } else if (sessiondate.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid MobileNumber",
                    Toast.LENGTH_LONG).show();
        }

        SessionDat = sessiondate.getText().toString();
        SchoolName = spinnerSchoolName.getSelectedItem().toString();
        Program = Program1.getSelectedItem().toString();
        Batch = Batch1.getSelectedItem().toString();
        Module = module.getText().toString();
        Atten = atten.getText().toString();
        Session = Session1.getSelectedItem().toString();
        Activity = Act1.getSelectedItem().toString();
        Activitytyp = Acttyp1.getSelectedItem().toString();
        Equp = spinnEquip.getSelectedItem().toString();
        Comm = comm.getText().toString();
        Toast.makeText(this, "Data Submitting...", Toast.LENGTH_SHORT).show();
        new StudentReg2(this).execute(SessionDat, SchoolName, Program, Batch, Module, Atten, Session, Activity, Activitytyp, Equp, Comm);
        super.onBackPressed();
//        Intent myIntent = new Intent(ProgressReport.this, EducationalFragment.class);
//        startActivity(myIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("PANKAJ_SPINNER", "itemSelected: " + position);
        String coachId = "1";
        String sessionDate = sessiondate.getText().toString();
        String schoolName = school.get(position);
        getEquipment(coachId, sessionDate, schoolName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
        RequestQueue requestQueue = Volley.newRequestQueue(ProgressReport.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSchool(JSONArray j) {
        //Calling method getStudents to get the students from the JSON Array
        if (school.size() > 0) {
            school.clear();
        }
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
        spinnerSchoolName.setAdapter(new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, school));
        spinnerSchoolName.setSelection(0, false);
        spinnerSchoolName.setOnItemSelectedListener(this);
    }

    // Download JSON file AsyncTask
    public class StudentReg2 extends AsyncTask<String, Void, String> {

        private Context context;

        public StudentReg2(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String SessionDat = arg0[0];
            String School = arg0[1];
            String Program = arg0[2];
            String Batch = arg0[3];
            String Module = arg0[4];
            String Atten = arg0[5];
            String Session = arg0[6];
            String Activity = arg0[7];
            String Activitytyp = arg0[8];
            String Equp = arg0[9];
            String Comm = arg0[10];


            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?sessiondate=" + URLEncoder.encode(SessionDat, "UTF-8");
                data += "&school=" + URLEncoder.encode(School, "UTF-8");
                data += "&program=" + URLEncoder.encode(Program, "UTF-8");
                data += "&batch=" + URLEncoder.encode(Batch, "UTF-8");
                data += "&module=" + URLEncoder.encode(Module, "UTF-8");
                data += "&atten=" + URLEncoder.encode(Atten, "UTF-8");
                data += "&session=" + URLEncoder.encode(Session, "UTF-8");
                data += "&act=" + URLEncoder.encode(Activity, "UTF-8");
                data += "&acttype=" + URLEncoder.encode(Activitytyp, "UTF-8");
                data += "&equp=" + URLEncoder.encode(Equp, "UTF-8");
                data += "&comm=" + URLEncoder.encode(Comm, "UTF-8");


                link = "http://www.lyceumssports.com/androidlogin/progressreport.php?id=" + id + data;

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