package com.jcgarcia.oldtimeycamerav2;

import static android.hardware.Camera.open;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = findViewById(R.id.surfaceView);
        mHolder = surface.getHolder();

        //Assign the Instances of the Threadpool:
        execute = Executors.newSingleThreadExecutor();
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

    public void onTakePictureClicked(View view){
        if(_openCameraFlag == 1){
            camera.takePicture(null, null, null, null);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        _openCameraFlag = 0;
        camera.stopPreview();
        camera.release();
    }

}