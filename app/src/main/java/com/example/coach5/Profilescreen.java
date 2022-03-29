package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Profilescreen extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);

        //create the spinners
        Spinner sport1 = (Spinner) findViewById(R.id.Sport1);
        Spinner sport2 = (Spinner) findViewById(R.id.Sport2);
        Spinner sport3 = (Spinner) findViewById(R.id.Sport3);
        Spinner skill_level1 = (Spinner) findViewById(R.id.skill_level1);
        Spinner skill_level2 = (Spinner) findViewById(R.id.skill_level2);
        Spinner skill_level3 = (Spinner) findViewById(R.id.skill_level3);

        // Create ArrayAdapters using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSport = ArrayAdapter.createFromResource(this,
                R.array.Sports, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSkill_level = ArrayAdapter.createFromResource(this,
                R.array.Skill_levels, android.R.layout.simple_spinner_item);

        // Specify the layouts to use when the list of choices appears
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSkill_level.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        sport1.setAdapter(adapterSport);
        sport2.setAdapter(adapterSport);
        sport3.setAdapter(adapterSport);
        skill_level1.setAdapter(adapterSkill_level);
        skill_level2.setAdapter(adapterSkill_level);
        skill_level3.setAdapter(adapterSkill_level);

        back = (ImageView) findViewById(R.id.imageView2);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageView2:
                startActivity(new Intent(this, Homescreen.class));
                break;
        }
    }
}
