package com.origin.wottopark;

import static com.origin.wottopark.BaseConst.BaseURL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.os.Bundle;

import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.SearchView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private ImageButton inbutton;
    private ImageButton outbutton,webviewButton;
    private ImageButton keyinbutton,keyoutbutton,inreadplate,outreadplate;
    private EditText inputPlate;
    private Button okButton,parkedButton;
    private String currentImei;
    private String PlateNumber,QrcodeValue;
    Dialog dialog;
//    private Button searchbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inbutton = (ImageButton) findViewById(R.id.img_in);
        outbutton = (ImageButton) findViewById(R.id.img_out);
        keyinbutton = (ImageButton)findViewById(R.id.img_key_in);
        keyoutbutton = (ImageButton)findViewById(R.id.img_key_out);
        inreadplate = (ImageButton)findViewById(R.id.img_cam_in);
        outreadplate = (ImageButton)findViewById(R.id.img_cam_out);
        webviewButton = (ImageButton)findViewById(R.id.img_webview);
        parkedButton = (Button)findViewById(R.id.btn_allparked);
        dialog = new Dialog(MainActivity.this);
        currentImei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        webviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Webview_func();
            }
        });
        parkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,ParkedActivity.class);
                startActivity(intent);
            }
        });
        inbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.popup_window);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.setCanceledOnTouchOutside(true);
                okButton = dialog.findViewById(R.id.btn_plateok);
                inputPlate = dialog.findViewById(R.id.edt_inputplatenumber);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                        String PlateNumber  = inputPlate.getText().toString();
                        if (PlateNumber.isEmpty()) {
                            inputPlate.setError("enter a Plate Number");

                        } else {
                            inputPlate.setError(null);
                            dialog.dismiss();
                            Intent intent =  new Intent(MainActivity.this,PriceActivity.class);
                            intent.putExtra("platenumber", PlateNumber);
                            startActivity(intent);
                        }


//                        Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });

        keyinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readplate_func();

//                dialog.setContentView(R.layout.popup_window);
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.setCancelable(false);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
//                dialog.setCanceledOnTouchOutside(true);
//                okButton = dialog.findViewById(R.id.btn_plateok);
//                inputPlate = dialog.findViewById(R.id.edt_inputplatenumber);
//                okButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//
//                        String PlateNumber  = inputPlate.getText().toString();
//                        if (PlateNumber.isEmpty()) {
//                            inputPlate.setError("enter a Plate Number");
//
//                        } else {
//                            inputPlate.setError(null);
//                            dialog.dismiss();
//                            Intent intent =  new Intent(MainActivity.this,PriceActivity.class);
//                            intent.putExtra("platenumber", PlateNumber);
//                            startActivity(intent);
//                        }
//
//
////                        Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                dialog.show();
            }
        });
        keyoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readplate_func();
//                dialog.setContentView(R.layout.popup_window);
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.setCancelable(false);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
//                dialog.setCanceledOnTouchOutside(true);
//                inputPlate = dialog.findViewById(R.id.edt_inputplatenumber);
//                okButton = dialog.findViewById(R.id.btn_plateok);
//
//                okButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PlateNumber  = inputPlate.getText().toString();
//                        if (PlateNumber.isEmpty()) {
//                            inputPlate.setError("enter a Plate Number");
//
//                        } else {
//                            inputPlate.setError(null);
//                            dialog.dismiss();
//                            Intent intent =  new Intent(MainActivity.this,OutActivity.class);
//                            intent.putExtra("platenumber", PlateNumber);
//                            startActivity(intent);
//                        }
//
//
//                       //Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//
//                dialog.show();
            }
        });

        outbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(MainActivity.this,QrcodeActivity.class);
                //intent.putExtra("platenumber", "123456");
                startActivity(intent);
            }
        });

        inreadplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodescan_func();
            }
        });
        outreadplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodescan_func();
            }
        });


    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private void barcodescan_func(){
        Intent intent =  new Intent(MainActivity.this,QrcodeActivity.class);
        startActivity(intent);

    }
    private void readplate_func(){
        Intent intent =  new Intent(MainActivity.this,ReadplateActivity.class);
        startActivity(intent);

    }
    private void Webview_func(){
        boolean netstatus = isNetworkConnected();
        if(netstatus == true){
            Intent intent =  new Intent(MainActivity.this,MainwebviewActivity.class);
            intent.putExtra("webviewurl", BaseConst.BaseURL + currentImei);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}