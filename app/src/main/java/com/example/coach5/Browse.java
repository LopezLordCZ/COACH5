package com.example.coach5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import androidx.appcompat.widget.SearchView;

public class Browse extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userName;

    RecyclerView recyclerView;
    SearchView searchView;
    Slider slider;
    BrowseAdapter browseAdapter;
    ArrayList<Coach> list;
    ArrayList<String> listId = new ArrayList<>();
    ArrayList<String[]> listIDemail = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Coaches");

        searchView = findViewById(R.id.action_search);
        slider = findViewById(R.id.action_slider);

        recyclerView = findViewById(R.id.recyclerViewCoaches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String UserEmail = currentUser.getEmail();

        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Dsnapshot: snapshot.getChildren()) {
                    User info = Dsnapshot.getValue(User.class);
                    if (UserEmail.equals(info.email)){
                        userName = info.name;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                            Coach coach = child.getValue(Coach.class);
                            list.add(coach);
                            String array[] = {coach.email, child.getKey()};
                            listIDemail.add(array);
                            listId.add(child.getKey());
                        }
                        browseAdapter = new BrowseAdapter(list, listId, userName);
                        recyclerView.setAdapter(browseAdapter);
                    }
                    browseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    Toast.makeText(Browse.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        if (slider != null) {
            slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(@NonNull Slider slider) {

                }

                @Override
                public void onStopTrackingTouch(@NonNull Slider slider) {
                    System.out.println(slider.getValue());
                    filterLocation(slider.getValue());
                }
            });



        }



    }


    private void search(String newText) {
        ArrayList<Coach> filterList = new ArrayList<>();
        ArrayList<String> filterApplied = new ArrayList<>();
        for (Coach coach : list) {
            if (coach.searchCondition(newText)) { //filter condition
                filterList.add(coach);
                System.out.println(listIDemail.size());
                for (int i = 0; i < listIDemail.size(); i++){
                    if (listIDemail.get(i)[0] == coach.email){
                        filterApplied.add(listIDemail.get(i)[1]);
                    }
                }
            }
        }
        browseAdapter = new BrowseAdapter(filterList, filterApplied, userName);
        recyclerView.setAdapter(browseAdapter);

    }



    private void filterLocation(Float val) {
        ArrayList<Coach> filterList = new ArrayList<>();
        ArrayList<String> filterApplied = new ArrayList<>();
        for (Coach coach : list) {
            int x = 101;
            try{
                x = Integer.parseInt(coach.location); //
                System.out.println(x); //
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
            if (x < val) { //filter condition
                filterList.add(coach);
                for (int i = 0; i < listIDemail.size(); i++){
                    if (listIDemail.get(i)[0] == coach.email){
                        filterApplied.add(listIDemail.get(i)[1]);
                    }
                }
            }
        }
        browseAdapter = new BrowseAdapter(filterList, filterApplied, userName);
        recyclerView.setAdapter(browseAdapter);
    }
}