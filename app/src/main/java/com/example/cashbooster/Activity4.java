package com.example.cashbooster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class Activity4 extends AppCompatActivity {
    TextView displayData, displayBalance, displayCode;
    Button playGame;
    //String TAG;

    CollectionReference GamePortals, Accounts;
    String collectionName;
    String AccountsCollectionName = "Accounts";

    FirebaseFirestore db;
    {
        db = FirebaseFirestore.getInstance();
        collectionName = "GamePortals";
        GamePortals = db.collection(collectionName);
        Accounts = db.collection(AccountsCollectionName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        displayData = findViewById(R.id.displayData);
        playGame = findViewById(R.id.playGame);
        displayBalance = findViewById(R.id.displayBalance);
        displayCode = findViewById(R.id.displayCode);

        //DocumentReference CurrentUserDocument;
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser;
        {
            currentUser = mFirebaseAuth.getCurrentUser().getUid();

        }

        getAccountBalance(currentUser);
        getGameCode(currentUser);
        //playGameVisible(currentUser); //new
        //autoSelectWinnersOnIdeal(currentUser);


        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SelectWinners();
                //displayResults(currentUser);

                //Checking for user permission (A stand for Admin anf F for follower
                //A user with the permission of A (mobile will process selection of winners.
                GamePortals.document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                String AdministratorRPermission = valueOf(document.get("Permission"));
                                String setToPlay = valueOf(document.get("GameStart"));

                                if(AdministratorRPermission.equals("A")  && setToPlay.equals("Ready")){

                                    SelectWinners(currentUser);
                                    /*GamePortals.whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                int numberOfWinners = task.getResult().size();

                                                if (numberOfWinners == 4){

                                                    updateSystemAccount(currentUser);
                                                    enterArenas();
                                                }
                                            }
                                        }
                                    });*/


                                    /**  The basic idea is to check if all users are ready (by counting documents where users are ready)
                                     * and introduce another field with value "True" which will be used to display data for different individuals.  **/

                                    //Making user that all users are ready before Processing and retrieving information
                                    //This is where i call a recycler view.

                                    //Making user that all users are ready before retrieving information
                                }else if(AdministratorRPermission.equals("F") && setToPlay.equals("Ready")){
                                    displayResults(currentUser);
                                    enterArenas();
                                }
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
                        numberPot.add(parseInt(valueOf(document.get("GameCode"))));

                    }
                    //shuffle(numberPot);
                    ArrayList<Integer> gameWinners;
                    gameWinners = new ArrayList<>();

                    //selecting the winners
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

                        //System.out.println("This a code for a loser!"+ numberPot);

                    }
                    for(int counter = 1; counter < 5; counter++){
                        int finalCounter = counter;
                        GamePortals.whereEqualTo("GameCode", gameWinners.get(counter - 1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document: task.getResult()){
                                        int winnerCode = Integer.parseInt(valueOf(document.get("GameCode")));

                                        // Trying to check/verify if the the random numbers that have been selected much with those in the db
                                        if(gameWinners.get(finalCounter) == winnerCode){
                                            Toast.makeText(getApplicationContext(),"These r the codes" + gameWinners.get(finalCounter), Toast.LENGTH_SHORT).show();
                                        }

                                        //updating the db,
                                        if (gameWinners.size() <= 4){

                                            Map<String, Object> updateWinner = new HashMap<>();
                                            updateWinner.put("GameState","Winner");
                                            GamePortals.document(document.getId()).update(updateWinner);
                                            Toast.makeText(getApplicationContext(),"Winners updated", Toast.LENGTH_SHORT).show();
                                            //System.out.println(gameWinners);

                                        }else{

                                            /*Map<String, Object> updateLoser = new HashMap<>();
                                            updateLoser.put("GameState","Loser");
                                            GamePortals.document(document.getId()).update(updateLoser);
                                            Toast.makeText(getApplicationContext(),"Loser(s) updated", Toast.LENGTH_SHORT).show();*/
                                            // System.out.println("This a code for a loser!"+ numberPot);

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
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //use a pop up alert to display if won or lost
                    DocumentSnapshot document = task.getResult();
                    //int Amount = parseInt(String.valueOf(document.get("Amount")));

                    if (document.exists()){

                        if(document.get("GameState").equals("none")){
                            displayData.setText("Loser !!!");
                        }else{
                            displayData.setText(valueOf(document.get("GameState")));
                        }
                        //doing some calculations
                        systemCalculations(userID);

                    }else{
                        Toast.makeText(getApplicationContext(),"Game in Progress !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void systemCalculations(String userID){
        GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot GameDocument = task.getResult();
                    if (GameDocument.exists()){
                        double UserAmount = Double.parseDouble(valueOf(GameDocument.get("Amount")));

                        if (GameDocument.get("GameState").equals("Winner")){
                            Accounts.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot AccountsDocument = task.getResult();
                                    double balance = Double.parseDouble(valueOf(AccountsDocument.get("AmountBalance")));

                                    double AmountWon = UserAmount * 0.2;
                                    double total = balance + AmountWon;

                                    Map<String, Object> updateTotal = new HashMap<>();
                                    updateTotal.put("AmountBalance",total);
                                    Accounts.document(userID).update(updateTotal);

                                    //Storing and deleting records
                                    //storeRecords(userID);
                                }
                            });
                        }
                        else if(GameDocument.get("GameState").equals("none")){
                            Accounts.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot AccountsDocument = task.getResult();
                                    double balance = Double.parseDouble(valueOf(AccountsDocument.get("AmountBalance")));

                                    double accountBalance = balance - UserAmount;

                                    Map<String, Object> updateBalance = new HashMap<>();
                                    updateBalance.put("AmountBalance",accountBalance);
                                    Accounts.document(userID).update(updateBalance);

                                    //Storing and deleting records
                                    //storeRecords(userID);
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext()," The game has not started!!!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext()," The document does not exist!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void updateSystemAccount(String userID){
        db.collection("GamePortals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Check if user balance is qualified for a particular game portal.
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();

                    GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()){

                                double UserAmount  = Double.parseDouble(valueOf(document.get("Amount")));

                                Accounts.document("systemAccount").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot systemDocument = task.getResult();

                                            if (systemDocument.exists()){
                                                double systemBalance = Double.parseDouble(valueOf(systemDocument.get("AmountBalance")));

                                                if (numberOfDocuments == 5){
                                                    double AmountWon = UserAmount * 0.2;
                                                    double total = systemBalance + AmountWon;

                                                    Map<String, Object> updateTotal = new HashMap<>();
                                                    updateTotal.put("AmountBalance",total);
                                                    Accounts.document("systemAccount").update(updateTotal);

                                                }else{
                                                    Toast.makeText(getApplicationContext(),"System Account, user population does meet specifies requirements.", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(),"System Document does not exist", Toast.LENGTH_SHORT).show();
                                            }

                                        }else{
                                            Toast.makeText(getApplicationContext(),"System's Account encountered an error !", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(getApplicationContext(),"error, something went wrong ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                    Toast.makeText(getApplicationContext(),"System Account was not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void storeRecords(String userID){
        /** storing records  **/
        myCashBoosterRecords dbHelper = new myCashBoosterRecords(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
               DocumentSnapshot document = task.getResult();
               String gameCode = valueOf(document.get("GameCode"));
               String gameState = valueOf(document.get("GameState"));
               String Amount = valueOf(document.get("Amount"));

               GamePortals.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()){

                           int numberOfPlayers = task.getResult().size();
                           if(gameState.equals("none")){
                               values.put("gameCode",gameCode);
                               values.put("portalRange",numberOfPlayers);
                               values.put("GameState","lost");
                               values.put("Amount",Amount);

                               long row =  db.insert("records",null,values);
                               if (row != 0){
                                   clearPortal(userID);
                                   Toast.makeText(getApplicationContext(),"saving records in SQL db !!!", Toast.LENGTH_SHORT).show();
                               }else{
                                   Toast.makeText(getApplicationContext(),"Error occurred while saving records in SQL db !!!", Toast.LENGTH_SHORT).show();
                               }

                           }else{
                               values.put("gameCode",gameCode);
                               values.put("portalRange",numberOfPlayers);
                               values.put("GameState",numberOfPlayers);
                               values.put("Amount",Amount);

                               long row =  db.insert("records",null,values);
                               if (row != 0){
                                   clearPortal(userID);
                                   Toast.makeText(getApplicationContext(),"saving records in SQL db !!!", Toast.LENGTH_SHORT).show();
                               }else{
                                   Toast.makeText(getApplicationContext(),"Error occurred while saving records in SQL db !!!", Toast.LENGTH_SHORT).show();
                               }
                           }
                       }
                   }
               });
            }
        });
    }

    public void clearPortal(String userID){
        GamePortals.document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext()," Portal Exited !!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext()," Portal Failed to be Exited !!!", Toast.LENGTH_SHORT).show();
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
                        displayBalance.setText(valueOf(document.get("AmountBalance")));
                    }else {
                        displayBalance.setText("N/A");
                        Toast.makeText(getApplicationContext(),"User Account Document does not exist!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getGameCode(String userID){
        GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        displayCode.setText(valueOf(document.get("GameCode")));
                    }else{
                        displayCode.setText("N/A");
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"error occurred while obtaining Code !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void enterArenas(){
        Intent intent = new Intent(Activity4.this,LandingActivity.class);
        startActivity(intent);
        finish();
    }

    //Background operation
    public void playGameVisible(String userID){
        Handler handler;
        handler = new Handler();

        Runnable checkVisibility = new Runnable() {
            @Override
            public void run() {
                GamePortals.whereEqualTo("GameStart","Ready").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int readyParticipant = task.getResult().size();

                            GamePortals.whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        int numberOfWinners = task.getResult().size();

                                        GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();

                                                    if (document.exists()){
                                                        String permission = valueOf(document.get("Permission"));

                                                        if (permission.equals("A")){

                                                            playGame.setVisibility(View.VISIBLE);

                                                        }else if (readyParticipant == 5 && numberOfWinners >= 1){
                                                            playGame.setVisibility(View.VISIBLE);
                                                        }else {
                                                            playGame.setVisibility(View.GONE);
                                                        }

                                                    }else{
                                                        Toast.makeText(getApplicationContext(),"document does not exists", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(getApplicationContext(),"retrieving results didn't occur ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(),"winners not available, something went wrong ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {
                            Intent intent = new Intent(Activity4.this,Activity2.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Something went wrong !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                handler.postDelayed(this,5000);
            }
        };
        handler.post(checkVisibility);

    }

    //Background operation
    public void autoSelectWinnersOnIdeal(String userID){
        Handler handler;
        handler = new Handler();

        Runnable autoSelectOnIdeal = new Runnable() {
            @Override
            public void run() {

                GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                String permission = valueOf(document.get("Permission"));

                                if (permission.equals("A")){
                                    //SelectWinners(userID);
                                    //systemCalculations(userID);
                                }

                            }else {
                                Toast.makeText(getApplicationContext()," Document does not exist !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                handler.postDelayed(this,60000*5);
            }
        };
        handler.post(autoSelectOnIdeal);
    }

}