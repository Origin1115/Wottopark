package com.origin.wottopark;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity implements View.OnClickListener{
    private Button infoButton,damageButton,okButton,completeButton;
    private EditText  Fullname,Telephone,Parklocation,Platenumber,Keynumber,Explain;
    private String Plate;
    private TextView  Pricename;
    private String PriceName,FullName,Phone,Explanatin,InPutUserImei,DamageStatus = "",Brand,Model,Color,ParkLot,OtoParkId,PriceId,KeyCardNumber,InputTime;
    private String Header1,Header2,Footer1,Footer2,DamageStatus1;
    ProgressDialog pDialog,printDialog;
    private PrintMe printMe;
    private boolean PrintInput;
    private int WIDTH = 100,WHITE = 0xffffff,BLACK = 0x000000;
    Dialog dialog_vehicleInfo,dialog_vehicleDamage;

//    private Button infoOkButton;
    private EditText vehiclecolor, vehiclebrand,vehiclemodel;
    private String vehicleColor, vehicleBrand,vehicleModel;
    private Button damageOkButton;
    private CheckBox front1, front2, front3, back1, back2, back3, right1,right2, right3, right4,left1,left2, left3, left4, above;
    private boolean Front1 = false,Front2= false,Front3= false,Back1= false,Back2= false,Back3= false,
            Right1= false,Right2= false,Right3= false,Right4= false,Left1= false,Left2= false,Left3= false,Left4= false,Above= false;
    private JSONObject damages;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Intent intentt = getIntent();
        Plate = intentt.getStringExtra("PlateNumber");
        Log.v("intent data plate--->",Plate);
        Load_datas(Plate);
        Init_View();
        infoButton = (Button) findViewById(R.id.btn_vehicleinformation);
        damageButton = (Button) findViewById(R.id.btn_vehicledamage);
        completeButton = (Button)findViewById(R.id.btn_complete);




        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =  new Intent(InputActivity.this,InformationActivity.class);
//                startActivity(intent);
                dodialog_vehicleInfo();
            }
        });
        damageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dodialog_damageStatus();
            }
        });
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validate()){
                   Complete_Func();
               }

            }
        });
    }
    public void printText(View v){
        printMe.sendTextToPrinter((String) getText(R.string.app_name),18,true,false,2);

        printMe.sendTextToPrinter("INPUT TICKET",18,true,false,2);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void printImage(View v){
//        printMe.sendImageToPrinter(
//                printMe.convertDrawableToBitmap(getDrawable(R.drawable.owwopark),100,100)
//        );
//    }

    public void printLayout(View v){
        View view = findViewById(R.id.print_me_in);
        printMe.sendViewToPrinter(view);
    }

    private void Init_View(){
        Platenumber = (EditText)findViewById(R.id.edt_platenumber);
        Fullname = (EditText) findViewById(R.id.edt_surname);
        Telephone = (EditText) findViewById(R.id.edt_telephone);
        Parklocation = (EditText) findViewById(R.id.edt_parklocation);
        Keynumber = (EditText)findViewById(R.id.edt_cardkey);
        Pricename = (TextView)findViewById(R.id.tarifei);
        Explain = (EditText)findViewById(R.id.edt_explain);

        Intent intent = getIntent();
        PriceName = intent.getStringExtra("pricename");Pricename.setText(PriceName);
        PriceId = intent.getStringExtra("priceid");
//        Color = intent.getStringExtra("vehicleColor");
//        Model = intent.getStringExtra("vehicleModel");
//        Brand = intent.getStringExtra("vehicleBrand");
//        DamageStatus = intent.getStringExtra("damageStatus");

        InPutUserImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);


        //TODO change get data from shared preference
        SharedPreferences splashshared = getSharedPreferences("DeviceSetting", MODE_PRIVATE);
        Header1 = (splashshared.getString("Header1", ""));
        Header2 = (splashshared.getString("Header2", ""));
        Footer1 = (splashshared.getString("Footer1", ""));
        Footer2 = (splashshared.getString("Footer2", ""));
        PrintInput = (splashshared.getBoolean("PrintInput", true));
//        //TODO end
//        Fullname.setText(FullName);
//        Telephone.setText(Phone);
        //TODO change get data from shared preference


        //TODO change get data from shared preference
        SharedPreferences selectedParkshared = getSharedPreferences("SelectedPark", MODE_PRIVATE);
        ParkLot = (selectedParkshared.getString("ParkName", ""));
        OtoParkId = (selectedParkshared.getString("ParkId", ""));
        //TODO end
        Parklocation.setText(ParkLot);

        Platenumber.setText(Plate);



        //TODO end
    }
    public void Complete_Func(){

        Record_Data();

    }
    public void Record_Data(){
        SimpleDateFormat currentDateandTime = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss z");
        InputTime = currentDateandTime.format(new Date());
        String reqUrl = BaseConst.Addnewticket_URL;
        pDialog = ProgressDialog.show( InputActivity.this ,"" ,"Wait..." ,false ,false );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, reqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.v("Addnew TIcket ", "Response : " + response);
                        //Toast.makeText(InputActivity.this,response,Toast.LENGTH_LONG).show();
                        if(PrintInput){

                            Log.v("Print Flag----->", String.valueOf(PrintInput));
                            if(PrintInput){
                                Print_Data();
                            }


                            Intent intent =  new Intent(InputActivity.this,MainActivity.class);
                            startActivity(intent);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.v("Addnew TIcket ", "Response : " + error.toString());
                        Toast.makeText(InputActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Plate",Plate);
                params.put("PriceName",PriceName);
                params.put("FullName",FullName);
                params.put("Phone",Phone);
                params.put("Explanatin",Explanatin);
                params.put("InPutUserImei",InPutUserImei);
                params.put("DamageStatus",DamageStatus1);
                params.put("Brand",Brand);
                params.put("Model",Model);
                params.put("Color",Color);
                params.put("ParkLot",ParkLot);
                params.put("OtoParkId",OtoParkId);
                params.put("PriceId",PriceId);
                params.put("KeyCardNumber",KeyCardNumber);
                params.put("InputTime",InputTime);
                Log.v("Add newticket Datas-->",params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        requestQueue.add( stringRequest );




    }
    public void Print_Data(){
        printDialog = ProgressDialog.show( InputActivity.this ,"" ,"Wait..." ,false ,false );
        Bitmap bitmap = null;
        QRCodeUtil generate = new QRCodeUtil();
        bitmap = generate.encodeAsBitmap(Header2,250,250);

        printMe =  new PrintMe(this);
        printMe.sendTextToPrinter("YRS",24,true,false,2);
        printMe.sendTextToPrinter(Header1,20,true,false,2);
        printMe.sendTextToPrinter(Header2,20,true,true,2);
        printMe.sendTextToPrinter("Plate Number",20,true,false,2);
        printMe.sendTextToPrinter(Plate,24,true,true,2);
        printMe.sendTextToPrinter("Entry Date",20,true,false,2);
        printMe.sendTextToPrinter(InputTime,20,true,true,2);
        printMe.sendImageToPrinter(bitmap);
        printMe.sendTextToPrinter("Explanation:"+ Footer1,20,true,false,2);
        printMe.sendTextToPrinter("INPUT" + Footer2,20,true,false,2);
        printDialog.dismiss();

    }


    public boolean validate() {
        boolean valid = true;

        Plate = Platenumber.getText().toString();
        FullName = Fullname.getText().toString();
        Phone = Telephone.getText().toString();
        ParkLot = Parklocation.getText().toString();
        KeyCardNumber = Keynumber.getText().toString();
        Explanatin = Explain.getText().toString();

        if (Plate.isEmpty()) {
            Platenumber.setError("enter a Plate Number");
            valid = false;
        } else {
            Platenumber.setError(null);
        }
        if (FullName.isEmpty()) {
            Fullname.setError("enter a Full Name");
            valid = false;
        } else {
            Fullname.setError(null);
        }
        if (Phone.isEmpty()) {
            Telephone.setError("enter a Phone Number");
            valid = false;
        } else {
            Telephone.setError(null);
        }
        if (ParkLot.isEmpty()) {
            Parklocation.setError("enter a Park Location");
            valid = false;
        } else {
            Parklocation.setError(null);
        }
        if (KeyCardNumber.isEmpty()) {
            Keynumber.setError("enter a Card Key");
            valid = false;
        } else {
            Keynumber.setError(null);
        }
//        if (Model.isEmpty()) {
//            Toast.makeText(InputActivity.this, "Please Input Vehicle Information", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
//        if (DamageStatus.isEmpty()) {
//            Toast.makeText(InputActivity.this, "Please Check Damage Status", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
        return valid;
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.front1:
                if (front1.isChecked()){
                    DamageStatus +="front1,";
                    Front1 = true;
                }else Front1 = false;
                //Toast.makeText(getApplicationContext(), "front1", Toast.LENGTH_LONG).show();

                break;
            case R.id.front2:
                if (front2.isChecked()){
                    DamageStatus +="front2,";
                    Front2 = true;
                }else Front2 = false;

                //Toast.makeText(getApplicationContext(), "front2", Toast.LENGTH_LONG).show();
                break;
            case R.id.front3:
                if (front3.isChecked()) {
                    DamageStatus +="front3,";
                    Front3 = true;
                }else Front3 = false;
                //Toast.makeText(getApplicationContext(), "front3", Toast.LENGTH_LONG).show();
                break;
            case R.id.back1:
                if (back1.isChecked()){
                    DamageStatus +="back1,";
                    Back1 = true;
                }else Back1 = false;
                //Toast.makeText(getApplicationContext(), "back1", Toast.LENGTH_LONG).show();
                break;
            case R.id.back2:
                if (back2.isChecked()){
                    DamageStatus +="back2,";
                    Back2 = true;
                }else Back2 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.back3:
                if (back3.isChecked()){
                    DamageStatus +="back3,";
                    Back3 =true;
                }else Back3 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right1:
                if (right1.isChecked()){
                    DamageStatus +="right1,";
                    Right1 = true;
                }else Right1 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right2:
                if (right2.isChecked()){
                    DamageStatus +="right2,";
                    Right2 = true;
                }else Right2 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right3:
                if (right3.isChecked()){
                    DamageStatus +="right3,";
                    Right3 = true;
                }else Right3 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right4:
                if (right4.isChecked()){
                    DamageStatus +="right4,";
                    Right4 = true;
                }else Right4 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left1:
                if (left1.isChecked()){
                    DamageStatus +="left1,";
                    Left1 = true;
                }else Left1 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left2:
                if (left2.isChecked())
                {
                    DamageStatus +="left2,";
                    Left2 = true;
                }else Left2 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left3:
                if (left3.isChecked()){
                    DamageStatus +="left3,";
                    Left3 = true;
                }else Left3 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left4:
                if (left4.isChecked()){
                    DamageStatus +="left4,";
                    Left4 =true;
                }else Left4 = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.above:
                if (above.isChecked()){
                    DamageStatus +="above,";
                    Above = true;
                }else Above = false;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void dodialog_damageStatus() {
        dialog_vehicleDamage = new Dialog(InputActivity.this);
        dialog_vehicleDamage.setContentView(R.layout.popup_damage);
        dialog_vehicleDamage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_vehicleDamage.setCancelable(false);
        dialog_vehicleDamage.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog_vehicleDamage.setCanceledOnTouchOutside(true);
        damages = new JSONObject();


//        damageStatus = damages.toString();
        front1 = dialog_vehicleDamage. findViewById(R.id.front1);front1.setOnClickListener(this);front1.setChecked(Front1);
        front2 = dialog_vehicleDamage. findViewById(R.id.front2);front2.setOnClickListener(this);front2.setChecked(Front2);
        front3 = dialog_vehicleDamage. findViewById(R.id.front3);front3.setOnClickListener(this);front3.setChecked(Front3);
        back1 = dialog_vehicleDamage.  findViewById(R.id.back1);back1.setOnClickListener(this);back1.setChecked(Back1);
        back2 = dialog_vehicleDamage. findViewById(R.id.back2);back2.setOnClickListener(this);back2.setChecked(Back2);
        back3 = dialog_vehicleDamage. findViewById(R.id.back3);back3.setOnClickListener(this);back3.setChecked(Back3);
        right1 = dialog_vehicleDamage.  findViewById(R.id.right1);right1.setOnClickListener(this);right1.setChecked(Right1);
        right2 = dialog_vehicleDamage.  findViewById(R.id.right2);right2.setOnClickListener(this);right2.setChecked(Right2);
        right3 = dialog_vehicleDamage.  findViewById(R.id.right3);right3.setOnClickListener(this);right3.setChecked(Right3);
        right4 = dialog_vehicleDamage.  findViewById(R.id.right4);right4.setOnClickListener(this);right4.setChecked(Right4);
        left1 = dialog_vehicleDamage.  findViewById(R.id.left1);left1.setOnClickListener(this);left1.setChecked(Left1);
        left2 = dialog_vehicleDamage. findViewById(R.id.left2);left2.setOnClickListener(this);left2.setChecked(Left2);
        left3= dialog_vehicleDamage.  findViewById(R.id.left3);left3.setOnClickListener(this);left3.setChecked(Left3);
        left4 = dialog_vehicleDamage. findViewById(R.id.left4);left4.setOnClickListener(this);left4.setChecked(Left4);
        above = dialog_vehicleDamage. findViewById(R.id.above);above.setOnClickListener(this);above.setChecked(Above);
        damageOkButton=dialog_vehicleDamage.findViewById(R.id.btn_damage);
        damageOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    dialog_vehicleDamage.dismiss();
//                    try {
//                        damages.put("front1",Front1);
//                        damages.put("front2",Front2);
//                        damages.put("front3",Front3);
//                        damages.put("back1",Back1);
//                        damages.put("back2",Back2);
//                        damages.put("back3",Back3);
//                        damages.put("left1",Left1);
//                        damages.put("left2",Left2);
//                        damages.put("left3",Left3);
//                        damages.put("left4",Left4);
//                        damages.put("right1",Right1);
//                        damages.put("right2",Right2);
//                        damages.put("right3",Right3);
//                        damages.put("right4",Right4);
//                        damages.put("above",Above);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                DamageStatus = "";
                if(Front1)DamageStatus+="front1,";
                if(Front2)DamageStatus+="front2,";
                if(Front3)DamageStatus+="front3,";
                if(Back1)DamageStatus+="back1,";
                if(Back2)DamageStatus+="back2,";
                if(Back3)DamageStatus+="back3,";
                if(Right1)DamageStatus+="right1,";
                if(Right2)DamageStatus+="right2,";
                if(Right3)DamageStatus+="right3,";
                if(Right4)DamageStatus+="right4,";
                if(Left1)DamageStatus+="left1,";
                if(Left2)DamageStatus+="left2,";
                if(Left3)DamageStatus+="left3,";
                if(Left4)DamageStatus+="left4,";
                if(Above)DamageStatus+="above,";


                DamageStatus1 = method(DamageStatus);
                if(DamageStatus1.isEmpty())DamageStatus1 = "No Damgage";
                    Log.v("Damage Stgatus-->", DamageStatus1);
                }



        });
        dialog_vehicleDamage.show();

    }
    public boolean validate_Info() {
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
    public void Load_datas(String plate){
        pDialog = ProgressDialog.show( InputActivity.this ,"" ,"Wait..." ,false ,false );

        mRequestQueue = Volley.newRequestQueue(InputActivity.this);

        String reqUrl= BaseConst.GetDevicebyPlate_URL +"?"+"plate=" +plate;


        // String reqUrl = BaseConst.Getoutputticket_URL;



        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pDialog.dismiss();
                    Log.v("Inputticket Response--",response.toString());
                    //String Plate,PriceName,FullName,Phone,Explanatin,InPutUserImei,DamageStatus = "",Brand,Model,Color,ParkLot,OtoParkId,PriceId,KeyCardNumber,InputTime;

                    FullName = response.getString("fullName");
                    Phone = response.getString("phone");
                    Brand = response.getString("brand");
                    Model = response.getString("vehicleModel");
                    Color = response.getString("vehicleColor");

                    Fullname.setText(FullName);
                    Telephone.setText(Phone);
//                    vehiclebrand.setText(Brand);
//                    vehiclemodel.setText(Model);
//                    vehiclecolor.setText(Color);

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
                Toast.makeText(InputActivity.this, "Something happens in Inputpage, Please try again", Toast.LENGTH_SHORT).show();

            }
        });
        mRequestQueue.add(jsonObjectRequest);


    }
    public void dodialog_vehicleInfo() {
        dialog_vehicleInfo = new Dialog(InputActivity.this);
        dialog_vehicleInfo.setContentView(R.layout.popup_information);
        dialog_vehicleInfo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_vehicleInfo.setCancelable(false);
        dialog_vehicleInfo.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog_vehicleInfo.setCanceledOnTouchOutside(true);

        vehiclecolor = dialog_vehicleInfo.findViewById(R.id.edt_vehiclecolor);
        vehiclebrand = dialog_vehicleInfo.findViewById(R.id.edt_vehiclebrand);
        vehiclemodel = dialog_vehicleInfo.findViewById(R.id.edt_vehiclemodel);
        //Load_datas1(Plate);
        vehiclebrand.setText(Brand);
        vehiclemodel.setText(Model);
        vehiclecolor.setText(Color);
        //todo--read plate info
        // pDialog = ProgressDialog.show( InputActivity.this ,"" ,"Wait..." ,false ,false );

        mRequestQueue = Volley.newRequestQueue(InputActivity.this);

        Button infoOkButton=dialog_vehicleInfo.findViewById(R.id.btn_info);
        infoOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_vehicleInfo.dismiss();
                if(validate_Info()){

                    Color = vehiclecolor.getText().toString();
                    Brand = vehiclebrand.getText().toString();
                    Model = vehiclemodel.getText().toString();
                }

            }
        });
        dialog_vehicleInfo.show();



    }
    public void Load_datas1(String plate){
        pDialog = ProgressDialog.show( InputActivity.this ,"" ,"Wait..." ,false ,false );

        mRequestQueue = Volley.newRequestQueue(InputActivity.this);

        String reqUrl= BaseConst.GetDevicebyPlate_URL +"?"+"plate=" +plate;


        // String reqUrl = BaseConst.Getoutputticket_URL;



        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pDialog.dismiss();
                    Log.v("Inputticket Response--",response.toString());
                    String Plate,PriceName,FullName,Phone,Explanatin,InPutUserImei,DamageStatus = "",Brand,Model,Color,ParkLot,OtoParkId,PriceId,KeyCardNumber,InputTime;

//                    FullName = response.getString("fullName");
//                    Phone = response.getString("phone");
                    Brand = response.getString("brand");
                    Model = response.getString("vehicleModel");
                    Color = response.getString("vehicleColor");

//                    Fullname.setText(FullName);
//                    Telephone.setText(Phone);
                    vehiclebrand.setText(Brand);
                    vehiclemodel.setText(Model);
                    vehiclecolor.setText(Color);

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
                Toast.makeText(InputActivity.this, "Something happens in Inputpage, Please try again", Toast.LENGTH_SHORT).show();

            }
        });
        mRequestQueue.add(jsonObjectRequest);


    }
    public String method(String str) {
        if (str != null && str.length() > 0 ) {//&& str.charAt(str.length() - 1) == 'x'
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

}
