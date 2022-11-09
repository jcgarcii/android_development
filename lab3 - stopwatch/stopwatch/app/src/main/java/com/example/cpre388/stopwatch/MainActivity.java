package com.example.cpre388.stopwatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //Data Values:
    private long systemTime, currentTime, runningTime;
    private long hours, minutes, seconds, milliseconds;
    //String format:
    String sH, sM, sS, smS;
    //Function Objects:
    private Handler handler;
    private WatchModel watch;
    private int status = 0;
    //XML Elements:
    private AppCompatButton starter, reset;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign XML Elements:
        display = (TextView) findViewById(R.id.timer);
        starter = (AppCompatButton) findViewById(R.id.starter);
        reset = (AppCompatButton) findViewById(R.id.reset);

        //Begin:
        runningTime = 0L;
        handler = new Handler();

        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == 1){
                    starter.setText("Start");
                    status = 0;
                    watch.storeData(runningTime);
                    handler.removeCallbacks(time);
                }
                else {
                    //Updates Button and sets Flag.
                    starter.setText("Stop");
                    status = 1;
                    //Gathers Start Time
                    systemTime = SystemClock.uptimeMillis();
                    //ViewModel and Handler:
                    handler.postAtTime(time, 0);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starter.setText("Start");
                status = 0;
                handler.removeCallbacks(time);

                //systemTime = 0;
                milliseconds = 0;
                seconds = 0;
                minutes = 0;
                hours = 0;
                currentTime = 0L;
                systemTime = 0L;
                runningTime = 0L;
                watch.storeData(runningTime);

                sH = String.format("%02d", hours) + ":";
                sM = String.format("%02d", minutes) + ":";
                sS = String.format("%02d", seconds) + ".";
                smS = String.format("%02d", milliseconds);

                display.setText(sH+sM+sS+smS);
            }
        });

        watch = new ViewModelProvider(this).get(WatchModel.class);

        final Observer<Long> timeObserver = new Observer<Long>(){
            @Override
            public void onChanged(@Nullable final Long newName){
                seconds = (int) (newName / 1000);
                minutes = seconds / 60;
                hours = minutes / 60;
                seconds = seconds %60;
                milliseconds = (int) (newName %1000);

                sH = String.format("%02d", hours) + ":";
                sM = String.format("%02d", minutes) + ":";
                sS = String.format("%02d", seconds) + ".";
                smS = String.format("%02d", milliseconds);


                display.setText(sH+sM+sS+smS);
            }
        };

        watch.getWatchLiveData().observe(this, timeObserver);
    }

    //runnable object:
    public Runnable time = new Runnable() {
        @Override
        public void run() {
            currentTime = SystemClock.uptimeMillis() - systemTime;
            runningTime = currentTime + watch.getPreviousValue();
            watch.calculate(runningTime);
            handler.postDelayed(this, 10);
        }
    };
}



