package com.example.cashbooster;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.String.valueOf;

public class Activity4 extends AppCompatActivity {
    //RecyclerView displayData;
    TextView displayData;
    Button playGame;
    String TAG;

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

        //FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        /*String currentUser;
        {
            currentUser = mFirebaseAuth.getCurrentUser().getUid();
        }*/

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWinners();
            }
        });
    }

    public void SelectWinners(){

        GamePortals.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    ArrayList<ArrayList<String>> numberPot;

                    numberPot = new ArrayList<>();
                    Random random = new Random();
                    int randomPicker = random.nextInt();
                    //int randomPicker = (int) (Math.random()*4);

                    for (QueryDocumentSnapshot document: task.getResult()){
                        //displayData.setText(String.valueOf(document.get("GameCode")));


                        //numberPot.add((Integer) document.get("GameCode"));
                        numberPot.add((ArrayList<String>) document.get("GameCode"));
                        //displayData.setText(valueOf(numberPot.size()));
                        //Log.d(TAG,"onCreate: numbers:" + numberPot.size());

                        //numberPot.get(randomPicker);
                        //displayData.setText(String.valueOf(numberPot));
                        //displayData.setText(String.valueOf(numberPot.get(randomPicker)));
                        //displayData.setText(numberPot.get(randomPicker));
                       // Log.d(TAG,"onCreate: numbers:" + numberPot.get(randomPicker));

                        /*GamePortals.whereEqualTo("GameCode", numberPot.get(randomPicker))
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    document.getReference().update("GameState","Winner");
                                    Toast.makeText(getApplicationContext(),"Updating was successful!", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Toast.makeText(getApplicationContext(),"Updating was not successful!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/
                    }
                    displayData.setText(valueOf(numberPot.size()));

                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}