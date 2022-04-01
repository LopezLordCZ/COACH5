package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity implements View.OnClickListener  {

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ImageView back;

    private boolean isCoach = false;

    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;
    ArrayList<Match> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        mAuth = FirebaseAuth.getInstance();
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

                        list = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Match match = child.getValue(Match.class);
                            list.add(match);
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
                    Toast.makeText(Friends.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backbutton) {
            // Go back to the different screen depending on user type
            Class targetClass = Homescreen.class;
            if(isCoach) {
                targetClass = HomescreenCoach.class;
            }

            startActivity(new Intent(this, targetClass));
            return;
        }

        // Pass the user's name onto the Chat activity
        String name = (String) ((TextView) v.findViewWithTag("friend_name")).getText();
        Intent newChat = new Intent(this, Chat.class);
        newChat.putExtra("friend_name", name);

        startActivity(newChat);
    }
}