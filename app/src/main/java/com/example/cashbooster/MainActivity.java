package com.example.cashbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    Button button, signupButton;
    EditText PasswordText, userName;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        signupButton = (Button)findViewById(R.id.signupButton);
        userName=(EditText)findViewById(R.id.userName);
        PasswordText=(EditText)findViewById(R.id.PasswordText);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //final EditText PasswordText = (EditText)findViewById(R.id.PasswordText);
        button.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(),"Your password should contain atleast 5 characters", Toast.LENGTH_SHORT).show();
                }
                else {

                    openNextActivity(username, password);
                    //Intent intent = new Intent(MainActivity.this,Activity2.class);
                    //startActivity(intent);
                    //finish();

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

       /* mFirebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Activity2.class));
                }else {
                    Toast.makeText(MainActivity.this,"Login failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        mFirebaseAuth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login successful!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Activity2.class));
                finish();
            }
        });
    }

    public void signingUp(){
        Intent intent = new Intent(MainActivity.this,Activity3.class);
        startActivity(intent);
    }
}