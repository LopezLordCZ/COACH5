package com.example.coach5;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profilescreen extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private Button save;
    private DatabaseReference reference;
    private String fAccount;
    private String fSurname;
    private String fEmail;
    private String fLocation;
    //get current user
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);

        //create the fields
        TextView current_name = findViewById(R.id.userName);
        TextView current_age = findViewById(R.id.age);
        Spinner sport1 = findViewById(R.id.Sport1);
        Spinner sport2 = findViewById(R.id.Sport2);
        Spinner sport3 = findViewById(R.id.Sport3);
        Spinner skill_level1 = findViewById(R.id.skill_level1);
        Spinner skill_level2 = findViewById(R.id.skill_level2);
        Spinner skill_level3 = findViewById(R.id.skill_level3);

        // Create ArrayAdapters using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSport = ArrayAdapter.createFromResource(this,
                R.array.Sports, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSkill_level = ArrayAdapter.createFromResource(this,
                R.array.Skill_levels, android.R.layout.simple_spinner_item);

        // Specify the layouts to use when the list of choices appears
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSkill_level.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        sport1.setAdapter(adapterSport);
        sport2.setAdapter(adapterSport);
        sport3.setAdapter(adapterSport);
        skill_level1.setAdapter(adapterSkill_level);
        skill_level2.setAdapter(adapterSkill_level);
        skill_level3.setAdapter(adapterSkill_level);

        if (user != null) {
            //email address of current user
            String email = user.getEmail();

            //get database reference
            reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        User info = snapshot.getValue(User.class);
                        String userEmail = info.email;
                        Log.d("firebase", userEmail);
                        //make sure reference is same as current user
                        if (info.email.equals(email)){
                            //save user specific info
                            fAccount = info.finalAccountType;
                            fSurname = info.surname;
                            fEmail = info.email;
                            fLocation = info.location;

                            //set the front end name+age
                            current_name.setText(info.name);
                            current_age.setText(info.age);

                            //set spinners
                            if (info.sport1.equals("Null")){
                                //do noting, not filled in field
                            }else{
                                sport1.setSelection(adapterSport.getPosition(info.sport1));
                            }
                            if (info.sport2.equals("Null")){
                                //do noting, not filled in field
                            }else{
                                sport2.setSelection(adapterSport.getPosition(info.sport2));
                            }
                            if (info.sport3.equals("Null")){
                                //do noting, not filled in field
                            }else{
                                sport3.setSelection(adapterSport.getPosition(info.sport3));
                            }
                            if (info.sport1Skill.equals("Null")){
                                //do noting, not filled in field
                            }else{
                                skill_level1.setSelection(adapterSkill_level.getPosition(info.sport1Skill));
                            }
                            if (info.sport2Skill.equals("Null")){
                                //do noting, not filled in field
                            }else{
                                skill_level2.setSelection(adapterSkill_level.getPosition(info.sport2Skill));
                            }
                            if (info.sport3Skill.equals("Null")){
                                //do noting, not filled in field
                            }else{
                                skill_level3.setSelection(adapterSkill_level.getPosition(info.sport3Skill));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        save = (Button) findViewById(R.id.profile);
        save.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.imageView2);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //create the fields
        TextView current_name = findViewById(R.id.userName);
        TextView current_age = findViewById(R.id.age);
        Spinner sport1 = findViewById(R.id.Sport1);
        Spinner sport2 = findViewById(R.id.Sport2);
        Spinner sport3 = findViewById(R.id.Sport3);
        Spinner skill_level1 = findViewById(R.id.skill_level1);
        Spinner skill_level2 = findViewById(R.id.skill_level2);
        Spinner skill_level3 = findViewById(R.id.skill_level3);
        switch(v.getId()) {
            case R.id.imageView2:
                //back button
                startActivity(new Intent(this, Homescreen.class));
                break;
            case R.id.profile:
                //button for saving

                //get data
                String upName = current_name.getText().toString();
                String upAge = current_age.getText().toString();
                String upSport1 = sport1.getSelectedItem().toString();
                String upSport2 = sport2.getSelectedItem().toString();
                String upSport3 = sport3.getSelectedItem().toString();
                String upSkill1 = skill_level1.getSelectedItem().toString();
                String upSkill2 = skill_level2.getSelectedItem().toString();
                String upSkill3 = skill_level3.getSelectedItem().toString();
                updateData(fAccount, upName, fSurname, upAge, fEmail, upSport1, upSport2, upSport3, upSkill1, upSkill2, upSkill3, fLocation);
                break;
        }
    }

    public void updateData(String account, String name, String surname, String age, String email, String sport1, String sport2, String sport3, String skill1, String skill2, String Skill3, String location){
        String key = user.getUid();
        User update = new User(account, name, surname, age, email, sport1, sport2,sport3, skill1, skill2, Skill3, location);
        Map<String, Object> userValues = update.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, userValues);

        reference.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Profilescreen.this, "Profile information saved!", Toast.LENGTH_SHORT).show();
                    //Back to login
                } else {
                    Toast.makeText(Profilescreen.this, "Updating information failed, try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
