package com.example.cashbooster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {


    String PasswordText, userEmail;
    Integer loginValue = 0;
    ProgressBar launcherProgressBar;
    ImageView imageView;

    //Boolean login;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener myAuthListener;

        //launcherProgressBar = findViewById(R.id.launcherProgressBar);
        //imageView = findViewById(R.id.imageView);

        /*SharedPreferences testSp = getApplicationContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        userEmail = testSp.getString("Email","");
        PasswordText = testSp.getString("password","");
        loginValue = testSp.getInt("loginValue",loginValue );*/

        //launcherProgressBar.setVisibility(View.VISIBLE);
        NextActivity();
       // imageView.setVisibility(View.VISIBLE);

        /*if(loginValue == 0){
            login = false;
        }else {
            login = true;
        }

        if (login){
            launcherProgressBar.setVisibility(View.VISIBLE);
            openNextActivity(userEmail,PasswordText);

        }else{
            try {

                launcherProgressBar.setVisibility(View.VISIBLE);
                Thread.sleep(5000);
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();

            }catch (Exception e){
                e.printStackTrace();
            }
        }*/


    }

    /*public void openNextActivity(String username, String password){

        mFirebaseAuth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(MainActivity.this, LandingActivity.class));
                launcherProgressBar.setVisibility(View.GONE);
                finish();
                Toast.makeText(MainActivity.this, "Login successful!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(MainActivity.this, "Login Failed because of a network issue!",Toast.LENGTH_SHORT).show();
                launcherProgressBar.setVisibility(View.GONE);

            }
        });

    }*/

    public void NextActivity(){
        Runnable aQualification = new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(5000);
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    //launcherProgressBar.setVisibility(View.GONE);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        Thread ArenaQBgThread = new Thread(aQualification);
        ArenaQBgThread.start();
    }
}