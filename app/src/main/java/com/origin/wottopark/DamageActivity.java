package com.origin.wottopark;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//import com.ahmedelsayed.sunmiprinterutill.PrintMe;

import org.json.JSONException;
import org.json.JSONObject;

public class DamageActivity extends AppCompatActivity implements View.OnClickListener{
    private String damageStatus;
    private CheckBox front1, front2, front3, back1, back2, back3, right1,right2, right3, right4,left1,left2, left3, left4, above;
    private boolean Front1 = false,Front2= false,Front3= false,Back1= false,Back2= false,Back3= false,
            Right1= false,Right2= false,Right3= false,Right4= false,Left1= false,Left2= false,Left3= false,Left4= false,Above= false;
    private JSONObject damages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_damage);
        Button damageButton = (Button) findViewById(R.id.btn_damage);
        damages = new JSONObject();
        Init_View();


        damageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    damages.put("front1",Front1);
                    damages.put("front2",Front2);
                    damages.put("front3",Front3);
                    damages.put("back1",Back1);
                    damages.put("back2",Back2);
                    damages.put("back3",Back3);
                    damages.put("left1",Left1);
                    damages.put("left2",Left2);
                    damages.put("left3",Left3);
                    damages.put("left4",Left4);
                    damages.put("right1",Right1);
                    damages.put("right2",Right2);
                    damages.put("right3",Right3);
                    damages.put("right4",Right4);
                    damages.put("above",Above);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                damageStatus = damages.toString();
                Intent intent =  new Intent(DamageActivity.this,InputActivity.class);
                intent.putExtra("damageStatus", damageStatus);
                startActivity(intent);


            }
        });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.front1:
                if (front1.isChecked())Front1 = true;
                    //Toast.makeText(getApplicationContext(), "front1", Toast.LENGTH_LONG).show();

                break;
            case R.id.front2:
                if (front2.isChecked())Front2 = true;
                    //Toast.makeText(getApplicationContext(), "front2", Toast.LENGTH_LONG).show();
                break;
            case R.id.front3:
                if (front3.isChecked()) Front3 = true;
                    //Toast.makeText(getApplicationContext(), "front3", Toast.LENGTH_LONG).show();
                break;
            case R.id.back1:
                if (back1.isChecked())Back1 = true;
                    //Toast.makeText(getApplicationContext(), "back1", Toast.LENGTH_LONG).show();
                break;
            case R.id.back2:
                if (back2.isChecked())Back2 = true;
                    //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.back3:
                if (back3.isChecked())Back3 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right1:
                if (right1.isChecked())Right1 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right2:
                if (right2.isChecked())Right2 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right3:
                if (right3.isChecked())Right3 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.right4:
                if (right4.isChecked())Right4 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left1:
                if (left1.isChecked())Left1 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left2:
                if (left2.isChecked())Left2 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left3:
                if (left3.isChecked())Left3 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.left4:
                if (left4.isChecked())Left4 = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
            case R.id.above:
                if (above.isChecked())Above = true;
                //Toast.makeText(getApplicationContext(), "back2", Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void Init_View(){
        front1 = (CheckBox) findViewById(R.id.front1);front1.setOnClickListener(this);
        front2 = (CheckBox) findViewById(R.id.front2);front2.setOnClickListener(this);
        front3 = (CheckBox) findViewById(R.id.front3);front3.setOnClickListener(this);
        back1 = (CheckBox) findViewById(R.id.back1);back1.setOnClickListener(this);
        back2 = (CheckBox) findViewById(R.id.back2);back2.setOnClickListener(this);
        back3 = (CheckBox) findViewById(R.id.back3);back3.setOnClickListener(this);
        right1 = (CheckBox) findViewById(R.id.right1);right1.setOnClickListener(this);
        right2 = (CheckBox) findViewById(R.id.right2);right2.setOnClickListener(this);
        right3 = (CheckBox) findViewById(R.id.right3);right3.setOnClickListener(this);
        right4 = (CheckBox) findViewById(R.id.right4);right4.setOnClickListener(this);
        left1 = (CheckBox) findViewById(R.id.left1);left1.setOnClickListener(this);
        left2 = (CheckBox) findViewById(R.id.left2);left2.setOnClickListener(this);
        left3= (CheckBox) findViewById(R.id.left3);left3.setOnClickListener(this);
        left4 = (CheckBox) findViewById(R.id.left4);left4.setOnClickListener(this);
        above = (CheckBox) findViewById(R.id.above);above.setOnClickListener(this);

    }



}
