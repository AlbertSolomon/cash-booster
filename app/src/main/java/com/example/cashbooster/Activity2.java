package com.example.cashbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Activity2 extends AppCompatActivity {

    Button activity2Button, searchPortal, goToActivity4;
    TextView textView2;

    FirebaseAnalytics TestFirebaseAnalytics;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //String collectionName;

    //CollectionReference GamePortals;
    Random randomNumber = new Random();
    int gameRange;
    int luckyNumber;
    int nameConcatenation;
    String permission;
    String GameState;
    String GameStart;
    {
        //collectionName = "GamePortals"+nameConcatenation;
        //GamePortals = db.collection(collectionName);
        nameConcatenation = randomNumber.nextInt(100000 - 10000) + 1;
        luckyNumber = randomNumber.nextInt(9999-100) + 1;
        gameRange = 2000;
        permission = "A";
        GameStart = "None";
        GameState = "None";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        FirebaseApp.initializeApp(this);

        activity2Button = findViewById(R.id.activity2Button);
        textView2 = findViewById(R.id.textView2);
        searchPortal = findViewById(R.id.searchPortal);
        goToActivity4 = findViewById(R.id.goToActivity4);

        TestFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getCurrentUser().getUid();

        db.collection("GamePortals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();

                    if(numberOfDocuments < 5){
                        String permission = "F";
                        activity2Button.setOnClickListener(view -> {
                            insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState);
                        });

                    }else{
                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(),"Could not count the number of documents in GamePortals", Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPortals();
            }
        });

        goToActivity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity4();
            }
        });


    }

    public void insertDataDocuments(String UserID, String permission, int Amount, int gameCode, String gameStart, String gameState){
        Map<String, Object>gamePortal = new HashMap<>();
        gamePortal.put("UserID",UserID);
        gamePortal.put("Permission",permission);
        gamePortal.put("Amount",Amount);
        gamePortal.put("GameCode",gameCode);
        gamePortal.put("GameStart",gameStart);
        gamePortal.put("GameState",gameState);
        gamePortal.put("Created", Timestamp.now());

        db.collection("GamePortals").document("Portal"+nameConcatenation).set(gamePortal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getApplicationContext(),"DocumentSnapshot successfully written", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

                Toast.makeText(getApplicationContext(),"DocumentSnapshot failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchPortals(){
        db.collection("GamePortals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();
                    textView2.setText(String.valueOf(numberOfDocuments));
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                    Toast.makeText(getApplicationContext(),"Could not count the number of documents in GamePortals", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void openActivity4(){
        Intent intent = new Intent(Activity2.this,Activity4.class);
        startActivity(intent);
    }

}