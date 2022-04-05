package com.example.coach5;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    ArrayList<Message> list;

    //get current user
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Matches");

    public ChatAdapter(ArrayList<Message> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Message message = list.get(position); //get message
        holder.message = message;
        holder.currentUser = currentUser;

        //adjust the message text and color respectively
        holder.text.setText(message.message);
        if (message.sender.equals(currentUser.getUid())){
            holder.text.setTextColor(Color.rgb(255, 98, 0));
        } else if (message.receiver.equals(currentUser.getUid())){
            holder.text.setTextColor(Color.rgb(130, 130,130));
        } else {
            holder.text.setTextColor(Color.rgb(112, 39,195));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        Message message;
        FirebaseUser currentUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
