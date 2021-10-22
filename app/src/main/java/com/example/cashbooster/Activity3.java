package com.example.cashbooster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity3 extends AppCompatActivity {

    Button Activity3button;
    EditText userName3, editTextPhone, passwordActivity3, passwordActivity3Confirm;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Activity3button = findViewById(R.id.Activity3button);
        userName3 = findViewById(R.id.userName3);
        //editTextPhone = findViewById(R.id.editTextPhone);
        passwordActivity3 = findViewById(R.id.passwordActivity3);
        passwordActivity3Confirm = findViewById(R.id.passwordActivity3Confirm);
        mFirebaseAuth = FirebaseAuth.getInstance();


        Activity3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName3.getText().toString().trim();
                String phoneNumber = editTextPhone.getText().toString().trim();
                String password = passwordActivity3.getText().toString().trim();
                String passwordConfirm = passwordActivity3Confirm.getText().toString().trim();

                //openUserDashboard();
                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your user name", Toast.LENGTH_SHORT).show();
                }else if (password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 5){
                    Toast.makeText(getApplicationContext(),"Your password should contain atleast 5 characters", Toast.LENGTH_SHORT).show();
                }
                else if (passwordConfirm.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please confirm your Password", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(passwordConfirm)){
                    Toast.makeText(getApplicationContext(),"Your passwords do not much", Toast.LENGTH_SHORT).show();
                }
                else {
                    RegisterUser(username, password);
                   // finish();
                }

            }
        });
    }
    public void RegisterUser(String username, String password){
        mFirebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(Activity3.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Activity3.this,"Login successful!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Activity3.this, Activity2.class));
                    finish();

                }else {
                    Toast.makeText(Activity3.this,"Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}