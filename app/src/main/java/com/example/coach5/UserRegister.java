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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    private TextView title, register;
    private EditText editTextName, editTextSurname, editTextAge, editTextEmail, editTextPassword, editTextRepeatPassword;
    private RadioButton user, coach;
    private ProgressBar progressBar;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

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
        String accountType = null;

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

        if (repeatPassword.isEmpty()) {
            editTextRepeatPassword.setError("Repeat the password!");
            editTextRepeatPassword.requestFocus();
            return;
        }

        if(!repeatPassword.equals(password)) {
            editTextRepeatPassword.setError("Passwords does not match!");
            editTextRepeatPassword.requestFocus();
        }

        if(user.isChecked()) {
            accountType = "User";
        } else if (coach.isChecked()) {
            accountType = "Coach";
        }

        progressBar.setVisibility(View.VISIBLE);
        String finalAccountType = accountType;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(finalAccountType, name, surname, age, email);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserRegister.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                //Back to login
                            } else {
                                Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else{
                    Toast.makeText(UserRegister.this, "Registration failed, try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}