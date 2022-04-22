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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    //Definition
    private EditText emailEditText;
    private TextView title;
    private Button resetPassword;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Email bar
        emailEditText = (EditText) findViewById(R.id.email);
        //Reset password bar
        resetPassword = (Button) findViewById(R.id.resetPassword);
        //Progressbar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //App title
        title = (TextView) findViewById(R.id.title);
        //Firebase call
        mAuth = FirebaseAuth.getInstance();
        //Make title clickable
        title.setOnClickListener(this);
        //Make Reset Password button clickable
        resetPassword.setOnClickListener(this);
    }

    //resetPassword function
    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        //Check email is inputted
        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        //Check email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        //Progressbar set
        progressBar.setVisibility(View.VISIBLE);
        //Firebase password reset function
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Send link to reset password
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset password!", Toast.LENGTH_LONG).show();
                } else {
                    //In case of error
                    Toast.makeText(ForgotPassword.this, "Something went wrong, try again!", Toast.LENGTH_LONG).show();
                }
                //Disable progressbar
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetPassword:
                //Reset password if clicked of reset password button
                resetPassword();
                break;
            case R.id.title:
                //Redirect to login screen
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}