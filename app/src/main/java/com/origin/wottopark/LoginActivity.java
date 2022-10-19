package com.origin.wottopark;

import static com.origin.wottopark.BaseConst.Fullname;
import static com.origin.wottopark.BaseConst.Mail;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button login;
    private EditText useremail;
    private EditText userpassword;
    private TextView donthave;
    private String email, password;
    private Integer mStatusCode;
    private boolean valid_login;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String   reqUrl ;
    private String currentImei, deviceimei;
    SharedPreferences Loginsharedpreferences;
    SharedPreferences.Editor editor;
    public static final String Fullname = "fullname";
    public static final String Companyname = "companyname";
    public static final String Phone = "phone";
    public static final String Email = "email";
    public static final String Password = "password";
    public static final String CompanyId = "companyId";
    public static final String Devicename = "devicename";
    public static final String DeviceBrand = "deviceBrand";
    public static final String DeviceModel = "deviceModel";
    public static final String ImeiNumber = "imeiNumber";
    public static final String SerialNumber = "serialNumber";
    public static final String currentUser = "userPref";
    ProgressDialog pDialog;
    Dialog dialog;
    ArrayAdapter adapter;
    ArrayList<String> arrayList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.btn_login);
        useremail = (EditText)findViewById(R.id.edt_email);
        userpassword = (EditText)findViewById(R.id.edt_password);
        donthave = (TextView)findViewById(R.id.txt_donthave);
        Loginsharedpreferences = getSharedPreferences(currentUser,Context.MODE_PRIVATE);
        editor = Loginsharedpreferences.edit();
        currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.v("IMEI----->",currentImei);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    login_func();
                }

            }
        });
        donthave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gosignup_func();
            }
        });
    }


    private void login_func(){
        pDialog = ProgressDialog.show( LoginActivity.this ,"" ,"Wait..." ,false ,false );

        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
//        Map<String, String> mParams = mParams = new HashMap<String, String>();;
//        mParams.put("email",email);
//        mParams.put("pasword",password);

        reqUrl = BaseConst.Login_URL +"/"+email +"/"+password+"/"+currentImei;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pDialog.dismiss();
                    Log.v("LoginResponse---",response.toString());
                    deviceimei = response.getString("imeiNumber");
                    editor.putString(Fullname, response.getString("fullName"));
                    editor.putString(Companyname, response.getString("companyName"));
                    editor.putString(Phone, response.getString("phone"));
                    editor.putString(Email, response.getString("email"));
                    editor.putString(Password, response.getString("password"));
                    //editor.putString(CompanyId, response.getString("companyId"));
                    editor.putString(Devicename, response.getString("devicename"));
                    editor.putString(DeviceBrand, response.getString("deviceBrand"));
                    editor.putString(DeviceModel, response.getString("deviceModel"));
                    editor.putString(ImeiNumber, response.getString("imeiNumber"));
                    Log.v("Device Imei---",response.getString("imeiNumber"));
                    editor.putString(SerialNumber, response.getString("serialNumber"));
                    editor.commit();
                    goMain_func();
                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something happens, Please try again", Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonObjectRequest);


    }
    private void gosignup_func(){

        Intent intent =  new Intent(LoginActivity.this,SignupActivity.class);
        //intent.putExtra("webviewurl", BaseConst.Signuppage_URL);
        startActivity(intent);
    }
    private void goMain_func(){
        if(Objects.equals(deviceimei, null)){
            Intent intent =  new Intent(LoginActivity.this,AdddeviceActivity.class);
            startActivity(intent);
        }else{

            //TODO chante avtivity
            Intent intent =  new Intent(LoginActivity.this,SelectparkActivity.class);
            startActivity(intent);//SelectparkActivity

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public boolean validate() {
        boolean valid = true;

        email = useremail.getText().toString();
        password = userpassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            useremail.setError("enter a valid email address");
            valid = false;
        } else {
            useremail.setError(null);
        }

        if (password.isEmpty() || password.length() < 5) {
            userpassword.setError("5 more alphanumeric characters");
            valid = false;
        } else {
            userpassword.setError(null);
        }

        return valid;
    }

}