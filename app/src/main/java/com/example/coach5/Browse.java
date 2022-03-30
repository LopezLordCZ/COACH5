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

import com.google.firebase.auth.FirebaseAuth;
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

    RecyclerView recyclerView;
    SearchView searchView;
    BrowseAdapter browseAdapter;
    ArrayList<Coach> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Coaches");

        searchView = findViewById(R.id.action_search);
        recyclerView = findViewById(R.id.recyclerViewCoaches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                        }
                        browseAdapter = new BrowseAdapter(list);
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
    }

    private void search(String newText) {
        ArrayList<Coach> filterList = new ArrayList<>();
        for (Coach coach : list) {
            if (coach.searchCondition(newText)) { //filter condition
                filterList.add(coach);
            }
        }
        browseAdapter = new BrowseAdapter(filterList);
        recyclerView.setAdapter(browseAdapter);

    }


}