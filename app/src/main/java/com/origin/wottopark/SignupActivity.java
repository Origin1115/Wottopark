package com.origin.wottopark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private TextView alreadyhave;
    private EditText fullname,companyname,phonenumber,useremail,userpassword;
    private String fullName,companyName,phoneNumber,Email,Password;
    private TextView devicename,devicebrand,devicemodel,serialnumber,imeinumber;
    private String deviceName,deviceBrand,deviceModel,serialNumber,imeiNumber;
    private Button signupButton;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        alreadyhave = (TextView) findViewById(R.id.txt_alreadyhave);
        signupButton = (Button)findViewById(R.id.btn_signup);
        Init_view();

        alreadyhave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                golpgin_func();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Signup_Func();
                }
            }
        });

    }
    private void golpgin_func(){

        Intent intent =  new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);


    }
    public void Init_view(){
        SharedPreferences deviceShared = getSharedPreferences("DEVICES", MODE_PRIVATE);
        deviceName = (deviceShared.getString("DEVICENAME", ""));
        deviceBrand = (deviceShared.getString("DEVICEBRAND", ""));
        deviceModel = (deviceShared.getString("DEVICEMODEL", ""));
        serialNumber = (deviceShared.getString("DEVICEESERIAL", ""));
        imeiNumber = (deviceShared.getString("DEVICEIMEI", ""));
        fullname = (EditText)findViewById(R.id.edt_fullname);
        companyname = (EditText)findViewById(R.id.edt_companyname);
        phonenumber = (EditText)findViewById(R.id.edt_phonenumber);
        useremail = (EditText)findViewById(R.id.edt_email);
        userpassword = (EditText)findViewById(R.id.edt_password);
        devicename = (TextView) findViewById(R.id.txt_devicename);devicename.setText(deviceName);
        devicebrand = (TextView) findViewById(R.id.txt_devicebrand);devicebrand.setText(deviceBrand);
        devicemodel = (TextView) findViewById(R.id.txt_devicemodel);devicemodel.setText(deviceModel);
        imeinumber = (TextView) findViewById(R.id.txt_imeinumber);imeinumber.setText(imeiNumber);
        serialnumber = (TextView) findViewById(R.id.txt_serialnumber);serialnumber.setText(serialNumber);

    }
    public boolean validate() {
        boolean valid = true;

        fullName = fullname.getText().toString();
        companyName = companyname.getText().toString();
        phoneNumber = phonenumber.getText().toString();
        Email = useremail.getText().toString();
        Password = userpassword.getText().toString();

        if (Email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            useremail.setError("enter a valid email address");
            valid = false;
        } else {
            useremail.setError(null);
        }

        if (Password.isEmpty() || Password.length() < 5) {
            userpassword.setError("5 more alphanumeric characters");
            valid = false;
        } else {
            userpassword.setError(null);
        }
        if (fullName.isEmpty()) {
            fullname.setError("enter fullname");
            valid = false;
        } else {
            fullname.setError(null);
        }
        if(phoneNumber.isEmpty() || Integer.parseInt(phoneNumber) < 100000 )
        {
            //Error message for example
            phonenumber.setError("enter Valid phonenumber");
            valid = false;
        }else {
            phonenumber.setError(null);
        }

        if (companyName.isEmpty()) {
            companyname.setError("enter company name");
            valid = false;
        } else {
            companyname.setError(null);
        }

        return valid;
    }
    public void Signup_Func(){

        String reqUrl = BaseConst.Signup_URL;
        pDialog = ProgressDialog.show( SignupActivity.this ,"" ,"Wait..." ,false ,false );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, reqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.v("Signup ", "Response : " + response);
//                        Toast.makeText(SignupActivity.this,response,Toast.LENGTH_LONG).show();
                        Intent intent =  new Intent(SignupActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fullName", fullName);
                params.put("companyName", companyName);
                params.put("phone", phoneNumber);
                params.put("email", Email);
                params.put("password", Password);
                params.put("devicename", deviceName);
                params.put("deviceBrand", deviceBrand);
                params.put("deviceModel", deviceModel);
                params.put("imeiNumber", imeiNumber);
                params.put("serialNumber", serialNumber);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        requestQueue.add( stringRequest );


//        StringRequest postRequest = new StringRequest(Request.Method.POST, reqUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.v("Signup ", "Response : " + response);
//                        pDialog.dismiss();
//                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                        finish();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        pDialog.dismiss();
//                        Log.d("Error.Response", String.valueOf(error));
//                        Toast.makeText(SignupActivity.this, "Something_went_wrong, Try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("fullName", fullName);
//                params.put("companyName", companyName);
//                params.put("phone", phoneNumber);
//                params.put("email", Email);
//                params.put("password", Password);
//                params.put("devicename", deviceName);
//                params.put("deviceBrand", deviceBrand);
//                params.put("deviceModel", deviceModel);
//                params.put("imeiNumber", imeiNumber);
//                params.put("serialNumber", serialNumber);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/form-data");
//                //"{\"type\":\"example\"}"
//                return headers;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
//        requestQueue.add( postRequest );

    }
}