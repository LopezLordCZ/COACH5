package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friendscoach extends AppCompatActivity implements View.OnClickListener  {

    //setting up variables
    private DatabaseReference reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ImageView back;

    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;
    ArrayList<Match> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        reference = FirebaseDatabase.getInstance().getReference("Matches");

        recyclerView = findViewById(R.id.recyclerViewMatches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back = (ImageView) findViewById(R.id.backbutton);
        back.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        //looking for all the matches
                        list = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Match match = child.getValue(Match.class);
                            if (user.getUid().equals(match.coachID)){
                                list.add(match);
                            }
                        }
                        friendsAdapter = new FriendsAdapter(getBaseContext(), list);
                        recyclerView.setAdapter(friendsAdapter);
                    }
                    friendsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    Toast.makeText(Friendscoach.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //go back to the right homescreen
            case R.id.backbutton:
                startActivity(new Intent(this, HomescreenCoach.class));
                break;
        }
    }
}