package com.example.admin.lyceumsport;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SingleItemView extends AppCompatActivity implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    String TAG = getClass().getName();
    // Declare Variables
    String rank, country, population, flag;
    String rankNew, countryNew, populationNew, flagNew, mUserId, mResponse, sportName;
    JSONObject jsonobject;
    JSONArray jsonarray;
    TextView txtRes, txtSessionName;

    private Context mContext;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private String url = "http://www.lyceumssports.com/androidlogin/incari.php";
    private String mSessionName, mSessionUrl;
    private String mGradeName;
    private ExpandableListView expListView;
    private WebView mWebView;
    private Button btnHideExpListV;
    private Context c;
    private boolean isConnected = true;
    private ProgressDialog PD;
    private ExpandListAdapter ExpAdapter;
    private NetworkImageView mSessionImageView;
    private RelativeLayout relativeLayout;
    ImageLoader imageLoaderNew = AppController.getInstance().getImageLoader();

    private ArrayList<ExpGroup> list;
    ArrayList<Child> ch_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);
        mContext = this;
        //Created by pankaj
        txtRes = (TextView) findViewById(R.id.txtResponse);
        txtSessionName = (TextView) findViewById(R.id.txtSessionName);
        mSessionImageView = (NetworkImageView) findViewById(R.id.imgSession);
        mWebView = (WebView) findViewById(R.id.webVwSessionDetails);
        expListView = (ExpandableListView) findViewById(R.id.expandListV);
        btnHideExpListV = (Button) findViewById(R.id.btnHideExpand);
        relativeLayout = (RelativeLayout) findViewById(R.id.relExpList);

        Bundle bundle = getIntent().getExtras();
        mUserId = bundle.getString("id");
        rankNew = bundle.getString("rank");
        countryNew = bundle.getString("country");
        flagNew = bundle.getString("flag");
        sportName = bundle.getString("name");


        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        expListView.setOnChildClickListener(this);
        btnHideExpListV.setOnClickListener(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni.getState() != NetworkInfo.State.CONNECTED) {
                // record the fact that there is not connection
                isConnected = false;
            }
        }

//        mWebView.setNetworkAvailable(isConnected);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (isConnected) {
//                    return false;
//                } else {
//                    return true;
//                }
                return false;
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //  super.onReceivedError(view, errorCode, description, failingUrl);        // Handle the error
                mWebView.loadUrl("file:///android_asset/error.html");
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // super.onReceivedError(view, req, rerr);
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        makejsonobjreq();
    }

    private void makejsonobjreq() {
        PD.show();
        String data = null;
        try {
            data = "?id=" + URLEncoder.encode(mUserId, "UTF-8");
            data += "&name=" + URLEncoder.encode(sportName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url + data, jsonobject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                list = new ArrayList<>();

                Log.w(TAG + "_RESPONSE=", "" + response.toString());
                try {
                    Iterator<String> key = response.keys();
                    while (key.hasNext()) {
                        String k = key.next();

                        ExpGroup gru = new ExpGroup();
                        gru.setName(k);
                        ch_list = new ArrayList<>();

                        JSONArray ja = response.getJSONArray(k);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);

                            Child ch = new Child();
                            ch.setName(jo.getString("section"));
                            ch.setSectionUrl(jo.getString("path"));

                            ch_list.add(ch);
                        } // for loop end
                        gru.setItems(ch_list);
                        list.add(gru);
                    } // while loop end

                    ExpAdapter = new ExpandListAdapter(mContext, list);
                    expListView.setAdapter(ExpAdapter);

                    PD.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });
        AppController.getInstance().addToReqQueue(jsonObjReq, "jreq");
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ExpGroup group = (ExpGroup) ExpAdapter.getGroup(groupPosition);
        mGradeName = group.getName();

        Child child = (Child) ExpAdapter.getChild(groupPosition, childPosition);
        mSessionName = child.getName();
        mSessionUrl = child.getSectionUrl();

        txtSessionName.setText(mSessionName);
        // mSessionImageView.setImageUrl(mSessionUrl, imageLoaderNew);
        if (haveNetworkConnection()) {
            mWebView.loadUrl(mSessionUrl);
//            mWebView.loadUrl("http://www.google.in");
        } else {
            mWebView.loadUrl("file:///android_asset/error.html");
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (relativeLayout.getVisibility() == View.GONE) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.GONE);
        }
    }
}