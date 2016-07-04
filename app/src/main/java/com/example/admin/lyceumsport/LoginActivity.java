package com.example.admin.lyceumsport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button Btnregister;
    String email, password;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
    String chk, msg, id;
    boolean error;
    String ghanshyam;
    private String response;
    private boolean isConnected;
    private String URL_NEW_PREDICTION = "http://lyceumssports.com/androidlogin//loginindex.php";
    private String URL_NEW_PREDICTION1 = "http://lyceumssports.com/androidlogin//loginindex.php?id=id";
    private ProgressDialog pDialog;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mContext = this;
        inputEmail = (EditText) findViewById(R.id.editText1);
        inputPassword = (EditText) findViewById(R.id.editText2);
        Btnregister = (Button) findViewById(R.id.createacc);
        btnLogin = (Button) findViewById(R.id.submit);

        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if ((!inputEmail.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) {
                    checkConnection();
                    sub(view);
                } else if ((!inputEmail.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!inputPassword.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        checkConnection();
        super.onStart();
    }

    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager != null) {
            // record the fact that there is not connection
            isConnected = ni != null && ni.isConnected();
        }
    }

    public void sub(View v) {
        inputEmail = (EditText) findViewById(R.id.editText1);
        inputPassword = (EditText) findViewById(R.id.editText2);
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
//        new ghani().execute();
        if (isConnected) {
            new ghani().execute();
        } else {
            Toast.makeText(mContext, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }

    private class ghani extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub


            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
                    ServiceHandler.POST, params);

            // Log.d("Create Prediction Request: ", "> " + json);
            ServiceHandler sh = new ServiceHandler();
            // response = new String("");
            // Making a request to url and getting response
            response = sh.makeServiceCall(URL_NEW_PREDICTION, ServiceHandler.POST, params);
            // String akash=response;
            chk = response.replace("\n", "");
            try {
                JSONObject jsn = new JSONObject(response);
                JSONArray test = jsn.optJSONArray("result");
                for (int i = 0; i < test.length(); i++) {
                    JSONObject test1 = test.getJSONObject(i);
                    ghanshyam = test1.optString("msg");
                    System.out.print(msg);
                    id = test1.optString("id");
                    System.out.print(id);
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
          /*  Intent i = new Intent(MainActivity.this, MainActivity_Tab.class);
            startActivity(i);*/
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (ghanshyam.equals("success")) {

                // Toast.makeText(LoginActivity.this, "OK", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id", id);
                i.putExtras(mBundle);
                startActivity(i);

            } else {
                Toast.makeText(LoginActivity.this, "Username or Password is Wrong", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(LoginActivity.this, "OK", Toast.LENGTH_SHORT).show();
        }

    }

    public void back() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage("Do you wish to exit the App")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually

        alert.show();
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(MainActivity.this, "OOOKKK", Toast.LENGTH_SHORT).show();
        back();
    }


}

