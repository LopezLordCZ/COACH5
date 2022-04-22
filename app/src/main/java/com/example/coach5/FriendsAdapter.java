package com.example.coach5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    //seting up variables
    ArrayList<Match> list;
    public Intent in;
    public Context context;

    //get current user
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Matches");

    //constructor
    public FriendsAdapter(Context con, ArrayList<Match> list) {
        context = con;
        in = new Intent(con,Homescreen.class);
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //assign correct layout to adapter
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_layout_item,parent,false);
        return new MyViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //get data
        Match match = list.get(position);
        holder.match = match;
        holder.currentUser = currentUser;
        holder.ref = reference;
        holder.match = match;

        //set the text to the name of the match
        if(match.userID.equals(currentUser.getUid())){
            //if current user is a user
            holder.name.setText(match.getCoachName());
        } else {
            //if current user is a coach
            holder.name.setText(match.getUserName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //variables
        TextView name;
        Button chat;
        Match match;
        FirebaseUser currentUser;
        DatabaseReference ref;

        public MyViewHolder(@NonNull View itemView, Context con) {
            super(itemView);

            //set up the adapter
            name = itemView.findViewById(R.id.friend_name);
            chat = itemView.findViewById(R.id.chat);

            //listener for the message button
            chat.setOnClickListener(v -> {
                if (v.getId() == R.id.chat) {
                    String Uid = currentUser.getUid();;

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot contact: snapshot.getChildren()){
                                //when clicking on the message button go to the chat and pass on the match data
                                v.getContext().startActivity(new Intent(con, Chat.class).putExtra("match", match));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            });
        }
    }
}
