package com.example.coach5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.MyViewHolder> implements View.OnClickListener {

    ArrayList<Coach> list;

    public BrowseAdapter(ArrayList<Coach> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcoach,parent,false);

        Button contactButton;
        contactButton = v.findViewById(R.id.contactButton);
        contactButton.setOnClickListener(this);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Coach coach = list.get(position); //get coach
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sport1, sport2, sport3, skill1, skill2, skill3, name, age, rate;
        LinearLayout rowRate, rowSport2, rowSkill2, rowSport3, rowSkill3;

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


        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.contactButton) {
            System.out.println("Add contact to database");
        }
    }
}
