package com.example.cashbooster;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class Activity4 extends AppCompatActivity {
    RecyclerView displayData;
    Button playGame;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    CollectionReference GamePortals;
    String collectionName;

    FirebaseFirestore db;
    {
        db = FirebaseFirestore.getInstance();
        collectionName = "GamePortals";
        GamePortals = db.collection(collectionName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        displayData = findViewById(R.id.displayData);
        playGame = findViewById(R.id.playGame);



        String currentUser;
        {

            currentUser = mFirebaseAuth.getCurrentUser().getUid();
        }

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWinners();
            }
        });
    }

    public void SelectWinners(){

        GamePortals.whereLessThan("GameCode",10000)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){

                         //displayData.setText(String.valueOf(document.getData()));
                        document.getId();

                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}