package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Friends extends AppCompatActivity implements View.OnClickListener  {

    private ImageView back;
    private LinearLayout mainLayout;
    private LinearLayout friendsList;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        back = (ImageView) findViewById(R.id.backbutton);
        back.setOnClickListener(this);

        // Set layouts from the .xml file
        mainLayout = (LinearLayout) findViewById(R.id.friends_main_layout);
        friendsList = (LinearLayout) findViewById(R.id.friends_list);

        // Initialise the inflater to create new views
        inflater = getLayoutInflater();

        addChatEntry("friend 1");
        addChatEntry("friend 2");
        addChatEntry("friend 3");
        addChatEntry("friend 4");
        addChatEntry("friend 5");
        addChatEntry("friend 6");
        addChatEntry("friend 7");
        addChatEntry("friend 8");
        addChatEntry("friend 9");
        addChatEntry("friend 10");
    }

    private void addChatEntry(String username) {
        // Create a copy of the friends item view
        View friendLayout = inflater.inflate(R.layout.friends_layout_item, friendsList, false);

        // First edit its properties
        TextView nameView = (TextView) friendLayout.findViewById(R.id.friend_name);
        nameView.setText(username);
        nameView.setTag("friend_name");

        // Add the click listener
        friendLayout.setOnClickListener(this);

        // Add to main
        friendsList.addView(friendLayout);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backbutton) {
            startActivity(new Intent(this, Homescreen.class));
            return;
        }

        // Pass the user's name onto the Chat activity
        String name = (String) ((TextView) v.findViewWithTag("friend_name")).getText();
        Intent newChat = new Intent(this, Chat.class);
        newChat.putExtra("friend_name", name);

        startActivity(newChat);
    }
}