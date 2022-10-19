package com.origin.wottopark;


import static android.Manifest.permission_group.CAMERA;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;


public class ReadplateActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageView;
    TextView plate_txt;
    Context context;
    ProgressBar progressBar;
    String token = "";
    String countrycode="";
    Date date;
    DateFormat df;
    String plate_type="",region_type="",car_type="",last_digits="",timeStamp="";
    CardView plateCard,regionCard,vihicalCard;
    Button btnPlateread;
    String imagePath;
    Bitmap bitmap;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private static final int IMAGE_PICK_REQUEST = 12345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readplate);
        context=this;
        date = new Date();
        df = new SimpleDateFormat("MM/dd/");
        // Use London time zone to format the date in
        df.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        btnPlateread=findViewById(R.id.btn_readplate);
        imageView = findViewById(R.id.imageView);
        btnPlateread.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Read_func();
            }
        });
        progressBar=findViewById(R.id.homeprogress);
        plate_txt = findViewById(R.id.car_plate);

        plateCard=findViewById(R.id.cardView);
        token = "a58c5a5e11969bab5703e7e907f61dda0bfc5236";
        if (token.equals("")){
            Toast.makeText(context, "Token Not Found", Toast.LENGTH_SHORT).show();
        }else {
            WebRequest.client.addHeader("Authorization","Token "+token);
        }



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Read_func(){
        boolean pick = true;
        if(pick){
            if(!checkCameraPermission()){
                requestCameraPermission();
            }else{
                PickImage();
            }
        }else{
            if(!checkStoragePermission()){
                requestStoragePermission();
            }else PickImage();
        }
    }

    private void PickImage() {
        displayChoiceDialog();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePermission() {
        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return  res1 && res2;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_PICK_REQUEST)
            if(resultCode == RESULT_OK&&data!=null) {
                Uri selectedImageUri = data.getData();

                if(selectedImageUri!=null){
                    // image selected from gallary
                    imagePath = getRealPathFromURI(this, selectedImageUri);
                    try {
                        InputStream stream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bmp = BitmapFactory.decodeStream(stream);
                        imageView.setImageBitmap(bmp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else     // image captured from camera
                     bitmap = (Bitmap) data.getExtras().get("data");
                     imageView.setImageBitmap(bitmap);
            }else {
                Log.d("==>","Operation canceled!");
            }
    }
    //pick result method to get image after getting image form gallary or camera
    public void Extract_plate(Bitmap bm) {

            RequestParams params=new RequestParams();
            String file=imagePath;
            String compressed=compressImage(file);
            countrycode="tr";
            String baseurl="https://api.platerecognizer.com/v1/plate-reader/";

            Log.d("response", "filepath: "+file+" ");
            try {
                params.put("upload", new File(compressed));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            params.put("regions",countrycode);
            Log.d("response", "image to upload: "+params+" ");
            WebRequest.post(context,baseurl,params,new JsonHttpResponseHandler()
            {
                @Override
                public void onStart() {
                    progressBar.setVisibility(View.VISIBLE);
                    plate_txt.setText(null);
                    imageView.setImageResource(R.drawable.upload);

                    Log.d("response", "onStart: ");
                    super.onStart();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Log.d("response ",response.toString()+" ");
                    try {
                        //json array or results
                        JSONArray Jsresults = response.getJSONArray("results");
                        if (Jsresults.length()>0)
                        {
                            for (int i = 0; i < Jsresults.length(); i++) {
                                JSONObject tabObj = Jsresults.getJSONObject(i);
                                plate_txt.setText(tabObj.getString("plate"));

                                timeStamp=response.getString("timestamp");
                                plateCard.setVisibility(View.VISIBLE);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("response1", "onFailure: " + errorResponse + " ");
                    progressBar.setVisibility(View.GONE);
                    regionCard.setVisibility(View.GONE);

                    Toast.makeText(ReadplateActivity.this, errorResponse+"", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("response2", "onFailure: "+errorResponse+" ");
                    progressBar.setVisibility(View.GONE);

                    plateCard.setVisibility(View.GONE);

                    Toast.makeText(ReadplateActivity.this,errorResponse.toString()+"",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("response3", "onFailure: "+responseString+" ");
                    progressBar.setVisibility(View.GONE);


                    Toast.makeText(ReadplateActivity.this,responseString+"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            });

    }
    private void displayChoiceDialog() {
        String choiceString[] = new String[] {"Gallery" ,"Camera"};
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Select image from");
        dialog.setItems(choiceString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        if (which ==0) {
                            intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        } else {
                            intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                        startActivityForResult(
                                Intent.createChooser(intent, "Select profile picture"), IMAGE_PICK_REQUEST);
                    }
                }).show();
    }
    /**
     * get actual path from uri
     * @param context context
     * @param contentUri  uri
     * @return actual path
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public String compressImage(String filePath) {

        int resized= -1;

        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight =resized*7.0f;
        float maxWidth = resized*12.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth)
        {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;

            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);
        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth,
                    actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        //      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename(this);
        try {
            out = new FileOutputStream(filename);
            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, resized, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }
    public static String getFilename(Context context) {
        File file = new File(context.getFilesDir().getPath(), ".Foldername/PlateRecognizerHistory");

        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;

        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height/ (float)
                    reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }       final float totalPixels = width * height;

        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}
