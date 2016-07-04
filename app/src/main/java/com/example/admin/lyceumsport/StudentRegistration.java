package com.example.admin.lyceumsport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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


public class StudentRegistration extends Activity implements View.OnClickListener {

    EditText name,birth,parent,mobile,altmobile,email;


    String Name,Birth,Medical,Parent,Mobile,Altmobile,Email,Session,Paystatus,Gender,id;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageButton image1;

    int pos;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner medical,session,payStatus,gender;
    Button submit;
    String toa1,loc1,addtype1,gen1;
    String med[] = {"---Select---",  "Fit", "Unfit"};
    String sessperwk[] = {"---Select---", "1", "2", "3","4","5","6", "7", "8","9","10","11", "12"};
    String pay[] = {"---Select---",  "Paid", "Unpaid"};
    String gen2[] = {"---Select---",  "Male", "Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentregistration);

        Bundle extras=getIntent().getExtras();
        id=extras.getString("id");


        image1=(ImageButton) findViewById(R.id.imageButton1);
        name = (EditText) findViewById(R.id.editTexts);
        birth = (EditText) findViewById(R.id.editText0);
        parent = (EditText) findViewById(R.id.editText2);
        mobile = (EditText) findViewById(R.id.editText3);
        altmobile = (EditText) findViewById(R.id.editText4);
        email = (EditText) findViewById(R.id.editText5);
        submit =(Button) findViewById(R.id.button);
        medical=(Spinner)findViewById(R.id.spinner);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner,med);
        medical.setAdapter(adapter);
        session=(Spinner)findViewById(R.id.spinner2);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner,sessperwk);
        session.setAdapter(adapter);
        payStatus=(Spinner)findViewById(R.id.spinner3);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner,pay);
        payStatus.setAdapter(adapter);
        gender=(Spinner)findViewById(R.id.spinner0);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner,gen2);
        gender.setAdapter(adapter);
        medical.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                toa1 = item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                loc1=item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        payStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                addtype1 = item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                gen1 = item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();

      //  new StudentReg2(this).execute();

    }
    private void findViewsById() {
      EditText  fromDateEtxt = (EditText) findViewById(R.id.editText0);
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
                birth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if(view == image1) {
            fromDatePickerDialog.show();
        }
    }

    public void signup(View v) {

       if (name.getText().toString().matches("") || gender.getSelectedItem().toString().matches("") || birth.getText().toString().matches("") || medical.getSelectedItem().toString().matches("") || parent.getText().toString().matches("") ||
                mobile.getText().toString().matches("") || altmobile.getText().toString().matches("") || email.getText().toString().matches("")
                || session.getSelectedItem().toString().matches("") ) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid details",
                    Toast.LENGTH_LONG).show();
        } else if (mobile.getText().toString().length() < 10) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid MobileNumber",
                    Toast.LENGTH_LONG).show();
        }

        Name = name.getText().toString();
        Gender = gender.getSelectedItem().toString();
        Birth = birth.getText().toString();
        Medical = medical.getSelectedItem().toString();
        Parent = parent.getText().toString();
        Mobile = mobile.getText().toString();
        Altmobile = altmobile.getText().toString();
        Email = email.getText().toString();
        Session = session.getSelectedItem().toString();
        Paystatus = payStatus.getSelectedItem().toString();
        Toast.makeText(this, "Data Submitting...", Toast.LENGTH_SHORT).show();
        new StudentReg2(this).execute(Name,Gender,Birth,Medical,Parent,Mobile,Altmobile,Email,Session,Paystatus);
        Intent myIntent = new Intent(StudentRegistration.this, DevelopmentFragment.class);
        startActivity(myIntent);
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
            String Name = arg0[0];
            String Gender = arg0[1];
            String Birth = arg0[2];
            String Medical = arg0[3];
            String Parent = arg0[4];
            String Mobile = arg0[5];
            String Altmobile = arg0[6];
            String Email = arg0[7];
            String Session = arg0[8];
            String Paystatus = arg0[9];


            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?name=" + URLEncoder.encode(Name, "UTF-8");
                data += "&gender=" + URLEncoder.encode(Gender, "UTF-8");
                data += "&birth=" + URLEncoder.encode(Birth, "UTF-8");
                data += "&medical=" + URLEncoder.encode(Medical, "UTF-8");
                data += "&parent=" + URLEncoder.encode(Parent, "UTF-8");
                data += "&mobile=" + URLEncoder.encode(Mobile, "UTF-8");
                data += "&altmobile=" + URLEncoder.encode(Altmobile, "UTF-8");
                data += "&email=" + URLEncoder.encode(Email, "UTF-8");
                data += "&session=" + URLEncoder.encode(Session, "UTF-8");
                data += "&paystatus=" + URLEncoder.encode(Paystatus, "UTF-8");


                link = "http://www.lyceumssports.com/androidlogin/sturegister.php?id="+id+data ;

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
