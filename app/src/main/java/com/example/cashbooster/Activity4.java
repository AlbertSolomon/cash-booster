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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Activity4 extends AppCompatActivity {
    //RecyclerView displayData;
    TextView displayData;
    Button playGame;
    //String TAG;

    CollectionReference GamePortals;
    String collectionName;
    //String documentIdUser;

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

        //DocumentReference CurrentUserDocument;
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser;
        {
            currentUser = mFirebaseAuth.getCurrentUser().getUid();
            //CurrentUserDocument = db.document(currentUser);
        }

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SelectWinners();
                //displayResults(currentUser);

                GamePortals.document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                String AdministratorRPermission = String.valueOf(document.get("Permission"));
                                String setToPlay = String.valueOf(document.get("GameStart"));

                                if(AdministratorRPermission.equals("A")  && setToPlay.equals("Ready")){

                                    SelectWinners(currentUser);

                                }else if(setToPlay.equals("Ready")) displayResults(currentUser);
                            }
                        }
                    }
                });

            }
        });
    }

    public void SelectWinners(String userID){

        GamePortals.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    ArrayList<Integer> numberPot;
                    numberPot = new ArrayList<>();
                    SecureRandom random;
                    random = new SecureRandom();

                    for (QueryDocumentSnapshot document: task.getResult()){

                        //displayData.setText(String.valueOf(document.get("GameCode")));
                        numberPot.add(Integer.parseInt(String.valueOf(document.get("GameCode"))));

                    }
                    //shuffle(numberPot);
                    ArrayList<Integer> gameWinners;
                    gameWinners = new ArrayList<>();

                    //displayData.setText(valueOf(numberPot.get(random.nextInt(numberPot.size()))));
                    for(int i= 0; i< numberPot.size() - 1; i++){

                        boolean toGameWinners,gameLoser;
                        //int randomPicker = random.nextInt(numberPot.size());
                        //int randomIndex = random.nextInt();

                        //toGameWinners = gameWinners.add(numberPot.get(randomIndex );
                        //gameLoser = numberPot.remove(numberPot.get(randomIndex );

                        toGameWinners = gameWinners.add(numberPot.get(random.nextInt(numberPot.size())));
                        gameLoser = numberPot.remove(numberPot.get(random.nextInt(numberPot.size())));

                        if(toGameWinners == gameLoser){
                            gameWinners.add(numberPot.get(random.nextInt(numberPot.size())));
                            numberPot.remove(numberPot.get(random.nextInt(numberPot.size())));
                        }
                    }
                        for(int counter = 1; counter < 5; counter++){
                            GamePortals.whereEqualTo("GameCode", gameWinners.get(counter -1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document: task.getResult()){
                                                if (gameWinners.size() < 5){

                                                    Map<String, Object> updateWinner = new HashMap<>();
                                                    updateWinner.put("GameState","Winner");
                                                    GamePortals.document(document.getId()).update(updateWinner);
                                                    Toast.makeText(getApplicationContext(),"Winners updated", Toast.LENGTH_SHORT).show();

                                                }else{
                                                    /*Map<String, Object> updateLoser = new HashMap<>();
                                                    updateLoser.put("GameState","Loser");
                                                    GamePortals.document(document.getId()).update(updateLoser);
                                                    Toast.makeText(getApplicationContext(),"Loser(s) updated", Toast.LENGTH_SHORT).show();*/
                                                }
                                            }

                                    }else {
                                        Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    //displayData.setText(valueOf(gameWinners.get(1)));
                    //displayData.setText(valueOf(numberPot.size()));
                    //displayData.setText(valueOf(gameWinners.size()));
                    displayResults(userID);

                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayResults(String userID){
        GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //use a pop up alert to display if won or lost
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()){
                        displayData.setText(String.valueOf(document.get("GameState")));
                    }else{
                        Toast.makeText(getApplicationContext(),"Game in Progress !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}