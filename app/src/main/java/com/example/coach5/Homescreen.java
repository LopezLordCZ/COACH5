package com.example.coach5;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class Homescreen extends AppCompatActivity implements View.OnClickListener {

    private TextView logout;
    private Button browse;
    private Button profile;
    private Button friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //setting up the buttons and textview
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(this);
        browse = (Button) findViewById(R.id.browse);
        browse.setOnClickListener(this);
        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(this);
        friends = (Button) findViewById(R.id.coaches);
        friends.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //logout button
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences = getSharedPreferences("PrefsFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(Homescreen.this,"You have been logged out!", Toast.LENGTH_LONG).show();
                break;
            case R.id.profile:
                //profilescreen button
                startActivity(new Intent(this, Profilescreen.class));
                break;
            case R.id.browse:
                //browse button
                startActivity(new Intent(this, Browse.class));
                break;
            case R.id.coaches:
                //coaches button
                startActivity(new Intent(this, Friends.class));
                break;
        }
    }
}
