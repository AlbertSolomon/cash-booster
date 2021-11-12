package com.example.cashbooster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {
    Button loginButton, signupButton;
    EditText PasswordText, userName;
    ProgressBar firstProgressBar;

    //Integer loginValue = 2;

    SharedPreferences loginCredentials;

    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //getSharedPreferences("credentials", Context.MODE_PRIVATE);

        loginButton= findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        userName= findViewById(R.id.userName);
        PasswordText= findViewById(R.id.PasswordText);
        firstProgressBar = findViewById(R.id.firstProgressBar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener myAuthListener;


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                String password = PasswordText.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your email", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 5){
                    Toast.makeText(getApplicationContext(),"Your password should contain at least 5 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    firstProgressBar.setVisibility(View.VISIBLE);
                    openNextActivity(username, password);
                    //execute service, to keep users logged in
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingUp();
            }
        });
    }

    public void openNextActivity(String username, String password){

        mFirebaseAuth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(SignInActivity.this, LandingActivity.class));
                firstProgressBar.setVisibility(View.GONE);

                /*SharedPreferences.Editor editor = loginCredentials.edit();
                editor.putString("Email",username);
                editor.putString("password",password);
                editor.putInt("loginValue",loginValue = 1);
                editor.apply();*/

                finish();
                Toast.makeText(SignInActivity.this, "Login successful!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(SignInActivity.this, "Login Failed because of a network issue!",Toast.LENGTH_SHORT).show();
                firstProgressBar.setVisibility(View.GONE);
            }
        });

    }

    public void signingUp(){
        Intent intent = new Intent(SignInActivity.this,Activity3.class);
        firstProgressBar.setVisibility(View.GONE);
        startActivity(intent);
    }

}