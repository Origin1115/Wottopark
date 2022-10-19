package com.origin.wottopark;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OutActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    String QRValue = "",PlateNumber="",TicketId;
    private boolean iswhat;
    private Button completeButton;
    private ImageButton pickdateButton;
    private TextView tvTicknumber,tvPlatenumber,tvStarttime,tvEndtime,tvTotaltime,tvPrice,tvPricename;
    private TextView edFullname,edPhonenumber,edParklocation,edCardkeynubmer,edExplain;
    String ticketId,ticketNuber,plate,fullName,phone,parkLot,keyCardNumber,explanatin,startTime,outPutTime,totalTime,totalPrice,priceName,outPutTime1,outputTime;
    private PrintMe printMe;
    ProgressDialog pDialog;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private boolean PrintInput;
    private String currentImei;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    private int WIDTH = 100,WHITE = 0xffffff,BLACK = 0x000000;
    private String Header1,Header2,Footer1,Footer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        completeButton = (Button) findViewById(R.id.btn_complete);
        printMe =  new PrintMe(this);
        currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        Intent intent = getIntent();
        QRValue = intent.getStringExtra("qrCode");
        PlateNumber = intent.getStringExtra("platenumber");


        if(QRValue==null) {
            iswhat = false;

            Log.v("intent data Plate",PlateNumber);

        }else{
            iswhat = true;

            Log.v("intent data QR",QRValue);
        }

        Init_View();

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Complete_Func();
                }


            }
        });
        pickdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Calendar calendar = Calendar.getInstance();
//                year = calendar.get(Calendar.YEAR);
//                month = calendar.get(Calendar.MONTH);
//                day = calendar.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(OutActivity.this, OutActivity.this,year, month,day);
//                datePickerDialog.show();
            }
        });

        Load_Datas();
    }
    public void printText(View v){

        printMe.sendTextToPrinter((String) getText(R.string.app_name),18,true,false,2);
        printMe.sendTextToPrinter("QR value" + QRValue,18,true,false,2);

        printMe.sendTextToPrinter("OUTPUT TICKET",18,true,false,2);

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void printImage(View v){
//        printMe.sendImageToPrinter(
//                printMe.convertDrawableToBitmap(getDrawable(R.drawable.owwopark),100,100)
//        );
//    }

    public void printLayout(View v){
        View view = findViewById(R.id.print_me_out);
        printMe.sendViewToPrinter(view);
    }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myYear = year;
            myday = day;
            myMonth = month + 1;
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR);
            minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(OutActivity.this, OutActivity.this, hour, minute, DateFormat.is24HourFormat(this));
            timePickerDialog.show();
        }
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myHour = hourOfDay;
                myMinute = minute;
                outPutTime1 = myMonth + "/" + myday + "/" + myYear + " " + myHour +":" + myMinute;
                tvEndtime.setText(outPutTime1);


            }


    public void Complete_Func(){

            if(PrintInput){
                Record_Data();
                Print_Data();
            }else{
                Record_Data();
            }


    }

    public void Record_Data(){
        if(outputTime==null){
            SimpleDateFormat currentDateandTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            outputTime = currentDateandTime.format(new Date());
            Log.v( "get output time--> " , outputTime);


        }

        String reqUrl = BaseConst.Addoutputticket_URL;
        pDialog = ProgressDialog.show( OutActivity.this ,"" ,"Wait..." ,false ,false );
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, reqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.v( "Add new output " ,"Response : " + response );
                        //Toast.makeText(InputActivity.this,response,Toast.LENGTH_LONG).show();
                        Intent intent =  new Intent(OutActivity.this,MainActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.v("Addnew TIcket ", "Response : " + error.toString());
                        Toast.makeText(OutActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ticketid",ticketId);
                params.put("outputtime",outputTime);
                params.put("imei",currentImei);
                Log.v("Add newticket Datas-->",params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                //"{\"type\":\"example\"}"
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        requestQueue.add( stringRequest );

    }
    public void Print_Data(){
        printMe.sendTextToPrinter("YRS",24,true,false,2);
        printMe.sendTextToPrinter(Header1,20,true,false,2);
        printMe.sendTextToPrinter(Header2,20,true,true,2);
        printMe.sendTextToPrinter("Plate Number",20,true,true,2);
        printMe.sendTextToPrinter(plate,24,true,true,2);
        printMe.sendTextToPrinter("Entry Date",20,true,false,2);
        printMe.sendTextToPrinter(startTime,20,true,true,2);
        printMe.sendTextToPrinter("Exit Date",20,true,false,2);
        printMe.sendTextToPrinter(outputTime,20,true,true,2);
        printMe.sendTextToPrinter("Price" + totalPrice,20,true,true,2);

        Bitmap bitmap = null;
        QRCodeUtil generate = new QRCodeUtil();
        bitmap = generate.encodeAsBitmap(ticketId,250,250);
            //imageView.setImageBitmap(bitmap);

        printMe.sendImageToPrinter(bitmap);
        printMe.sendTextToPrinter("Explanation:"+Footer1,20,true,false,2);
        printMe.sendTextToPrinter("OUTPUT" + Footer2,20,true,false,2);

    }


    public void Init_View(){
        tvTicknumber = (TextView) findViewById(R.id.txt_ticketnumber);
        tvPlatenumber = (TextView) findViewById(R.id.txt_platenumber);
        tvStarttime = (TextView) findViewById(R.id.txt_starttime);
        tvEndtime = (TextView) findViewById(R.id.txt_endtime);
        tvTotaltime = (TextView) findViewById(R.id.txt_totaltime);
        tvPrice = (TextView) findViewById(R.id.txt_price);
        tvPricename = (TextView) findViewById(R.id.txt_pricename);
        edFullname = (TextView) findViewById(R.id.edt_fullname);
        edPhonenumber = (TextView) findViewById(R.id.edt_phonenumber);
        edParklocation = (TextView) findViewById(R.id.edt_parkloc);
        edCardkeynubmer = (TextView) findViewById(R.id.edt_cardkeynumber);
        edExplain = (TextView) findViewById(R.id.edt_explain);
        pickdateButton = (ImageButton)findViewById(R.id.img_endtime);

        //TODO change get data from shared preference
        SharedPreferences splashshared = getSharedPreferences("DeviceSetting", MODE_PRIVATE);
        Header1 = (splashshared.getString("Header1", ""));
        Header2 = (splashshared.getString("Header2", ""));
        Footer1 = (splashshared.getString("Footer1", ""));
        Footer2 = (splashshared.getString("Footer2", ""));
        PrintInput = (splashshared.getBoolean("PrintOutput", true));
        //TODO end
//        SimpleDateFormat currentDateandTime = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss z");
//        outputTime = currentDateandTime.format(new Date());
    }
    public void Load_Datas(){
        pDialog = ProgressDialog.show( OutActivity.this ,"" ,"Wait..." ,false ,false );

        mRequestQueue = Volley.newRequestQueue(OutActivity.this);
       String reqUrl;
       if(iswhat){
           reqUrl= BaseConst.Getoutputticket_URL +"?"+"ticketid=" +QRValue;
       }else{
           reqUrl= BaseConst.Getoutputticket_URL +"?"+"plate=" +PlateNumber;
       }

        // String reqUrl = BaseConst.Getoutputticket_URL;



        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, reqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pDialog.dismiss();
                    Log.v("Outputticket Response--",response.toString());
                    ticketId = response.getString("ticketId");
                    ticketNuber = response.getString("ticketNuber");
                    plate = response.getString("plate");
                    priceName = response.getString("priceName");
                    fullName = response.getString("fullName");
                    phone = response.getString("phone");
                    parkLot = response.getString("parkLot");
                    explanatin = response.getString("explanatin");
                    keyCardNumber = response.getString("keyCardNumber");
                    startTime = response.getString("startTime");
                    outPutTime = response.getString("outPutTime");
                    totalTime = response.getString("totalTime");
                    totalPrice = response.getString("totalPrice");

                    tvTicknumber.setText(ticketNuber);
                    tvPlatenumber.setText(plate);
                    tvStarttime.setText(startTime);
                    tvEndtime.setText(outPutTime);
                    tvTotaltime.setText(totalTime);
                    tvPrice.setText(totalPrice);
                    tvPricename.setText(priceName);
                    edFullname.setText(fullName);
                    edPhonenumber.setText(phone);
                    edParklocation.setText(parkLot);
                    edCardkeynubmer.setText(keyCardNumber);
                    edExplain.setText(explanatin);
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
                Toast.makeText(OutActivity.this, "There is no data, Please try again", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(OutActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        mRequestQueue.add(jsonObjectRequest);




    }
    public boolean validate() {
        boolean valid = true;

        fullName = edFullname.getText().toString();
        phone = edPhonenumber.getText().toString();
        parkLot = edParklocation.getText().toString();
        keyCardNumber = edCardkeynubmer.getText().toString();
        explanatin = edExplain.getText().toString();

        if (fullName.isEmpty()) {
            edFullname.setError("enter a Full Name");
            valid = false;
        } else {
            edFullname.setError(null);
        }
        if (phone.isEmpty()) {
            edPhonenumber.setError("enter a Phone Number");
            valid = false;
        } else {
            edPhonenumber.setError(null);
        }

        if (parkLot.isEmpty()) {
            edParklocation.setError("enter a Park Location");
            valid = false;
        } else {
            edParklocation.setError(null);
        }
        if (keyCardNumber.isEmpty()) {
            edCardkeynubmer.setError("enter a Card Key");
            valid = false;
        } else {
            edCardkeynubmer.setError(null);
        }

        return valid;
    }


}
