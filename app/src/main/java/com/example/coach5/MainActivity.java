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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Definition
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

    //On creation
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register button
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        //Forgot password button
        forgotPassword = (TextView) findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(this);

        //Login button
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        //Email text bar
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        //Progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //User radio button
        userButton = (RadioButton) findViewById(R.id.User);
        //Coach radio button
        coachButton = (RadioButton) findViewById(R.id.Coach);

        //Remember me checkbox
        mCheckBoxRemember = (CheckBox) findViewById(R.id.Remember);

        //Connection to firebase
        mAuth = FirebaseAuth.getInstance();

        //Check for system preferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //Refer to Users path in firebase
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //Refer to Coaches path in firebase
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

    //closeNow function
    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    //Connection to LocationService class
    void startService () {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
    }

    //ClickRegister function
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register:
                //Redirect to user registration
                startActivity(new Intent(this, UserRegister.class));
                break;
            case R.id.login:
                //Call the userLogin function
                userLogin();
                break;
            case R.id.forgotpassword:
                //Redirect to forgot password
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    //userLogin function
    private void userLogin() {
        //Email bar
        String email = editTextEmail.getText().toString().trim();
        //Password bar
        String password = editTextPassword.getText().toString().trim();

        //Check if email is not empty
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        //Checks if the email is a valid email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        //Checks if password was inputted
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        //Checks that the password length is at least 8
        if (password.length() < 8) {
            editTextPassword.setError("Min. password length is 8 characters!");
            editTextPassword.requestFocus();
            return;
        }

        //Activates the progress bar visibility
        progressBar.setVisibility(View.VISIBLE);

        //Commence if "user" radio button is checked - User login
        if (userButton.isChecked()) {
            //Firebase signIn function
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //If sign in successful
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //Check if email is verified
                        if(user.isEmailVerified()) {
                            //Check coach accounts for the inserted email
                            referenceCoach.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Notify user it is a coach account
                                    if (snapshot.exists()) {
                                        editTextEmail.setError("This email is registered as coach account");
                                        editTextEmail.requestFocus();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        //Remember details if "Remember Me" checkbox is ticked in case it is not a coach account
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
                                            Toast.makeText(getApplicationContext(), "Login details remembered", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //"Remember Me" not ticked
                                            mPrefs.edit().clear().apply();
                                        }
                                        //Redirect user to the homepage
                                        startActivity(new Intent(MainActivity.this, Homescreen.class));
                                    }
                                }

                                //Autogenerated with the onDataChange function
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            //Send verification to the users email address
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        //In case the user login fails
                        Toast.makeText(MainActivity.this, "Failed to login, try again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            //Commence if "coach" radio button is checked - Coach login
        } else if (coachButton.isChecked()) {
            //Firebase signIn function
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //If sign in successful
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //Check if email is verified
                        if(user.isEmailVerified()) {
                            //Check user accounts for the inserted email
                            reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Notify coach it is a user account
                                    if (snapshot.exists()) {
                                        editTextEmail.setError("This email is registered as user account");
                                        editTextEmail.requestFocus();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        //Remember details if "Remember Me" checkbox is ticked in case it is not a user account
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
                                            //"Remember Me" not ticked
                                            mPrefs.edit().clear().apply();
                                        }
                                        //Redirect user to the homepage
                                        startActivity(new Intent(MainActivity.this, HomescreenCoach.class));
                                    }
                                }

                                //Autogenerated with the onDataChange function
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            //Send verification to the coach email address
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        //In case the coach login fails
                        Toast.makeText(MainActivity.this, "Failed to login, try again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    //Get preferences of the given user
    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //User radio button
        if(sp.contains("pref_user")) {
            Boolean acc = sp.getBoolean("pref_user", false);
            userButton.setChecked(acc);
        }
        //Coach radio button
        if(sp.contains("pref_coach")) {
            Boolean acc = sp.getBoolean("pref_coach", false);
            coachButton.setChecked(acc);
        }
        //Email baf
        if (sp.contains("pref_name")) {
            String u = sp.getString("pref_name", "not found.");
            editTextEmail.setText(u.toString());
        }
        //Password bar
        if (sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "not found");
            editTextPassword.setText(p.toString());
        }
        //Remember Me checkbox
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }
    }
}