package com.example.cpre388.lab1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        TextView display = findViewById(R.id.displayText);

        //gather intent
        Intent previous = getIntent();
        String message = previous.getStringExtra(MainActivity.EXTRA_MESSAGE);

        display.setText(message);



    }
}

