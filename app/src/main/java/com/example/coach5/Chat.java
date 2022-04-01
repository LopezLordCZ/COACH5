package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;
    ArrayList<Match> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Matches");

        recyclerView = findViewById(R.id.recycler_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backButton = (ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(this);

        if(getIntent().getExtras() != null) {
            Match match = (Match) getIntent().getSerializableExtra("match");
            TextView chatTitle = (TextView) findViewById(R.id.chat_title);
            chatTitle.setText(match.coachName);
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
