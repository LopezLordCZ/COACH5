package com.example.coach5;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Homescreen extends AppCompatActivity implements View.OnClickListener {

    private TextView logout;
    private Button browse;
    private Button profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(this);
        browse = (Button) findViewById(R.id.browse);
        browse.setOnClickListener(this);
        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, Profilescreen.class));
                break;
            case R.id.browse:
                startActivity(new Intent(this, Browse.class));
                break;
        }
    }

}
