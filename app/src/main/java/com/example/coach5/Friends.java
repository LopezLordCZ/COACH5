package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Friends extends AppCompatActivity implements View.OnClickListener  {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        back = (ImageView) findViewById(R.id.backbutton);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backbutton:
                startActivity(new Intent(this, Homescreen.class));
                break;
        }
    }
}