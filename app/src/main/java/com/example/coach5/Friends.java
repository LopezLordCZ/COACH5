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

public class Friends extends AppCompatActivity implements View.OnClickListener  {

    //setting up variables for data base
    private DatabaseReference reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //variable for imageview
    private ImageView back;

    //variables for view
    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;

    //variables for data
    ArrayList<Match> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //get right database reference
        reference = FirebaseDatabase.getInstance().getReference("Matches");

        //setup the recyclerview that contains the matches
        recyclerView = findViewById(R.id.recyclerViewMatches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setup the back button
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
                        //create a new list
                        list = new ArrayList<>();
                        //add every match to the list
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Match match = child.getValue(Match.class);
                            if (user.getUid().equals(match.userID)){
                                list.add(match);
                            }
                        }
                        //use the list to create the friends adapter
                        friendsAdapter = new FriendsAdapter(getBaseContext(), list);
                        recyclerView.setAdapter(friendsAdapter);
                    }
                    friendsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    Toast.makeText(Friends.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //going back to the homescreen using the back button
            case R.id.backbutton:
            startActivity(new Intent(this, Homescreen.class));
            break;
        }
    }
}