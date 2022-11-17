package com.jcgarcia.cpre388.pedometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor built_in_sensor;
    private TextView built_in;
    private TextView custom;
    private StepViewModel custom_viewModel;
    private Long actual_steps = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        built_in = findViewById(R.id.builtin);
        custom = findViewById(R.id.custom);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        built_in_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Custom Method of Counting Steps listener:
        custom_viewModel = new ViewModelProvider(this).get(StepViewModel.class);
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
                built_in.setText(set);
            }
        };

        custom_viewModel.getStepLiveData().observe(this, stepObserver);
        custom_viewModel.getSensorLiveData().observe(this, sensorObserver);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            actual_steps++;
            custom_viewModel.sen_storeData(actual_steps);
            Toast.makeText(getBaseContext(), "HELLO WORLD", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}


    public void onReset(View view){
        built_in.setText("0 Steps");
        custom.setText("0 Steps");
        custom_viewModel.release();
    }

}