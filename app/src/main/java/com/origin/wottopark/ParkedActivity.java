package com.origin.wottopark;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkedActivity extends AppCompatActivity {
    private ArrayList<ParkModel> mVehicleList = new ArrayList<>();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiclelist);
        LoadDatas();

    }

    public void LoadDatas(){

       String currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

       String reqUrl = BaseConst.GetParkedVehicle_URL + "?imei=" + currentImei;


        pDialog = ProgressDialog.show( ParkedActivity.this ,"" ,"Wait..." ,false ,false );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Request.Method.GET ,
                reqUrl,null,new Response.Listener <JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    pDialog.dismiss();
                    Log.v("vehicle list------- ", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        ParkModel mvehicle = new ParkModel();
                        JSONObject jsonObject = response.getJSONObject( i );


                        //Get employee details
                        mvehicle.setPlatenumber(jsonObject.getString("plaka"));
                        mvehicle.setParkedtime(jsonObject.getString("parkSuresi"));
                        mvehicle.setTicketnumber(jsonObject.getString("biletId"));
                        mvehicle.setInputtime(jsonObject.getString("girisZamani"));
                        mvehicle.setTotalprice(jsonObject.getString("ucret"));

                        //Add employee object to the list
                        mVehicleList.add(mvehicle);

                    }
                    Log.v("Vehicle--->added ", mVehicleList.toString());
                    RecyclerView recyclerView = findViewById(R.id.myRecyclerView);

                    ParkViewAdapter myRecyclerViewAdapter = new ParkViewAdapter(mVehicleList);
                    recyclerView.setAdapter(myRecyclerViewAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ParkedActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("Unnati ", "Volley Error : " + error.getMessage());
                Toast.makeText(ParkedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue( ParkedActivity.this );
        requestQueue.add( jsonArrayRequest );


    }

}
