package com.example.imagerecognition;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    ImageView iv;
    Intent data;
    Button btnUp, btnCapture;
    public  byte[] byteArray;
    public static final int CAMERA_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv1);
        btnUp = (Button) findViewById(R.id.btn1);
        btnCapture = (Button) findViewById(R.id.btn2);
        iv = (ImageView) findViewById(R.id.iv1);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);

            }
            public void onActivityResult (int requestCode, int resultCode, Intent data) {
                if (resultCode == RESULT_OK) {
//user is returning from capturing an image using the camera
                    if (requestCode == CAMERA_CAPTURE) {
//carry out the crop operation
//get the returned data
                        Bundle extras = data.getExtras();
//get the cropped bitmap
                        Bitmap thePic = extras.getParcelable("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        thePic.recycle();

                    }
                }
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    uploadToServer("/sdcard/Download/cit.jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void uploadToServer(String filePath) throws FileNotFoundException {

        //Create a file object using file path

        File file2up = new File(filePath);
        if(file2up.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(file2up.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.iv1);

            myImage.setImageBitmap(myBitmap);

        }
        // Create a request body with file and image media type
        //RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        //MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        //RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        //InputStream in = null;
        InputStream in = new FileInputStream(file2up);

        byte[] buf = new byte[0];
        try {
            buf = new byte[in.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if (in.read(buf) == -1) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        RequestBody requestBody = RequestBody
                .create(MediaType.parse("application/octet-stream"), buf);
        //Call <Void> mediaPost = uploadAPIs.uploadBinaryFile(requestBody);
        //Path path = Paths.get("/sdcard/Download", "cit.jpg");
        Call call = uploadAPIs.uploadBinaryFile(requestBody);
        //jgson = call.execute().body();
        call.enqueue(new Callback <JGson> () {
            @Override
            public void onResponse(Call <JGson> call, Response <JGson> response) {
                //Log.e(jgson.toString(),"HHHHHHHHHHHHH");
                JGson jgson = response.body();
                if (response.isSuccessful()){


                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
                        tv.setText(response.body().getName());
                } else{
                    Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_LONG).show();
                    // check error.
                }
            }
            @Override
            public void onFailure(Call <JGson> call, Throwable t) {
                Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}