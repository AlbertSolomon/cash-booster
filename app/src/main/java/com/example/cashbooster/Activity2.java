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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Activity2 extends AppCompatActivity {

    Button activity2Button, searchPortal, goToActivity4;
    TextView textView2, CashBalanceDisplay;

    FirebaseAnalytics TestFirebaseAnalytics;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Accounts;

    String collectionName;

    CollectionReference GamePortals;
    Random randomNumber = new Random();
    int gameRange;
    int luckyNumber;
    //int nameConcatenation;
    String permission;
    String GameState;
    String GameStart;
    String AccountsCollectionName = "Accounts";
    {
        collectionName = "GamePortals";
        GamePortals = db.collection(collectionName);
        //nameConcatenation = randomNumber.nextInt(100000 - 10000) + 1;
        luckyNumber = randomNumber.nextInt(9999-100) + 1;
        gameRange = 2000;
        permission = "A";
        GameStart = "None";
        GameState = "None";
        Accounts = db.collection(AccountsCollectionName);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        FirebaseApp.initializeApp(this);

        activity2Button = findViewById(R.id.activity2Button);
        textView2 = findViewById(R.id.textView2);
        CashBalanceDisplay = findViewById(R.id.CashBalanceDisplay);
        searchPortal = findViewById(R.id.searchPortal);
        goToActivity4 = findViewById(R.id.goToActivity4);

        TestFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getCurrentUser().getUid();

        getAccountBalance(currentUser);

        db.collection("GamePortals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();

                    if (numberOfDocuments == 0){
                        String permission = "A";
                        activity2Button.setOnClickListener(view -> {
                            insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState);
                        });

                    }else if(numberOfDocuments < 5){
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

                openActivity4(currentUser);

            }
        });


    }

    public void insertDataDocuments(String UserID, String permission, int Amount, int gameCode, String gameStart,String gameState){
        Map<String, Object>gamePortal = new HashMap<>();
        gamePortal.put("UserID",UserID);
        gamePortal.put("Permission",permission);
        gamePortal.put("Amount",Amount);
        gamePortal.put("GameCode",gameCode);
        gamePortal.put("GameStart",gameStart);
        gamePortal.put("GameState",gameState);
        //gamePortal.put("Created", Timestamp.now());

        db.collection("GamePortals").document(UserID).set(gamePortal).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void openActivity4(String userID){

        GamePortals.document(userID).update("GameStart","Ready").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"user is Ready !!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(),"waiting for user to be Ready !!!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(Activity2.this,Activity4.class);
        startActivity(intent);
        finish();
    }

    public void AccountCollection(String UserID, int AccountBalance){
        Map<String, Object>accountCollection = new HashMap<>();
        accountCollection.put("UserID",UserID);
        accountCollection.put("AmountBalance", AccountBalance);

        Accounts.document(UserID).set(accountCollection).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void getAccountBalance(String UserID){
        Accounts.document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        CashBalanceDisplay.setText(String.valueOf(document.get("AmountBalance")));
                    }else {
                        CashBalanceDisplay.setText("N/A");
                        Toast.makeText(getApplicationContext(),"User Account Document does not exist!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*public void readyToPlay(String userID){
        GamePortals.whereEqualTo("UserID",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot document = task.getResult();
                    Map<String, Object> readyToStart = new HashMap<>();
                    readyToStart.put("GameStart","True");

                    GamePortals.document()
                }
            }
        });
    }*/
}