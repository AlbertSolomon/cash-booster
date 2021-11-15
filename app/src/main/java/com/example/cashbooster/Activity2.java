package com.example.cashbooster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Activity2 extends AppCompatActivity {

    CardView card2k,card5k,card10k,card20k,card50k,card100k;
    TextView CashBalanceDisplay;
    ProgressBar gameProgressBar;

    FirebaseAnalytics TestFirebaseAnalytics;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Accounts;

    String collectionName, portalNameHolder;
    SharedPreferences collectionNameSp;

    CollectionReference GamePortals;
    Random randomNumber = new Random();
    int gameRange;
    int luckyNumber;
    String permission, GameState, GameStart, AccountsCollectionName;
    boolean gamePlayed, inAnotherGame;

    Date date = new Date();


    double AccountBalance;

    {
        AccountsCollectionName = "Accounts";
        portalNameHolder = "";
        luckyNumber = randomNumber.nextInt(9999 - 100) + 1;
        permission = "A";
        GameStart = "Ready";
        GameState = "None";
        gameRange = 2000;
        Accounts = db.collection(AccountsCollectionName);
    }

    Integer requiredLosers = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        FirebaseApp.initializeApp(this);

        collectionNameSp = getSharedPreferences("gameCollections", Context.MODE_PRIVATE);

        CashBalanceDisplay = findViewById(R.id.CashBalanceDisplay);
        gameProgressBar = findViewById(R.id.gameProgressBar);

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

        arenaQualification(currentUser);
        //peepAndCheck();

        //Restriction after a game has been played, and when some users have exited the Portal
        //playedGameRestriction(currentUser );

        //################################# 2k ############################################
        card2k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 2000;
                collectionName = "GamePortals";
                playedGameRestriction(collectionName);

                SharedPreferences.Editor editor = collectionNameSp.edit();
                editor.putString("collectionName",collectionName);
                editor.putInt("numberOfLosers",requiredLosers);
                editor.commit();

                if (AccountBalance < gameRange){

                    Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                    //an intent to a top up page for an api
                }else{
                    db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int numberOfPlayers = task.getResult().size();

                                db.collection(collectionName).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){

                                                gameProgressBar.setVisibility(View.VISIBLE);
                                                openActivity4(currentUser,collectionName);

                                            }else {
                                                //check if the game has been played
                                                if(gamePlayed){
                                                    Toast.makeText(getApplicationContext(),"Wait for players to clear !!!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (numberOfPlayers == 0){
                                                        permission = "A";

                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else if (numberOfPlayers <5){
                                                        permission = "F";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }else{
                                            task.getException();
                                        }
                                    }
                                });
                            }else{
                                task.getException();
                            }
                        }
                    });
                }
            }
        });

        //################################# 5k ############################################
        card5k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 5000;
                String collectionName = "GamePortal5k";

                SharedPreferences.Editor editor = collectionNameSp.edit();
                editor.putString("collectionName",collectionName);
                editor.putInt("numberOfLosers",requiredLosers);
                editor.commit();

                if (AccountBalance < gameRange){

                    Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                    //an intent to a top up page for an api
                }else{
                    db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int numberOfPlayers = task.getResult().size();

                                db.collection(collectionName).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){

                                                gameProgressBar.setVisibility(View.VISIBLE);
                                                openActivity4(currentUser,collectionName);
                                            }else {
                                                //check if the game has been played
                                                if(gamePlayed){
                                                    Toast.makeText(getApplicationContext(),"Wait for players to clear !!!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (numberOfPlayers == 0){
                                                        permission = "A";

                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else if (numberOfPlayers <5){
                                                        permission = "F";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }else{
                                            task.getException();
                                        }
                                    }
                                });
                            }else{
                                task.getException();
                            }
                        }
                    });
                }
            }
        });

        //################################### 10k ##########################################
        card10k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 10000;
                String collectionName = "GamePortal10k";

                SharedPreferences.Editor editor = collectionNameSp.edit();
                editor.putString("collectionName",collectionName);
                editor.putInt("numberOfLosers",requiredLosers);
                editor.commit();

                if (AccountBalance < gameRange){

                    Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                    //an intent to a top up page for an api
                }else{
                    db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int numberOfPlayers = task.getResult().size();

                                db.collection(collectionName).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                gameProgressBar.setVisibility(View.VISIBLE);
                                                openActivity4(currentUser,collectionName);
                                            }else {
                                                //check if the game has been played
                                                if(gamePlayed){
                                                    Toast.makeText(getApplicationContext(),"Wait for players to clear !!!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (numberOfPlayers == 0){
                                                        permission = "A";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else if (numberOfPlayers <5){
                                                        permission = "F";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }else{
                                            task.getException();
                                        }
                                    }
                                });
                            }else{
                                task.getException();
                            }
                        }
                    });
                }
            }
        });

        //############################### 20k ##############################################
        card20k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 20000;
                String collectionName = "GamePortal20k";

                SharedPreferences.Editor editor = collectionNameSp.edit();
                editor.putString("collectionName",collectionName);
                editor.putInt("numberOfLosers",requiredLosers);
                editor.commit();

                if (AccountBalance < gameRange){

                    Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                    //an intent to a top up page for an api
                }else{
                    db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int numberOfPlayers = task.getResult().size();

                                db.collection(collectionName).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                gameProgressBar.setVisibility(View.VISIBLE);
                                                openActivity4(currentUser,collectionName);
                                            }else {
                                                //check if the game has been played
                                                if(gamePlayed){
                                                    Toast.makeText(getApplicationContext(),"Wait for players to clear !!!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (numberOfPlayers == 0){
                                                        permission = "A";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else if (numberOfPlayers <5){
                                                        permission = "F";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }else{
                                            task.getException();
                                        }
                                    }
                                });
                            }else{
                                task.getException();
                            }
                        }
                    });
                }
            }
        });

        //############################ 50k #################################################
        card50k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 50000;
                String collectionName = "GamePortal50k";

                SharedPreferences.Editor editor = collectionNameSp.edit();
                editor.putString("collectionName",collectionName);
                //editor.putInt("numberOfLosers",requiredLosers);
                editor.commit();

                if (AccountBalance < gameRange){

                    Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                    //an intent to a top up page for an api
                }else{
                    db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int numberOfPlayers = task.getResult().size();

                                db.collection(collectionName).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                gameProgressBar.setVisibility(View.VISIBLE);
                                                openActivity4(currentUser,collectionName);
                                            }else {
                                                //check if the game has been played
                                                if(gamePlayed){
                                                    Toast.makeText(getApplicationContext(),"Wait for players to clear !!!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (numberOfPlayers == 0){
                                                        permission = "A";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else if (numberOfPlayers <5){
                                                        permission = "F";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }else{
                                            task.getException();
                                        }
                                    }
                                });
                            }else{
                                task.getException();
                            }
                        }
                    });
                }
            }
        });

        //################################ 100k #############################################
        card100k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRange = 100000;
                String collectionName = "GamePortal100k";

                SharedPreferences.Editor editor = collectionNameSp.edit();
                editor.putString("collectionName",collectionName);
                editor.putInt("numberOfLosers",requiredLosers);
                editor.commit();

                if (AccountBalance < gameRange){

                    Toast.makeText(getApplicationContext(),"Please top up your account", Toast.LENGTH_SHORT).show();
                    //an intent to a top up page for an api
                }else{
                    db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int numberOfPlayers = task.getResult().size();

                                db.collection(collectionName).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                gameProgressBar.setVisibility(View.VISIBLE);
                                                openActivity4(currentUser,collectionName);
                                            }else {
                                                //check if the game has been played
                                                if(gamePlayed){
                                                    Toast.makeText(getApplicationContext(),"Wait for players to clear !!!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    if (numberOfPlayers == 0){
                                                        permission = "A";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else if (numberOfPlayers <5){
                                                        permission = "F";
                                                        gameProgressBar.setVisibility(View.VISIBLE);
                                                        insertDataDocuments(currentUser,permission,gameRange,luckyNumber,GameStart,GameState,collectionName);

                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"The Challenge is full", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }else{
                                            task.getException();
                                        }
                                    }
                                });
                            }else{
                                task.getException();
                            }
                        }
                    });
                }
            }
        });
    }

    //############################################ System functionalities... ########################################################
    public void insertDataDocuments(String UserID, String permission, int Amount, int gameCode, String gameStart,String gameState,String collectionName){

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
                gamePortal.put("CreatedOn",date);

                db.collection(collectionName).document(UserID).set(gamePortal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        gameProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"DocumentSnapshot successfully written", Toast.LENGTH_SHORT).show();
                        openActivity4(UserID,collectionName);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(getApplicationContext(),"DocumentSnapshot failed", Toast.LENGTH_SHORT).show();
                        gameProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        };

        Thread insertInDocuments = new Thread(insertDataDocuments);
        insertInDocuments.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cash_booster_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Home:
                Intent intent = new Intent(Activity2.this, LandingActivity.class);
                startActivity(intent);
                break;

            case R.id.topUpAccount:
                Toast.makeText(getApplicationContext(),"Top up Service is currently not Available", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cashOut:
                Toast.makeText(getApplicationContext(),"Cash out Service is currently not Available", Toast.LENGTH_SHORT).show();
                break;

            case R.id.checkMyRecords:

                Intent intentRecords = new Intent(Activity2.this, Activity5.class);
                startActivity(intentRecords);
                break;

            case R.id.acknowledgement:

                //Intent intent = new Intent(Activity2.this, Acknowledgment.class);
                //startActivity(intent);
                Toast.makeText(getApplicationContext(),"navigate to Acknowledgment page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.aboutCashBoosterApp:

                //Intent intent = new Intent(Activity2.this, AboutUs.class);
                //startActivity(intent);
                Toast.makeText(getApplicationContext(),"navigate to About Us page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:

                //calling sign out
                FirebaseAuth.getInstance().signOut();

                SharedPreferences loginCredentials = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.clear();
                editor.apply();

                Intent firebaseSignOut = new Intent(Activity2.this, MainActivity.class);
                startActivity(firebaseSignOut);
                finish();

                //Toast.makeText(getApplicationContext(),"Fire Store Logout", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //Operation Methods
    public void searchPortals(){
        db.collection("GamePortals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Check if user balance is qualified for a particular game portal.
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();
                    //textView2.setText(String.valueOf(numberOfDocuments));
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                    Toast.makeText(getApplicationContext(),"Could not count the number of documents in GamePortals", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void openActivity4(String userID,String collectionName){

        db.collection(collectionName).document(userID).update("GameStart","Ready").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"user is Ready !!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity2.this, Activity4.class);
                startActivity(intent);
                gameProgressBar.setVisibility(View.GONE);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                gameProgressBar.setVisibility(View.GONE);
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

                                int starterPack = 500000;
                                Map<String, Object>accountCollection = new HashMap<>();
                                accountCollection.put("UserID",UserID);
                                accountCollection.put("AmountBalance", starterPack);

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
                                       // CashBalanceDisplay.setText("N/A");
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
                            AccountBalance = Double.parseDouble(String.valueOf(document.get("AmountBalance")));
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
    public void playedGameRestriction(String nameOfCollection){

        Runnable pGameRestriction = new Runnable() {
            @Override
            public void run() {
                db.collection(nameOfCollection).whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            int participants = task.getResult().size();

                            if (participants >= 1){
                                gamePlayed = true;
                            }else{
                                gamePlayed = false;
                            }
                        }else{
                            task.getException();
                        }
                    }
                });
            }
        };

        Thread gameRestrictionBgThread = new Thread(pGameRestriction);
        gameRestrictionBgThread.start();

    }

    //

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

    //Players have to play one game at a time... (sharedPref)
    public void OnceAtATimeRestriction(){

    }

    /*public void insertingAndCreatingPortals(String collectionName,String UserID){

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
    }*/

    //background operations, checking if portals

}