package com.example.coach5;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HomescreenCoach extends AppCompatActivity implements View.OnClickListener {

    private TextView logout;
    private Button profile;
    private Button matches;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepagecoach);

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(this);
        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(this);
        matches = (Button) findViewById(R.id.Matches);
        matches.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfilescreenCoach.class));
                break;
            case R.id.Matches:
                startActivity(new Intent(this, Friends.class));
                break;
        }
    }
}
