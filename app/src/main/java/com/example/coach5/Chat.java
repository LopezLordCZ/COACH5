package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    Match match;

    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    ArrayList<Message> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Matches");

        recyclerView = findViewById(R.id.recycler_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton send = findViewById(R.id.btn_send);
        send.setOnClickListener(this);

        backButton = (ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(this);

        if(getIntent().getExtras() != null) {
            match = (Match) getIntent().getSerializableExtra("match");
            TextView chatTitle = (TextView) findViewById(R.id.chat_title);
            chatTitle.setText(match.coachName);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (reference != null) {
            reference.child(match.userID+match.coachID).child("Messages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Message message = child.getValue(Message.class);
                            list.add(message);
                        }
                        chatAdapter = new ChatAdapter(list);
                        recyclerView.setAdapter(chatAdapter);
                    }
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    Toast.makeText(Chat.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backbutton:
                startActivity(new Intent(this, Friends.class));
                break;
            case R.id.btn_send:
                storeChat();
                break;
        }
    }

    public void storeChat() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        TextView text = findViewById(R.id.text_send);
        String textMessage = text.getText().toString();

        Message message = new Message(match.userID, match.coachID, textMessage);
        list.add(message);

        Match newMatch = new Match(match.userID, match.coachID, match.userName, match.coachName, list);

        Map<String, Object> testValues = newMatch.toMap();
        Map<String, Object> childupdates1 = new HashMap<>();
        childupdates1.put("Matches/"+match.userID+match.coachID, testValues);

        reference2.updateChildren(childupdates1);

    }

}
