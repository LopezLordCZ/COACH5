package com.example.coach5;

import android.content.Intent;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private RadioButton userButton, coachButton;
    private Button login;
    private CheckBox mCheckBoxRemember;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    private FirebaseAuth mAuth;

    private DatabaseReference reference;
    private DatabaseReference referenceCoach;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        userButton = (RadioButton) findViewById(R.id.User);
        coachButton = (RadioButton) findViewById(R.id.Coach);

        mCheckBoxRemember = (CheckBox) findViewById(R.id.Remember);

        mAuth = FirebaseAuth.getInstance();

        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        referenceCoach = FirebaseDatabase.getInstance().getReference("Coaches");
        
        getPreferencesData();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //request Location Permission
                startService();
            }
        } else {
            //Start the Location Service
            startService();
        }
    }

    //App will close if location access denied

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                closeNow();
            }
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

        void startService () {
            Intent intent = new Intent(MainActivity.this, LocationService.class);
            startService(intent);
        }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, UserRegister.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotpassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            editTextPassword.setError("Min. password length is 8 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (userButton.isChecked()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()) {
                            referenceCoach.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        editTextEmail.setError("This email is registered as coach account");
                                        editTextEmail.requestFocus();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        if (mCheckBoxRemember.isChecked()) {
                                            Boolean boolIsChecked = mCheckBoxRemember.isChecked();
                                            Boolean boolUserIsChecked = userButton.isChecked();
                                            Boolean boolCoachIsChecked = coachButton.isChecked();
                                            SharedPreferences.Editor editor = mPrefs.edit();
                                            editor.putBoolean("pref_user", boolUserIsChecked);
                                            editor.putBoolean("pref_coach", boolCoachIsChecked);
                                            editor.putString("pref_name", editTextEmail.getText().toString());
                                            editor.putString("pref_pass", editTextPassword.getText().toString());
                                            editor.putBoolean("pref_check", boolIsChecked);
                                            editor.apply();
                                            Toast.makeText(getApplicationContext(), "Settings has been saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mPrefs.edit().clear().apply();
                                        }
                                        startActivity(new Intent(MainActivity.this, Homescreen.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to login, try again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else if (coachButton.isChecked()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()) {
                            reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        editTextEmail.setError("This email is registered as user account");
                                        editTextEmail.requestFocus();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        if (mCheckBoxRemember.isChecked()) {
                                            Boolean boolIsChecked = mCheckBoxRemember.isChecked();
                                            Boolean boolUserIsChecked = userButton.isChecked();
                                            Boolean boolCoachIsChecked = coachButton.isChecked();
                                            SharedPreferences.Editor editor = mPrefs.edit();
                                            editor.putBoolean("pref_user", boolUserIsChecked);
                                            editor.putBoolean("pref_coach", boolCoachIsChecked);
                                            editor.putString("pref_name", editTextEmail.getText().toString());
                                            editor.putString("pref_pass", editTextPassword.getText().toString());
                                            editor.putBoolean("pref_check", boolIsChecked);
                                            editor.apply();
                                            Toast.makeText(getApplicationContext(), "Settings has been saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mPrefs.edit().clear().apply();
                                        }
                                        startActivity(new Intent(MainActivity.this, HomescreenCoach.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to login, try again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.contains("pref_user")) {
            Boolean acc = sp.getBoolean("pref_user", false);
            userButton.setChecked(acc);
        }
        if(sp.contains("pref_coach")) {
            Boolean acc = sp.getBoolean("pref_coach", false);
            coachButton.setChecked(acc);
        }
        if (sp.contains("pref_name")) {
            String u = sp.getString("pref_name", "not found.");
            editTextEmail.setText(u.toString());
        }
        if (sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "not found");
            editTextPassword.setText(p.toString());
        }
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }
    }
}