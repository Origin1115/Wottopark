package com.origin.wottopark;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectparkActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button okButton;
    private String   reqUrl ;
    private String currentImei, deviceimei;
    public Spinner spinnerr;
    private ProgressDialog pDialog;
    private String selectedParkname;
    SharedPreferences SelectedParksharedpreferences;
    SharedPreferences.Editor selectedparkEditor;
    ArrayList<String> parkIdList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_parklist);
        currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.v("IMEI----->",currentImei);
        Init_View();
        //spinnerr = (Spinner)findViewById(R.id.spinner_parklist);
        okButton = (Button)findViewById(R.id.btn_parklist);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_parklist);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        ArrayList<String> parkList= new ArrayList<>();

        reqUrl = BaseConst.Getparklist_URL + currentImei;


        pDialog = ProgressDialog.show( SelectparkActivity.this ,"" ,"Wait..." ,false ,false );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Request.Method.GET ,
                reqUrl,null,new Response.Listener <JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    pDialog.dismiss();
                    Log.v("Lisk Part------- ", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject( i );
                        parkList.add( object.getString( "parkName" ) );
                        parkIdList.add(object.getString("parkId"));
                    }
                    Log.v("Parklist--->added ", parkList.toString());
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectparkActivity.this, android.R.layout.simple_spinner_item, parkList);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("Unnati ", "Volley Error : " + error.getMessage());
                Toast.makeText(SelectparkActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue( SelectparkActivity.this );
        requestQueue.add( jsonArrayRequest );







        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SelectparkActivity.this,MainActivity.class);
                intent.putExtra("parkname", selectedParkname);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        selectedParkname = item;
        SelectedParksharedpreferences = getSharedPreferences("SelectedPark",Context.MODE_PRIVATE);
        selectedparkEditor = SelectedParksharedpreferences.edit();
        selectedparkEditor.putString("ParkName", item);
        selectedparkEditor.putString("ParkId", parkIdList.get(position));
        selectedparkEditor.commit();

        Log.v("Parklist--->SElected ", String.valueOf(position));

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }




    public void Init_View(){

    }



}