package com.example.cashbooster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Activity3 extends AppCompatActivity {

    Button Activity3button;
    EditText userName3, editTextPhone, passwordActivity3, passwordActivity3Confirm;
    ProgressBar progressBarSignUp;
    private FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean DoeUserExists;

    int loginValue;
    //SharedPreferences loginCredentials;
    String AuthBucket = "AuthenticationBucket";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //android:screenOrientation="portrait" to added as an attribute in this activity in manifest

        //getSharedPreferences("credentials", Context.MODE_PRIVATE);

        Activity3button = findViewById(R.id.Activity3button);
        userName3 = findViewById(R.id.userName3);

        progressBarSignUp = findViewById(R.id.progressBarSignUp);
        //editTextPhone = findViewById(R.id.editTextPhone);
        passwordActivity3 = findViewById(R.id.passwordActivity3);
        passwordActivity3Confirm = findViewById(R.id.passwordActivity3Confirm);
        mFirebaseAuth = FirebaseAuth.getInstance();

        //email = userName3.getText().toString().trim();

        Activity3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName3.getText().toString().trim();
                //String phoneNumber = editTextPhone.getText().toString().trim();
                String password = passwordActivity3.getText().toString().trim();
                String passwordConfirm = passwordActivity3Confirm.getText().toString().trim();

                //openUserDashboard();
                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your email", Toast.LENGTH_SHORT).show();
                }else if (password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 5){
                    Toast.makeText(getApplicationContext(),"Your password should contain at least 5 characters", Toast.LENGTH_SHORT).show();
                }
                else if (passwordConfirm.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please confirm your Password", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(passwordConfirm)){
                    Toast.makeText(getApplicationContext(),"Your passwords do not much", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBarSignUp.setVisibility(View.VISIBLE);
                    RegisterUser(username, password);


                   // finish();
                }

            }
        });
    }
    public void RegisterUser(String username, String password){

        //checkEmailExistsOrNot(username);
        CheckEmailAvailability(username);

        if (!DoeUserExists){
            mFirebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(Activity3.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        Map<String, Object> authBucket = new HashMap<>();
                        authBucket.put("Email",username);
                        db.collection(AuthBucket).document().set(authBucket).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Activity3.this,"setting thing up", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(Activity3.this,"something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(Activity3.this,"Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Activity3.this, Activity2.class));

                        /*SharedPreferences.Editor editor = loginCredentials.edit();
                        editor.putString("Email",username);
                        editor.putString("password",password);
                        editor.putInt("loginValue",loginValue = 1);
                        editor.apply();*/

                        progressBarSignUp.setVisibility(View.GONE);
                        finish();

                    }else {
                        Toast.makeText(Activity3.this,"Login Failed", Toast.LENGTH_SHORT).show();
                        progressBarSignUp.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(Activity3.this,"This email already exists", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Activity3.this, SignInActivity.class));
        }
    }

    public void CheckEmailAvailability(String emailBucket){
        db.collection(AuthBucket).whereEqualTo("Email", emailBucket).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot documentSnapshots = task.getResult();

                    if (documentSnapshots.size() > 0){
                        DoeUserExists = true;
                    }else{
                        DoeUserExists = false;
                    }
                }
            }
        });
    }

}