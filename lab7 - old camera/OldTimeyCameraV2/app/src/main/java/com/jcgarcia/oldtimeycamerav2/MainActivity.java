package com.jcgarcia.oldtimeycamerav2;

import static android.hardware.Camera.ACTION_NEW_PICTURE;
import static android.hardware.Camera.open;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService execute;
    private Handler threadedHandler;
    private Camera camera;
    private Camera.Parameters cameraParameters;
    public SurfaceView surface;
    public SurfaceHolder mHolder;
    public int _openCameraFlag = 0;
    public static final String ACTION_NEW_PICTURE = Camera.ACTION_NEW_PICTURE;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = findViewById(R.id.surfaceView);
        mHolder = surface.getHolder();
        button = findViewById(R.id.button);

        //Assign the Instances of the Threadpool:
        execute = Executors.newFixedThreadPool(4);
        threadedHandler = HandlerCompat.createAsync(Looper.getMainLooper());

        button.setOnClickListener(click);

    }

    @Override
    public void onResume(){
        super.onResume();
        //Start Camera
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                openCamera();
                _openCameraFlag = 1;
                try {
                    camera.setPreviewDisplay(mHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        execute.submit(runnable);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onTakePictureClicked();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if(_openCameraFlag == 1){
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setCameraDisplayOrientation(MainActivity.this, 0, camera);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
                setCameraDisplayOrientation(MainActivity.this, 0, camera);
            }
        }
        else{
            Toast.makeText(this, "open the camera", Toast.LENGTH_SHORT).show();
        }
    }



    public void openCamera(){
        try{
            camera = open();
            //cameraParameters = camera.getParameters();
        }
        catch (Exception e){
            Log.e("OldTimeyCameara", Log.getStackTraceString(e));
        }
        try{
            cameraParameters = camera.getParameters();
        }
        catch (Exception e){
            Log.e("OldTimeyCameara", Log.getStackTraceString(e));
        }
        camera.setDisplayOrientation(90);
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void onTakePictureClicked() {
        Runnable takePicture = new Runnable() {
            @Override
            public void run() {
                Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        if (file == null) {
                            Log.e("TAG", "Unable to store images");
                            return;
                        }
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(bytes);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Log.e("TAG", "File not found:" + e.getMessage());
                            e.getStackTrace();
                        } catch (IOException e) {
                            Log.e("TAG", "I/O error writing file: " + e.getMessage());
                            e.getStackTrace();
                        }
                    }
                };
                camera.lock();
                camera.takePicture(null, null,null, pictureCallback);
                camera.unlock();
            }
        };

        execute.submit(takePicture);
    }

    private File getOutputMediaFile(int type)
    {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
        if (!dir.exists())
        {
            if (!dir.mkdirs())
            {
                Log.e("TAG", "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
        if (type == MEDIA_TYPE_IMAGE)
        {
            return new File(dir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        }
        else
        {
            return null;
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        Runnable pause = new Runnable() {
            @Override
            public void run() {
                _openCameraFlag = 0;
                camera.stopPreview();
                camera.release();
            }
        };
        execute.submit(pause);
    }


}