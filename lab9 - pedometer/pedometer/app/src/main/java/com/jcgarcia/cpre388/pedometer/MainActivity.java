package com.jcgarcia.cpre388.pedometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView built_in;
    private TextView custom;
    private StepViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        built_in = findViewById(R.id.builtin);
        custom = findViewById(R.id.custom);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        viewModel = new ViewModelProvider(this).get(StepViewModel.class);
        final Observer<Float> stepObserver = new Observer<Float>(){
            @Override
            public void onChanged(@Nullable final Float steps){

            }
        };

        viewModel.getStepLiveData().observe(this, stepObserver);
    }

    public void onReset(View view){
        built_in.setText("0 Steps");
        custom.setText("0 Steps");
        viewModel.release();
    }




}