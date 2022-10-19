package com.origin.wottopark;


import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AdddeviceActivity extends AppCompatActivity{
    private Button okButton;
    private TextView devicename,devicemodel,devicebrand,imeinumber,serialnumber,companyid;
    private String deviceName,deviceModel,deviceBrand,imeiNumber,serialNumber,companyId;

    private String   reqUrl ;
    private String currentImei;
    ProgressDialog pDialog;
    JSONObject jsonBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_device);
        currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        Init_View();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDevice_Func();
            }
        });

    }

    public void Init_View() {
        devicename = (TextView) findViewById(R.id.edt_devicename);
        devicemodel = (TextView)findViewById(R.id.edt_devicemodel);
        devicebrand = (TextView)findViewById(R.id.edt_devicebrand);
        imeinumber = (TextView)findViewById(R.id.edt_imeinumber);
        serialnumber = (TextView)findViewById(R.id.edt_serialnumber);
        companyid = (TextView)findViewById(R.id.edt_companyid);
        okButton = (Button) findViewById(R.id.btn_adddevice_ok);

        //imeinumber.setText(currentImei);
        SharedPreferences deviceShared = getSharedPreferences("DEVICES", MODE_PRIVATE);
        deviceName = (deviceShared.getString("DEVICENAME", ""));devicename.setText(deviceName);
        deviceBrand = (deviceShared.getString("DEVICEBRAND", ""));devicebrand.setText(deviceBrand);
        deviceModel = (deviceShared.getString("DEVICEMODEL", ""));devicemodel.setText(deviceModel);
        serialNumber = (deviceShared.getString("DEVICEESERIAL", ""));serialnumber.setText(serialNumber);
        imeiNumber = (deviceShared.getString("DEVICEIMEI", ""));imeinumber.setText(imeiNumber);
        SharedPreferences loginShared = getSharedPreferences("userPref", MODE_PRIVATE);
        companyId = (loginShared.getString("companyId", "0"));companyid.setText(companyId);

    }
    public boolean validate() {
        boolean valid = true;

        deviceName = devicename.getText().toString();
        deviceBrand = devicebrand.getText().toString();
        deviceModel = devicemodel.getText().toString();
        imeiNumber = imeinumber.getText().toString();
        serialNumber = serialnumber.getText().toString();
        companyId = companyid.getText().toString();

        if (deviceName.isEmpty()) {
            devicename.setError("enter a Device Name");
            valid = false;
        } else {
            devicename.setError(null);
        }
        if (deviceModel.isEmpty()) {
            devicemodel.setError("enter a Device Model");
            valid = false;
        } else {
            devicemodel.setError(null);
        }
        if (deviceBrand.isEmpty()) {
            devicebrand.setError("enter a Device Brand");
            valid = false;
        } else {
            devicebrand.setError(null);
        }
        if (imeiNumber.isEmpty()) {
            imeinumber.setError("enter a Android ID");
            valid = false;
        } else {
            imeinumber.setError(null);
        }
        if (serialNumber.isEmpty()) {
            serialnumber.setError("enter a Serial Number");
            valid = false;
        } else {
            serialnumber.setError(null);
        }
        if (companyId.isEmpty()) {
            companyid.setError("enter a Company ID");
            valid = false;
        } else {
            companyid.setError(null);
        }

        return valid;
    }
    public void AddDevice_Func()  {
        reqUrl = BaseConst.Adddevice_URL;
        pDialog = ProgressDialog.show( AdddeviceActivity.this ,"" ,"Wait..." ,false ,false );



        StringRequest postRequest = new StringRequest(Request.Method.POST, reqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.v("Unnati ", "Response : " + response);
                        pDialog.dismiss();
                        startActivity(new Intent(AdddeviceActivity.this, MainActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        pDialog.dismiss();
                        Log.d("Error.Response", String.valueOf(error));
                        Toast.makeText(AdddeviceActivity.this, "Something_went_wrong, Try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("devicename", deviceName);
                params.put("deviceBrand", deviceBrand);
                params.put("deviceModel", deviceModel);
                params.put("imeiNumber", imeiNumber);
                params.put("serialNumber", serialNumber);
                params.put("companyId", companyId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                //"{\"type\":\"example\"}"
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        requestQueue.add( postRequest );

    }
}