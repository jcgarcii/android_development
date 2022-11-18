package edu.iastate.cpre388.findmyfriends.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.iastate.cpre388.findmyfriends.R;

public class AddFriendDialogActivity extends AppCompatActivity {

    public static final String NEW_FRIEND_NAME = "edu.iastate.cpre388.findmyfriends.new_friend_name";
    public static final String NEW_FRIEND_MAC_ADDR = "edu.iastate.cpre388.findmyfriends.new_friend_mac_addr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_dialog);
        setTitle(R.string.add_friend_dialog_title);

        EditText nameEditText = findViewById(R.id.addFriendActivityName);
        EditText addrEditText = findViewById(R.id.addFriendActivityAddr);

        Button positiveButton = findViewById(R.id.addFriendActivityPositive);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(NEW_FRIEND_NAME, nameEditText.getText().toString());
                data.putExtra(NEW_FRIEND_MAC_ADDR, addrEditText.getText().toString());
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        Button negativeButton = findViewById(R.id.addFriendActivityNegative);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}