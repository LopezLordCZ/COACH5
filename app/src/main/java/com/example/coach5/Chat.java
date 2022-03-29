package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private Button chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //chat = (Button) findViewById(R.id.profile);
        //chat.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String friendName = (String) extras.getString("friend_name");
            // This ^ variable holds the name of the selected user
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.profile:
                startActivity(new Intent(this, Friends.class));
                break;
        }
    }
}
