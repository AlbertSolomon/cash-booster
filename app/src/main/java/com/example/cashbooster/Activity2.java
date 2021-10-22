package com.example.cashbooster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Activity2 extends AppCompatActivity {

    Button activity2Button, searchPortal, goToActivity4, getRecords;
    TextView textView2, CashBalanceDisplay;
    CardView card1k,card2k,card5k,card10k,card20k,card50k,card100k;

    FirebaseAnalytics TestFirebaseAnalytics;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Accounts;

    String collectionName, portalNameHolder;

    CollectionReference GamePortals;
    Random randomNumber = new Random();
    int gameRange;
    int luckyNumber;
    String permission, GameState, GameStart, AccountsCollectionName;
    boolean natureOfPortal;

    {
        AccountsCollectionName = "Accounts";
        portalNameHolder = "";
        collectionName = "GamePortals";
        GamePortals = db.collection(collectionName);
        luckyNumber = randomNumber.nextInt(9999 - 100) + 1;
        permission = "A";
        GameStart = "Ready";
        GameState = "None";
        gameRange = 2000;
        Accounts = db.collection(AccountsCollectionName);
    }

    public static final String dataHolder = "com.example.cashbooster.TEXT_TO_SEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        FirebaseApp.initializeApp(this);

        activity2Button = findViewById(R.id.activity2Button);
        getRecords = findViewById(R.id.getRecords);
        textView2 = findViewById(R.id.textView2);
        CashBalanceDisplay = findViewById(R.id.CashBalanceDisplay);
        searchPortal = findViewById(R.id.searchPortal);
        goToActivity4 = findViewById(R.id.goToActivity4);

        //CardView Declarations test
        card1k = findViewById(R.id.card1k);
        card2k = findViewById(R.id.card2k);
        card5k = findViewById(R.id.card5k);
        card10k = findViewById(R.id.card10k);
        card20k = findViewById(R.id.card20k);
        card50k = findViewById(R.id.card50k);
        card100k = findViewById(R.id.card100k);

        TestFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getCurrentUser().getUid();

        getAccountBalance(currentUser);
        AccountCollection(currentUser,gameRange);

        //arenaQualification(currentUser);
        //peepAndCheck();

        //Restriction after a game has been played, and when some users have exited the Portal
        //playedGameRestriction(currentUser );

        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

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

                        //background operation (continuous check if the portal is full, if full set activity2Button GONE
                        activity2Button.setVisibility(View.GONE);
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

                GamePortals.whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int participants = task.getResult().size();
                            //if (participants >= 1){
                               // goToActivity4.setVisibility(View.GONE);
                           // }else{

                                goToActivity4.setOnClickListener(view -> {
                                    openActivity4(currentUser);
                                });
                                Toast.makeText(getApplicationContext()," Getting set ", Toast.LENGTH_SHORT).show();
                            //}
                        }else{

                            Toast.makeText(getApplicationContext()," Task does not exists !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        getRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTheRecords();
            }
        });

        //############################## 1k ################################################
        card1k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 1000;
                String collectionName = "GamePortal1k";

                //collection reference for a specific card portal(game amount)
                CollectionReference GamePortal = db.collection(collectionName);

                //passing values to next activity
                //Intent intent = new Intent();
               // intent.putExtra("gameRange",gameRange);
                //intent.putExtra("collectionName",collectionName);
               // startActivity(intent);

                    //how to retrieve values from intent in the next activity
                    /**
                     * Intent intent = getIntent();
                     * String collectionName = intent.getStringExtra("collectionName");
                     * **/
            }
        });

        //################################# 2k ############################################
        card2k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 2000;
                //String collectionName = "GamePortal2k";

                //collection reference for a specific card portal(game amount) password: cashbooster000
                //CollectionReference GamePortals = db.collection(collectionName);

                /*GamePortals.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int numberOfPlayers = task.getResult().size();

                            if (numberOfPlayers == 5){

                                Toast.makeText(getApplicationContext(),"Sorry the portal is full!!!", Toast.LENGTH_LONG).show();

                            }else {
                                insertingAndCreatingPortals(collectionName,currentUser);
                                //passing values to next activity
                                Intent intent = new Intent(Activity2.this, Activity4.class);
                                //intent.putExtra("gameRange",gameRange);
                                intent.putExtra("collectionName",collectionName);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });*/


                GamePortals.whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int participants = task.getResult().size();
                            //if (participants >= 1){
                            // goToActivity4.setVisibility(View.GONE);
                            // }else{

                                openActivity4(currentUser);
                            Toast.makeText(getApplicationContext()," Getting set ", Toast.LENGTH_SHORT).show();
                            //}
                        }else{

                            Toast.makeText(getApplicationContext()," Task does not exists !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        //################################# 5k ############################################
        card5k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 5000;
                String collectionName = "GamePortal5k";

                //collection reference for a specific card portal(game amount)
                CollectionReference GamePortal = db.collection(collectionName);
            }
        });

        //################################### 10k ##########################################
        card10k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 10000;
                String collectionName = "GamePortal10k";

                //collection reference for a specific card portal(game amount)
                CollectionReference GamePortal = db.collection(collectionName);
            }
        });

        //############################### 20k ##############################################
        card20k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 20000;
                String collectionName = "GamePortal20k";

                //collection reference for a specific card portal(game amount)
                CollectionReference GamePortal = db.collection(collectionName);
            }
        });

        //############################ 50k #################################################
        card50k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 50000;
                String collectionName = "GamePortal50k";

                //collection reference for a specific card portal(game amount)
                CollectionReference GamePortal = db.collection(collectionName);


            }
        });

        //################################ 100k #############################################
        card100k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 100000;
                String collectionName = "GamePortal100k";

                //collection reference for a specific card portal(game amount)
                CollectionReference GamePortal = db.collection(collectionName);
            }
        });
    }

    //################################################## ASYNC TASK (CLASS DECLARATION) #####################################################
    public class JoiningPortals extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String string){
            super.onPostExecute(string);
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    //############################################ System functionalities... ########################################################
    public void insertDataDocuments(String UserID, String permission, int Amount, int gameCode, String gameStart,String gameState){

        Runnable insertDataDocuments = new Runnable() {
            @Override
            public void run() {
                Map<String, Object>gamePortal = new HashMap<>();
                gamePortal.put("UserID",UserID);
                gamePortal.put("Permission",permission);
                gamePortal.put("Amount",Amount);
                gamePortal.put("GameCode",gameCode);
                gamePortal.put("GameStart",gameStart);
                gamePortal.put("GameState",gameState);
                //gamePortal.put("Created", Timestamp.now());

                db.collection(collectionName).document(UserID).set(gamePortal).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        };

        Thread insertInDocuments = new Thread(insertDataDocuments);
        insertInDocuments.start();
    }

    public void searchPortals(){
        db.collection("GamePortals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Check if user balance is qualified for a particular game portal.
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
                Intent intent = new Intent(Activity2.this, Activity4.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(),"waiting for user to be Ready !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //background operation
    public void AccountCollection(String UserID, int AccountBalance){

        //Handler handler = new Handler();
        Runnable setAccountBg = new Runnable() {
            @Override
            public void run() {
                Accounts.document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if(!task.getResult().exists()){
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
                        }
                    }
                });
            }
        };

        Thread setAccount = new Thread(setAccountBg);
        setAccount.start();
    }

    //background operation
    public void getAccountBalance(String UserID){

        Handler handler;
        handler = new Handler();

        Runnable getAccountBalance = new Runnable() {
            @Override
            public void run() {
                Accounts.document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            Runnable iteratedCheck = new Runnable() {
                                @Override
                                public void run() {
                                    if(document.exists()){
                                        CashBalanceDisplay.setText(String.valueOf(document.get("AmountBalance")));
                                    }else {
                                        CashBalanceDisplay.setText("N/A");
                                        handler.postDelayed(this,5000);
                                        Toast.makeText(getApplicationContext(),"User Account Document does not exist!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            handler.post(iteratedCheck);
                        }else{
                            Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        Thread accountBalance = new Thread(getAccountBalance);
        accountBalance.start();
    }

    public void getTheRecords(){
        Intent intent = new Intent(Activity2.this, Activity5.class);
        startActivity(intent);
    }

    //background operation
    public void arenaQualification(String userID){

        Runnable aQualification = new Runnable() {
            @Override
            public void run() {
                Accounts.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document= task.getResult();

                        if (document.exists()){
                            double AccountBalance = Double.parseDouble(String.valueOf(document.get("AmountBalance")));

                            if (AccountBalance < gameRange){
                                activity2Button.setVisibility(View.GONE);
                                goToActivity4.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                                //an intent to a top up page for an api
                            }else{
                                activity2Button.setVisibility(View.VISIBLE);
                                goToActivity4.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Account does not exists", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };

        Thread ArenaQBgThread = new Thread(aQualification);
        ArenaQBgThread.start();

    }

    //background operation
    public void playedGameRestriction(String userID){

        Runnable pGameRestriction = new Runnable() {
            @Override
            public void run() {
                GamePortals.whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            int participants = task.getResult().size();

                            if (participants >= 1){
                                activity2Button.setVisibility(View.GONE);
                            }else{
                                activity2Button.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext()," Join Arena ", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            activity2Button.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };

        Thread gameRestrictionBgThread = new Thread(pGameRestriction);
        gameRestrictionBgThread.start();

    }

    //background operation
    public void peepAndCheck(){

        Handler handler;
        handler = new Handler();

        Runnable innerThread = new Runnable() {
            @Override
            public void run() {
                GamePortals.whereEqualTo("GameStart","Ready").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int noReadyParticipants = task.getResult().size();

                            Runnable iteratedCheck = new Runnable() {
                                @Override
                                public void run() {
                                    if(noReadyParticipants == 5){
                                        Intent intent = new Intent(Activity2.this, Activity4.class);
                                        startActivity(intent);
                                        finish();

                                    }else {
                                        handler.postDelayed(this,5000);
                                    }
                                }

                            };
                            handler.post(iteratedCheck);
                        }else{
                            Toast.makeText(getApplicationContext()," task was not successful ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        Thread peepThread = new Thread(innerThread);
        peepThread.start();
    }

    public void insertingAndCreatingPortals(String collectionName,String UserID){

        Runnable RunningInsertAndCreate = new Runnable() {
            @Override
            public void run() {
                db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int numberOfDocuments = task.getResult().size();

                            if (numberOfDocuments == 0){
                                String permission = "A";
                                activity2Button.setOnClickListener(view -> {
                                    insertDataDocuments(UserID,permission,gameRange,luckyNumber,GameStart,GameState);
                                });

                            }else if(numberOfDocuments < 5){

                                String permission = "F";
                                activity2Button.setOnClickListener(view -> {
                                    insertDataDocuments(UserID,permission,gameRange,luckyNumber,GameStart,GameState);
                                });

                            }else{
                                Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();

                                //background operation (continuous check if the portal is full, if full set activity2Button GONE
                                activity2Button.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Could not count the number of documents in GamePortals", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };

        Thread runningIAndCThread = new Thread(RunningInsertAndCreate);
        runningIAndCThread.start();
    }

    //background operations, checking if portals

}