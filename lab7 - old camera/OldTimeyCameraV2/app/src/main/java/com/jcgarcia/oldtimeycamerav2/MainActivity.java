package com.jcgarcia.oldtimeycamerav2;

import static android.hardware.Camera.open;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
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
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    public Camera.PictureCallback jpeg;
    public int _openCameraFlag = 0;
    public Bitmap bitmap;
    public Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = findViewById(R.id.surfaceView);
        mHolder = surface.getHolder();

        //Assign the Instances of the Threadpool:
        execute = Executors.newFixedThreadPool(4);
        threadedHandler = HandlerCompat.createAsync(Looper.getMainLooper());

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

    public void onTakePictureClicked(){
        Runnable takePicture = new Runnable() {
            @Override
            public void run() {
                if(_openCameraFlag == 1){
                    camera.takePicture(null, null, null, jpeg);
                }
            }
        };
        execute.submit(takePicture);
    }



    public void saveImg(Bitmap bitmap){
        File dir = new File(Environment.getExternalStorageDirectory().toString()+
                '/'+getString(R.string.app_name));

        if(!dir.exists()){dir.mkdirs();}
        String name = System.currentTimeMillis() + ".png";
        File file = new File(dir, name);
        try{
            saveToStream(bitmap, new FileOutputStream(file));
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void saveToStream(Bitmap bitmap, OutputStream outputStream){
        if(outputStream != null){
            try{
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            }catch (Exception e){
                e.printStackTrace();
            }
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