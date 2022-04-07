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
    String userName;

    //get current user
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    public BrowseAdapter(ArrayList<Coach> list, ArrayList<String> listId, String userName) {
        this.listId = listId;
        this.list = list;
        this.userName = userName;
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
        String currentUid = currentUser.getUid();

        holder.coachID = listId.get(position);
        holder.coach = coach;
        holder.currentUser = currentUser;
        holder.userName = userName;
        holder.ref = reference;

        holder.sport1.setText(coach.getSport1());
        holder.skill1.setText(coach.getSkill1());
        holder.name.setText(coach.getName());
        holder.age.setText(coach.getAge());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    String uid = child.getKey();
                    if (currentUid.equals(uid)) {
                        Boolean hasLat = child.hasChild("lat");
                        Boolean latIsNotNull = !(coach.getLat() == null);
                        if (latIsNotNull && hasLat){
                            User user = child.getValue(User.class);
                            Double uLat = user.getLat();
                            Double uLng = user.getLng();
                            Double cLat = coach.getLat();
                            Double cLng = coach.getLng();
                            //Double dist = calcDistance(uLat, cLat, uLng, cLng);
                            Distance dist = new Distance();
                            Double d = dist.calcDistance(uLat, cLat, uLng, cLng);

                            System.out.println(d);
                            String distStr = Double.toString(d);
                            holder.distance.setText(distStr + "km");
                        }
                        else {
                            holder.distance.setText("missing location data");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

        TextView sport1, sport2, sport3, skill1, skill2, skill3, name, age, rate, distance;
        LinearLayout rowRate, rowSport2, rowSkill2, rowSport3, rowSkill3;
        Button contactButton;
        Coach coach;
        String coachID;
        String userName;
        ArrayList<Message> list2 = new ArrayList<>();
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
            distance = itemView.findViewById(R.id.tvDistance);

            rowSport2 = itemView.findViewById(R.id.rowSport2);
            rowSkill2 = itemView.findViewById(R.id.rowSkill2);
            rowSport3 = itemView.findViewById(R.id.rowSport3);
            rowSkill3 = itemView.findViewById(R.id.rowSkill3);
            rowRate = itemView.findViewById(R.id.rowRate);

            contactButton = itemView.findViewById(R.id.contactButton);
            contactButton.setOnClickListener(v -> {
                if (v.getId() == R.id.contactButton) {
                    String Uid = currentUser.getUid();
                    String UserEmail = currentUser.getEmail();
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
                                    Message message = new Message("Boundary security", "Boundary security", "Send a messsage to start chatting!");
                                    list2.add(message);
                                    Match newMatch = new Match(Uid, coachId, userName, coach.name, list2);
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

    public static double calcDistance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return Math.round(distance);
    }

}
