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

    //Definition
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userName;
    Double userLat;
    Double userLng;
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

        //Firebase call
        mAuth = FirebaseAuth.getInstance();
        //Reference Coach path
        reference = FirebaseDatabase.getInstance().getReference("Coaches");

        //Search
        searchView = findViewById(R.id.action_search);
        //Slider
        slider = findViewById(R.id.action_slider);

        //Recycler View
        recyclerView = findViewById(R.id.recyclerViewCoaches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get current email
        String UserEmail = currentUser.getEmail();

        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Dsnapshot: snapshot.getChildren()) {
                    User info = Dsnapshot.getValue(User.class);
                    //Check there is required email in users path
                    if (UserEmail.equals(info.email)){
                        userName = info.name;
                        userLat = info.lat;
                        userLng = info.lng;
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
                    Toast.makeText(Browse.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
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
                    filterLocation(slider.getValue());
                }
            });
        }
    }

    //Search function
    private void search(String newText) {
        ArrayList<Coach> filterList = new ArrayList<>();
        ArrayList<String> filterApplied = new ArrayList<>();
        for (Coach coach : list) {
            //Filter condition
            if (coach.searchCondition(newText)) {
                filterList.add(coach);
                //Inform log
                System.out.println(listIDemail.size());
                //Loop through the array
                for (int i = 0; i < listIDemail.size(); i++){
                    if (listIDemail.get(i)[0] == coach.email){
                        filterApplied.add(listIDemail.get(i)[1]);
                    }
                }
            }
        }
        //Define new BrowseAdapter
        browseAdapter = new BrowseAdapter(filterList, filterApplied, userName);
        recyclerView.setAdapter(browseAdapter);
    }

    //Location filter
    private void filterLocation(Float val) {
        ArrayList<Coach> filterList = new ArrayList<>();
        ArrayList<String> filterApplied = new ArrayList<>();
        //Check user has location
        if (userLat == null){
            Toast toast = Toast.makeText(findViewById(R.id.browse).getContext(), "Update location to use all features", Toast.LENGTH_LONG);
            toast.show();
        }
        for (Coach coach : list) {

            double d = -0.1;

            //Update distance
            if (userLat != null && userLng != null && coach.getLat() != null && coach.getLng() != null) {
                Double coachLat = coach.getLat();
                Double coachLng = coach.getLng();
                Distance dist = new Distance();
                d = dist.calcDistance(userLat, coachLat, userLng, coachLng);
            }

            //If distance is to coach is smaller than range slider value
            if (d < val) {
                filterList.add(coach);
                //Add coach to filter list
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