package com.example.cashbooster;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    Button loginButton, signupButton;
    EditText PasswordText, userName;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loginButton= findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        userName= findViewById(R.id.userName);
        PasswordText= findViewById(R.id.PasswordText);

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
                startActivity(new Intent(MainActivity.this, LandingActivity.class));
                finish();
                Toast.makeText(MainActivity.this, "Login successful!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(MainActivity.this, "Login Failed because of a network issue!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void signingUp(){
        Intent intent = new Intent(MainActivity.this,Activity3.class);
        startActivity(intent);
    }
}