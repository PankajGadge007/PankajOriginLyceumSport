package com.example.admin.lyceumsport;

/**
 * Created by admin on 6/7/2016.
 */
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by admin on 5/10/2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://www.lyceumssports.com/androidlogin/register.php";
    String email,password,name,mobile,address,location,dob,experience,qualification,designation;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    ImageButton image1;
    private static String KEY_NAME = "name";
    private static String KEY_MOBILE = "mobile";
    private static String KEY_EMAIL = "email";
    private static String KEY_PASSWORD = "password";
    private static String KEY_ADDRESS = "address";
    private static String KEY_LOCATION = "location";
    private static String KEY_DOB = "dob";
    private static String KEY_EXP = "experience";
    private static String KEY_QUAL = "qualification";
    private static String KEY_DES = "designation";


    EditText inputName;
    EditText inputMobile;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputRtyPassword;
    EditText inputAddress;
    EditText inputLocation;
    EditText inputDob;
    EditText inputExp;
    EditText inputQual;
    EditText inputDes;

    private Button buttonRegister,Btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        image1=(ImageButton) findViewById(R.id.imageButton1);
        inputName = (EditText) findViewById(R.id.name);
        inputMobile = (EditText) findViewById(R.id.mobile);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputRtyPassword = (EditText) findViewById(R.id.rtypassword);
        inputAddress = (EditText) findViewById(R.id.address);
        inputLocation = (EditText) findViewById(R.id.location);
        inputDob = (EditText) findViewById(R.id.dob);
        inputExp = (EditText) findViewById(R.id.experience);
        inputQual = (EditText) findViewById(R.id.qualification);
        inputDes = (EditText) findViewById(R.id.designation);
        buttonRegister = (Button) findViewById(R.id.btnRegister);
        Btnregister = (Button) findViewById(R.id.btnLinkToLoginScreen);
        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }});
        buttonRegister.setOnClickListener(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
    }
    private void findViewsById() {
        //EditText  fromDateEtxt = (EditText) findViewById(R.id.editText0);
        inputDob.setInputType(InputType.TYPE_NULL);
        inputDob.requestFocus();

    }

    private void setDateTimeField() {
        image1.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                inputDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    private void registerUser(){
        name = inputName.getText().toString().trim();
        mobile = inputMobile.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        address= inputAddress.getText().toString().trim();
        location= inputLocation.getText().toString().trim();
        dob= inputDob.getText().toString().trim();
        experience= inputExp.getText().toString().trim();
        qualification= inputQual.getText().toString().trim();
        designation= inputDes.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAME,name);
                params.put(KEY_MOBILE,mobile);
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_ADDRESS,address);
                params.put(KEY_LOCATION,location);
                params.put(KEY_DOB,dob);
                params.put(KEY_EXP, experience);
                params.put(KEY_QUAL,qualification);
                params.put(KEY_DES, designation);
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Intent registered = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(registered);

    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
        if(v == image1) {
            fromDatePickerDialog.show();
        }
    }
}