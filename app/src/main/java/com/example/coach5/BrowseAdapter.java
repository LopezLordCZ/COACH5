package com.example.coach5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.MyViewHolder> {

    ArrayList<Coach> list;
    ArrayList<String> listId;

    //get current user
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    public BrowseAdapter(ArrayList<Coach> list, ArrayList<String> listId) {
        this.listId = listId;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcoach,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Coach coach = list.get(position); //get coach

        holder.coachID = listId.get(position);
        holder.coach = coach;
        holder.currentUser = currentUser;
        holder.ref = reference;

        holder.sport1.setText(coach.getSport1());
        holder.skill1.setText(coach.getSkill1());
        holder.name.setText(coach.getName());
        holder.age.setText(coach.getAge());

        if (coach.getSport2().equals("Null")) { //make sport2 invisible if null
            holder.rowSport2.setVisibility(View.GONE);
            holder.rowSkill2.setVisibility(View.GONE);
        } else {
            holder.sport2.setText(coach.getSport2());
            holder.skill2.setText(coach.getSkill2());
        }

        if (coach.getSport3().equals("Null")) { //make sport3 invisible if null
            holder.rowSport3.setVisibility(View.GONE);
            holder.rowSkill3.setVisibility(View.GONE);
        } else {
            holder.sport3.setText(coach.getSport3());
            holder.skill3.setText(coach.getSkill3());
        }

        if (coach.getRate().equals("Null")) { //make rate invisible if null
            holder.rowRate.setVisibility(View.GONE);
        } else {
            holder.rate.setText(coach.getRate());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sport1, sport2, sport3, skill1, skill2, skill3, name, age, rate;
        LinearLayout rowRate, rowSport2, rowSkill2, rowSport3, rowSkill3;
        Button contactButton;
        Coach coach;
        String coachID;
        FirebaseUser currentUser;
        DatabaseReference ref;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sport1 = itemView.findViewById(R.id.tvSport1);
            sport2 = itemView.findViewById(R.id.tvSport2);
            sport3 = itemView.findViewById(R.id.tvSport3);
            skill1 = itemView.findViewById(R.id.tvSkill1);
            skill2 = itemView.findViewById(R.id.tvSkill2);
            skill3 = itemView.findViewById(R.id.tvSkill3);
            name = itemView.findViewById(R.id.tvName);
            age = itemView.findViewById(R.id.tvAge);
            rate = itemView.findViewById(R.id.tvRate);

            rowSport2 = itemView.findViewById(R.id.rowSport2);
            rowSkill2 = itemView.findViewById(R.id.rowSkill2);
            rowSport3 = itemView.findViewById(R.id.rowSport3);
            rowSkill3 = itemView.findViewById(R.id.rowSkill3);
            rowRate = itemView.findViewById(R.id.rowRate);

            contactButton = itemView.findViewById(R.id.contactButton);
            contactButton.setOnClickListener(v -> {
                if (v.getId() == R.id.contactButton) {
                    String Uid = currentUser.getUid();
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Matches");
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();


                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                boolean matchExists = false;
                                String coachId = coachID;
                                String matchId = "";
                                for (DataSnapshot contact: snapshot.getChildren()){
                                    matchId = contact.getKey();
                                    if (matchId.equals(Uid+coachId)) {
                                        Toast toast = Toast.makeText(v.getContext(), "Contact already added!", Toast.LENGTH_LONG);
                                        toast.show();
                                        matchExists = true;
                                    }
                                }
                                if (!matchExists){
                                    Match newMatch = new Match(Uid, coachId, ref.child(Uid).child("name").toString(), coach.name);
                                    Map<String, Object> testValues = newMatch.toMap();
                                    Map<String, Object> childupdates1 = new HashMap<>();
                                    childupdates1.put("Matches/"+Uid+coachId, testValues);

                                    reference2.updateChildren(childupdates1);

                                    Toast toast = Toast.makeText(v.getContext(), "New contact has been added!", Toast.LENGTH_LONG);
                                    toast.show();

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
