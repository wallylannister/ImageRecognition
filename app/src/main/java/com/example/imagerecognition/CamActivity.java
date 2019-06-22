package com.example.imagerecognition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

public class CamActivity extends AppCompatActivity {
    private Camera camera;
    private LinearLayout scrollImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        scrollImages = (LinearLayout)findViewById(R.id.scrollImages);

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                camera.stopPreview();
                camera.release();
                camera = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                camera = Camera.open();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                // TODO Auto-generated method stub

            }
        });

        ImageButton btnTakePicture;
        btnTakePicture = (ImageButton) findViewById(R.id.btn);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                camera.takePicture(null, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        // TODO Auto-generated method stub
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inSampleSize = 4;
                        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, option);

                        ImageView imageView = new ImageView(getApplicationContext());
                        imageView.setImageBitmap(b);
                        scrollImages.addView(imageView);
                    }
                });
            }
        });
    }



}
