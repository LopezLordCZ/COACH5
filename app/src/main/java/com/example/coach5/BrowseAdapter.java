package com.example.coach5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.MyViewHolder> {

    ArrayList<Coach> list;

    public BrowseAdapter(ArrayList<Coach> list) {
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

        Coach coach = list.get(position);
        holder.sport.setText(coach.getSport());
        holder.name.setText(coach.getName());
        holder.age.setText(coach.getAge());
        holder.rate.setText(coach.getRate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sport, name, age, rate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sport = itemView.findViewById(R.id.tvSport);
            name = itemView.findViewById(R.id.tvName);
            age = itemView.findViewById(R.id.tvAge);
            rate = itemView.findViewById(R.id.tvRate);

        }
    }
}
