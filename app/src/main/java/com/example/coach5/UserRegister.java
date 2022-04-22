package com.example.coach5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    //Definition
    private TextView title, register;
    private EditText editTextName, editTextSurname, editTextAge, editTextEmail, editTextPassword, editTextRepeatPassword;
    private RadioButton user, coach;
    private ProgressBar progressBar;
    private DatabaseReference reference, referenceCoach;
    private FirebaseAuth mAuth;

    //On creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //Firebase connection
        mAuth = FirebaseAuth.getInstance();
        //Firebase user path
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //Firebase coach path
        referenceCoach = FirebaseDatabase.getInstance().getReference("Coaches");
        //Title sign
        title = (TextView) findViewById(R.id.title);
        title.setOnClickListener(this);
        //Register button
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        //Name bar
        editTextName = (EditText) findViewById(R.id.userName);
        //Surname bar
        editTextSurname = (EditText) findViewById(R.id.userSurname);
        //Age bar
        editTextAge = (EditText) findViewById(R.id.userAge);
        //Email bar
        editTextEmail = (EditText) findViewById(R.id.userEmail);
        //Password bar
        editTextPassword = (EditText) findViewById(R.id.userPassword);
        //Repeat password bar
        editTextRepeatPassword = (EditText) findViewById(R.id.userRepeatPassword);
        //User radio button
        user = (RadioButton) findViewById(R.id.User);
        //Coach radio button
        coach = (RadioButton) findViewById(R.id.Coach);
        //Progress bar
        progressBar = (ProgressBar) findViewById(R.id.registerLoading);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //
            case R.id.title:
                //Redirect to login
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                //registerUser function
                registerUser();
                break;
        }
    }

    //Register function called by register button
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
        Double lat = 0.0;
        Double lng = 0.0;
        String price = "Null";
        final String[] accountType = {null};

        //Check name was inputted
        if (name.isEmpty()) {
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        //Check surname was inputted
        if (surname.isEmpty()) {
            editTextSurname.setError("Surname is required!");
            editTextSurname.requestFocus();
            return;
        }

        //Check age was inputted
        if (age.isEmpty()) {
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }

        //Check email is inputted
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        //Check email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        //Check password is inputted
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        //Checks password is minimum 8 characters
        if (password.length() < 8) {
            editTextPassword.setError("Password must contain at least 8 characters");
            editTextPassword.requestFocus();
            return;
        }

        //Checks password is maximum 16 characters
        if (password.length() > 16) {
            editTextPassword.setError("Password must contain at most 16 characters");
            editTextPassword.requestFocus();
            return;
        }

        //Check repeatPassword was inputted
        if (repeatPassword.isEmpty()) {
            editTextRepeatPassword.setError("Repeat the password!");
            editTextRepeatPassword.requestFocus();
            return;
        }

        //Check if password and repeat password match
        if (!repeatPassword.equals(password)) {
            editTextRepeatPassword.setError("Passwords does not match!");
            editTextRepeatPassword.requestFocus();
        }

        //Define account type
        if (user.isChecked()) {
            accountType[0] = "User";
        } else if (coach.isChecked()) {
            accountType[0] = "Coach";
        }

        //Check email was not yet registered as a user
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

        //Check email was not yet registered as a coach
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

        //Make progress bar visible
        progressBar.setVisibility(View.VISIBLE);
        String finalAccountType = accountType[0];
        //Firebase user/coach create function
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If registered account is user
                        if (accountType[0].equals("User")) {
                            //If registration is successful
                            if (task.isSuccessful()) {
                                //Create new instance of user in the database
                                User user = new User(finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location, lat, lng);

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
                                        //Stop progressbar
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                //If registration failed
                                Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            //If registered account is coach
                        } else if (accountType[0].equals("Coach")) {
                            //If registration is successful
                            if (task.isSuccessful()) {
                                //Create new instance of coach in the database
                                Coach coach = new Coach(finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location, price, lat, lng);

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
                                        //Stop progressbar
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                //If registration failed
                                Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}