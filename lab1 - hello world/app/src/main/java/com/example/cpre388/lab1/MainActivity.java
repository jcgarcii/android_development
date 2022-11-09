package com.example.cpre388.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.cpre388.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textInitialization:
        EditText text = findViewById(R.id.textField);

        //intent initialization:
        Intent display = new Intent(MainActivity.this, DisplayActivity.class);

        //Button Stuff:
        Button send = findViewById(R.id.coolButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text.getText().toString();
                display.putExtra(EXTRA_MESSAGE, message);
                startActivity(display);
            }
        });

    }


}

