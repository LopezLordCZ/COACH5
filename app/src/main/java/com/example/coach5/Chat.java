package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = (ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            // This variable holds the name of the selected user
            String friendName = (String) extras.getString("friend_name");

            // Edit the chat title
            TextView chatTitle = (TextView) findViewById(R.id.chat_title);
            chatTitle.setText(friendName);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backbutton:
                startActivity(new Intent(this, Friends.class));
                break;
        }
    }
}
