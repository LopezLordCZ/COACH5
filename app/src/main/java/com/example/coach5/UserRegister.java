package com.example.coach5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    private TextView title, register;
    private EditText editTextName, editTextSurname, editTextAge, editTextEmail, editTextPassword, editTextRepeatPassword;
    private RadioButton user, coach;
    private ProgressBar progressBar;

    private DatabaseReference reference, referenceCoach;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        referenceCoach = FirebaseDatabase.getInstance().getReference("Coaches");

        title = (TextView) findViewById(R.id.title);
        title.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.userName);
        editTextSurname = (EditText) findViewById(R.id.userSurname);
        editTextAge = (EditText) findViewById(R.id.userAge);
        editTextEmail = (EditText) findViewById(R.id.userEmail);
        editTextPassword = (EditText) findViewById(R.id.userPassword);
        editTextRepeatPassword = (EditText) findViewById(R.id.userRepeatPassword);

        user = (RadioButton) findViewById(R.id.User);
        coach = (RadioButton) findViewById(R.id.Coach);

        progressBar = (ProgressBar) findViewById(R.id.registerLoading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repeatPassword = editTextRepeatPassword.getText().toString().trim();
        String sport1 = "Null";
        String sport2 = "Null";
        String sport3 = "Null";
        String sport1Skill = "Null";
        String sport2Skill = "Null";
        String sport3Skill = "Null";
        String location = "Null";
        String price = "Null";
        //List<String> contacts = new ArrayList<>();
        final String[] accountType = {null};

        if (name.isEmpty()) {
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        if (surname.isEmpty()) {
            editTextSurname.setError("Surname is required!");
            editTextSurname.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            editTextPassword.setError("Password must contain at least 8 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() > 16) {
            editTextPassword.setError("Password must contain at most 16 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (repeatPassword.isEmpty()) {
            editTextRepeatPassword.setError("Repeat the password!");
            editTextRepeatPassword.requestFocus();
            return;
        }

        if (!repeatPassword.equals(password)) {
            editTextRepeatPassword.setError("Passwords does not match!");
            editTextRepeatPassword.requestFocus();
        }

        if (user.isChecked()) {
            accountType[0] = "User";
        } else if (coach.isChecked()) {
            accountType[0] = "Coach";
        }

        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editTextEmail.setError("Email already taken");
                    editTextEmail.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceCoach.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editTextEmail.setError("Email already taken");
                    editTextEmail.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressBar.setVisibility(View.VISIBLE);
        String finalAccountType = accountType[0];
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (accountType[0].equals("User")) {
                            if (task.isSuccessful()) {
                                User user = new User(finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserRegister.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            //Back to login
                                        } else {
                                            Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else if (accountType[0].equals("Coach")) {
                            if (task.isSuccessful()) {
                                Coach coach = new Coach(finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location, price);

                                FirebaseDatabase.getInstance().getReference("Coaches")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(coach).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserRegister.this, "Coach has been successfully registered", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            //Back to login
                                        } else {
                                            Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}