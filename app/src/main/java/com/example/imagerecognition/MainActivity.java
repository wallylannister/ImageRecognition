package com.example.imagerecognition;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button btn;
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
        btn = (Button) findViewById(R.id.btn1);


        btn.setOnClickListener(new View.OnClickListener() {
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
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path

        File file2up = new File(filePath);
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
        RequestBody requestBody = RequestBody
                .create(MediaType.parse("application/octet-stream"), buf);
        //Call <Void> mediaPost = uploadAPIs.uploadBinaryFile(requestBody);
        Call call = uploadAPIs.uploadBinaryFile(requestBody);
        call.enqueue(new Callback <JGson> () {
            @Override
            public void onResponse(Call <JGson> call, Response <JGson> response) {
                JGson jgson = new JGson();
                tv.setText(jgson.toString());
            }
            @Override
            public void onFailure(Call <JGson> call, Throwable t) {
                Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}