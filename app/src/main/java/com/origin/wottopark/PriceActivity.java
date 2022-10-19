package com.origin.wottopark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PriceActivity extends AppCompatActivity {

    private TextView pricename;
    private String PriceName,PriceId,PlateNumber;
    private Button okButton;
    private String currentImei;
    private String reqUrl;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_price);
        okButton = (Button) findViewById(R.id.btn_price);
        pricename = (TextView)findViewById(R.id.txt_pricename);
        currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Intent intent = getIntent();
        PlateNumber = intent.getStringExtra("platenumber");
        reqUrl = BaseConst.Selectprice_URL + "/" + currentImei;
        pDialog = ProgressDialog.show( PriceActivity.this ,"" ,"Wait..." ,false ,false );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Request.Method.GET ,
                reqUrl,null,new Response.Listener <JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    pDialog.dismiss();
                    Log.v("Lisk Part------- ", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject( 0 );
                        PriceName = object.getString("priceName");
                        PriceId = object.getString("priceId");
                        //parkList.add( object.getString( "parkName" ) );
                    }
                    Log.v("PericePage--->added ", PriceName);
                    pricename.setText(PriceName);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("Unnati ", "Volley Error : " + error.getMessage());
                Toast.makeText(PriceActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue( PriceActivity.this );
        requestQueue.add( jsonArrayRequest );



        okButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent =  new Intent(PriceActivity.this,InputActivity.class);
                 intent.putExtra("pricename", PriceName);
                 intent.putExtra("priceid", PriceId);
                 intent.putExtra("PlateNumber",PlateNumber);
                 startActivity(intent);
             }
        });





    }






}