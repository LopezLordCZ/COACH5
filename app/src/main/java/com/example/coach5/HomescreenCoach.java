package com.example.coach5;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomescreenCoach extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private TextView logout;
    private Button profile;
    private Button matches;
    private String fSport1;
    private String fSport2;
    private String fSport3;
    private String fSkill1;
    private String fSkill2;
    private String fSkill3;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepagecoach);

        if (user != null) {
            //email address of current coach
            String email = user.getEmail();

            //get database reference
            reference = FirebaseDatabase.getInstance().getReference().child("Coaches");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Coach info = snapshot.getValue(Coach.class);
                        String coachEmail = info.email;
                        Log.d("firebase", coachEmail);
                        //make sure reference is same as current coach
                        if (info.email.equals(email)){
                            //save user specific info
                            fSport1 = info.sport1;
                            fSport2 = info.sport2;
                            fSport3 = info.sport3;
                            fSkill1 = info.sport1Skill;
                            fSkill2 = info.sport2Skill;
                            fSkill3 = info.sport3Skill;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if ((!fSport1.equals("Null") && !fSkill1.equals("Null")) || (!fSport2.equals("Null") && !fSkill2.equals("Null")) || (!fSport3.equals("Null") && !fSkill3.equals("Null"))){
            profile = (Button) findViewById(R.id.profile);
            profile.setOnClickListener(this);
            Toast.makeText(HomescreenCoach.this, "Complete your account first!", Toast.LENGTH_LONG).show();
        } else {
            logout = (TextView) findViewById(R.id.logout);
            logout.setOnClickListener(this);
            profile = (Button) findViewById(R.id.profile);
            profile.setOnClickListener(this);
            matches = (Button) findViewById(R.id.Matches);
            matches.setOnClickListener(this);
        }
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
