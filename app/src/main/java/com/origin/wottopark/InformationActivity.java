package com.origin.wottopark;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//import com.ahmedelsayed.sunmiprinterutill.PrintMe;

public class InformationActivity extends AppCompatActivity{
    private Button infoButton;
    private EditText vehiclecolor, vehiclebrand,vehiclemodel;
    private String vehicleColor, vehicleBrand,vehicleModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_information);
        infoButton = (Button) findViewById(R.id.btn_info);
        Init_View();

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Intent intent =  new Intent(InformationActivity.this,InputActivity.class);
                    intent.putExtra("vehicleColor",vehicleColor);
                    intent.putExtra("vehicleModel",vehicleModel);
                    intent.putExtra("vehicleBrand",vehicleBrand);
                    startActivity(intent);
                }

            }
        });

    }


    private void Init_View(){
        vehiclecolor = (EditText)findViewById(R.id.edt_vehiclecolor);
        vehiclebrand = (EditText)findViewById(R.id.edt_vehiclebrand);
        vehiclemodel = (EditText)findViewById(R.id.edt_vehiclemodel);
//        //TODO change get data from shared preference
//        SharedPreferences loginshared = getSharedPreferences("userPref", MODE_PRIVATE);
//        vehicleName = (loginshared.getString("devicename", ""));
//        vehicleBrand = (loginshared.getString("deviceBrand", ""));
//        vehicleModel = (loginshared.getString("deviceModel", ""));
//        //TODO end
//
//        vehiclename.setText(vehicleName);
//        vehiclebrand.setText(vehicleBrand);
//        vehiclemodel.setText(vehicleModel);


    }
    public boolean validate() {
        boolean valid = true;

        vehicleColor = vehiclecolor.getText().toString();
        vehicleBrand = vehiclebrand.getText().toString();
        vehicleModel = vehiclemodel.getText().toString();

        if (vehicleColor.isEmpty()) {
            vehiclecolor.setError("enter a Device Name");
            valid = false;
        } else {
            vehiclecolor.setError(null);
        }
        if (vehicleModel.isEmpty()) {
            vehiclemodel.setError("enter a Device Model");
            valid = false;
        } else {
            vehiclemodel.setError(null);
        }
        if (vehicleBrand.isEmpty()) {
            vehiclebrand.setError("enter a Device Brand");
            valid = false;
        } else {
            vehiclebrand.setError(null);
        }


        return valid;
    }
}
