package com.origin.wottopark;

import static android.content.ContentValues.TAG;

import static com.origin.wottopark.LoginActivity.currentUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;

    private String reqUrl;
    //private String currentImei;
    private String deviceimei;
    private String DEVICENAME,DEVICEMODEL,DEVICEBRAND,DEVICEESERIAL,DEVICEIMEI;
    SharedPreferences Splashsharedpreferences,DEVICESHAREDOREFERENCE;
    SharedPreferences.Editor splashEditor,DEVICEEDITOR;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_splash);

        DEVICEIMEI = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        DEVICENAME =  Build.DEVICE;
        DEVICEMODEL = Build.MODEL;
        DEVICEBRAND = Build.BRAND;
        DEVICEESERIAL = Build.ID;
        DEVICESHAREDOREFERENCE = getSharedPreferences("DEVICES",Context.MODE_PRIVATE);
        DEVICEEDITOR = DEVICESHAREDOREFERENCE.edit();
        DEVICEEDITOR.putString("DEVICENAME", DEVICENAME);
        DEVICEEDITOR.putString("DEVICEMODEL", DEVICEMODEL);
        DEVICEEDITOR.putString("DEVICEBRAND", DEVICEBRAND);
        DEVICEEDITOR.putString("DEVICEESERIAL", DEVICEESERIAL);
        DEVICEEDITOR.putString("DEVICEIMEI", DEVICEIMEI);
        DEVICEEDITOR.commit();



        Log.v("devicename-----",DEVICENAME);
        Log.v("devicemodel-----",DEVICEMODEL);
        Log.v("devicebrand-----",DEVICEBRAND);
        Log.v("deviceserial-----",DEVICEESERIAL);
        reqUrl = BaseConst.CheckIMEI + "/" + DEVICEIMEI;
        Splashsharedpreferences = getSharedPreferences("DeviceSetting",Context.MODE_PRIVATE);
        splashEditor = Splashsharedpreferences.edit();

        mRequestQueue = Volley.newRequestQueue(SplashActivity.this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("Splash Response-----",response.toString());
                    deviceimei = response.getString("imeinumrasi");
                    splashEditor.putInt("DeviceID", response.getInt("cihazId"));
                    splashEditor.putString("DeviceName", response.getString("cihazAdi"));
                    splashEditor.putString("DeviceBrand", response.getString("markasi"));
                    splashEditor.putString("DeviceModel", response.getString("modeli"));
                    splashEditor.putString("ImeiNumber", response.getString("imeinumrasi"));
                    splashEditor.putString("SerialNumber", response.getString("serinumrasi"));
                    splashEditor.putString("Header1", response.getString("header1"));
                    splashEditor.putString("Header2", response.getString("header2"));
                    splashEditor.putString("Footer1", response.getString("footer1"));
                    splashEditor.putString("Footer2", response.getString("footer2"));
                    splashEditor.putString("AppVersion", response.getString("uygulamaversiyonu"));
                    splashEditor.putString("AppUpdatedate", response.getString("uygulamaguncellemetarihi"));
                    splashEditor.putBoolean("PrintOutput", response.getBoolean("cikistaFisyazilsinmi"));
                    splashEditor.putBoolean("PrintInput", response.getBoolean("giristeFisYazilsinmi"));
                    splashEditor.putBoolean("SmsInput", response.getBoolean("giristeSmsGondermi"));
                    splashEditor.putBoolean("SmsOutput", response.getBoolean("cikistaSmsGondermi"));
                    splashEditor.putBoolean("PrintPayment", response.getBoolean("aboneyeFisYazdirmi"));
                    splashEditor.putString("InputSmsFormat", response.getString("giristeSmsFormati"));
                    splashEditor.putString("OutputSmsFormat", response.getString("cikistaSmsFormati"));
                    splashEditor.putString("AddDate", response.getString("eklemeTarihi"));
                    splashEditor.putString("CompanyId", response.getString("firmaId"));
                    //JSONObject companyObject = response.getJSONObject("firmaId");
                    //splashEditor.putString("Company", String.valueOf(response.getJSONObject("firma")));
                    splashEditor.commit();

                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // this is the error listener method which
            // we will call if we get any error from API.
            @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a toast message along with our error.
                //Toast.makeText(SplashActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        mRequestQueue.add(jsonObjectRequest);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Objects.equals(DEVICEIMEI, deviceimei)){
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);//MainActivity
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },3000);

    }






}