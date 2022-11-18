package com.jcgarcia.cpre388.pedometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor built_in_sensor;
    private ExecutorService executor;
    private Handler threadedHandler;
    private Sensor accel;
    private TextView actual;
    private TextView custom;
    private StepViewModel custom_viewModel;
    private StepViewModel sensor_viewModel;
    private Long actual_steps = 0L;
    private Long calc_steps = 0L;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actual = findViewById(R.id.actualSensor);
        custom = findViewById(R.id.custom);

        executor = Executors.newFixedThreadPool(2);
        threadedHandler = HandlerCompat.createAsync(Looper.getMainLooper());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        built_in_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Custom Method of Counting Steps listener:
        custom_viewModel = new ViewModelProvider(this).get(StepViewModel.class);
        sensor_viewModel = new ViewModelProvider(this).get(StepViewModel.class);
        sensor_viewModel.release();
        custom_viewModel.release();

        final Observer<Long> stepObserver = new Observer<Long>(){
            @Override
            public void onChanged(@Nullable final Long steps){

            }
        };
        //Actual built in sensor listener:
        final Observer<Long> sensorObserver = new Observer<Long>(){
            @Override
            public void onChanged(@Nullable final Long steps){
                String set = String.format("Steps: " + steps.toString());
                actual.setText(set);
            }
        };

        custom_viewModel.getStepLiveData().observe(this, stepObserver);
        sensor_viewModel.getStepLiveData().observe(this, sensorObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, built_in_sensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
    }

    public void onReset(View view){
        actual.setText("STEPS RESET: 0 STEPS");
        custom.setText("STEPS RESET: 0 STEPS");
        custom_viewModel.release();
        sensor_viewModel.release();
        Toast.makeText(getBaseContext(), "Reset Steps", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            Runnable step = new Runnable() {
                @Override
                public void run() {
                    actual_steps = actual_steps + 1;
                    sensor_viewModel.step_storeData(actual_steps);
                }
            };
            executor.submit(step);
        }

        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Runnable acc = new Runnable() {
                @Override
                public void run() {

                }
            };
            executor.submit(acc);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}