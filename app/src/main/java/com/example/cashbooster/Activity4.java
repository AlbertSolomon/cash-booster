package com.example.cashbooster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class Activity4 extends AppCompatActivity {
    //RecyclerView displayData;
    TextView displayData, displayBalance, displayCode, PlayerMessage;
    Button playGame;
    //String TAG;

    CollectionReference GamePortals, Accounts;
    String collectionName;
    String AccountsCollectionName = "Accounts";
    //String chosenOnes = "TheChosenOnes";

    String winnersMessage = "Congratulations You are a";
    String losersMessage = "Sorry, Try Again ";
    String Loser = "Loser";
    String Winner = "Winner";
    String ThreeToWin = "Three to Win";
    String FourToWin = "Four to Win";


    boolean natureOfAdmin,threeToWinIs;
    int myCode;
    int numberOfLosers = 1;
    int numberSp;

    FirebaseFirestore db;
    {
        db = FirebaseFirestore.getInstance();
        Accounts = db.collection(AccountsCollectionName);
    }

    Vibrator vibrator;

    RequestQueue requestQueue;
    String insertInUrl = "https://cashbooster.000webhostapp.com/jsonProcessor.php";

    String game_state;
    Integer game_amount,gameCode;
    Date date = new Date();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences testSp = getApplicationContext().getSharedPreferences("gameCollections", Context.MODE_PRIVATE);
        collectionName = testSp.getString("collectionName","");
        numberOfLosers = testSp.getInt("numberOfLosers",numberSp);
       // Toast.makeText(getApplicationContext(), String.format("# of loser is%d", numberOfLosers), Toast.LENGTH_LONG).show();

        if (numberOfLosers == 2){
            threeToWinIs = true;
        }else {
            threeToWinIs = false;
        }

        GamePortals = db.collection(collectionName);

        PlayerMessage = findViewById(R.id.PlayerMessage);
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

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        getAccountBalance(currentUser);
        getGameCode(currentUser);
        playGameVisible(); //new

        GamePortals.document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        String AdministratorRPermission = valueOf(document.get("Permission"));

                        if(AdministratorRPermission.equals("A")){
                            natureOfAdmin = true;
                            //SelectWinners(currentUser);
                            //updateSystemAccount(currentUser);
                            //enterArenas();

                        }else if(AdministratorRPermission.equals("F")){
                            //displayResults(currentUser);
                            //enterArenas();
                            natureOfAdmin = false;
                        }

                    }
                }
            }
        });

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SelectWinners(currentUser);
                //SelectWinnersVersion2(currentUser);
                //displayResults(currentUser);
                try {
                    //Thread.sleep(10000);
                    if (natureOfAdmin){
                        SelectWinners(currentUser);
                        //updateSystemAccount(currentUser);
                        updateSystemAccountV2(currentUser);
                    }else{
                        displayResults(currentUser);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
                Intent intent = new Intent(Activity4.this, LandingActivity.class);
                startActivity(intent);
                break;

            case R.id.topUpAccount:
                Toast.makeText(getApplicationContext(),"Top up Service is currently not Available", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cashOut:
                Toast.makeText(getApplicationContext(),"Cash out Service is currently not Available", Toast.LENGTH_SHORT).show();
                break;

            case R.id.checkMyRecords:

                Intent intentRecords = new Intent(Activity4.this, Activity5.class);
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
                SharedPreferences loginCredentials = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.clear();
                editor.apply();

                Intent firebaseSignOut = new Intent(Activity4.this, MainActivity.class);
                startActivity(firebaseSignOut);
                finish();
                //Toast.makeText(getApplicationContext(),"Fire Store Logout", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void SelectWinners(String userID){

        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    ArrayList<Integer> numberPotClone;
                    numberPotClone = (ArrayList<Integer>) numberPot.clone();

                    //shuffle(numberPot);
                    ArrayList<Integer> gameWinners;
                    gameWinners = new ArrayList<>();

                    if (threeToWinIs){
                        int numberOfElements = 3;
                        for (int i = 0; i < numberOfElements; i++) {
                            int randomIndex = random.nextInt(numberPot.size());
                            //int randomElement = numberPot.get(randomIndex);
                            numberPot.remove(randomIndex);

                        }

                        //displayData.setText(valueOf(gameWinners.get(0)));
                        //displayData.setText(valueOf(numberPot));
                        //System.out.println(numberPotClone);

                        //Removing losers from winners.
                         numberPotClone.removeAll(numberPot);

                        //displayData.setText(valueOf(numberPotClone));
                        //System.out.println(numberPotClone);

                        //updating winners
                        for(int clonedWinners = 0; clonedWinners <numberPotClone.size(); clonedWinners++){
                            ArrayList<Integer> finalNumberPotClone = numberPotClone;
                            db.collection(collectionName).whereEqualTo("GameCode",numberPotClone.get(clonedWinners)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){

                                                Map<String, Object> updateWinner = new HashMap<>();
                                                updateWinner.put("GameState","Winner");
                                                GamePortals.document(document.getId()).update(updateWinner);
                                                Toast.makeText(getApplicationContext(),"Winners updated", Toast.LENGTH_SHORT).show();
                                                System.out.println(finalNumberPotClone);

                                        }
                                    }else{
                                        task.getException();
                                    }
                                }
                            });
                        }

                        //updating losers
                        for (int undisputedLosers = 0; undisputedLosers < numberPot.size(); undisputedLosers++){
                            db.collection(collectionName).whereEqualTo("GameCode",numberPot.get(undisputedLosers)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                    for (QueryDocumentSnapshot document: task.getResult()){
                                        Map<String, Object> updateLoser = new HashMap<>();
                                        updateLoser.put("GameState",Loser);
                                        GamePortals.document(document.getId()).update(updateLoser);
                                        Toast.makeText(getApplicationContext(),"Loser updated", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }

                        try {
                            String GameType = "Three To win";
                            Thread.sleep(300);
                            FirestoreGameRecords(GameType);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        //System.out.println("losers");
                        //System.out.println(numberPot.get(0));
                        //System.out.println(numberPot.get(1));

                        try{

                            Thread.sleep(700);
                            displayResults(userID);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else {

                        //########################################### new implementation 4 to  #############################################

                        int numberOfElements = 4;

                        for (int i = 0; i < numberOfElements; i++) {
                            int randomIndex = random.nextInt(numberPot.size());
                            //int randomElement = numberPot.get(randomIndex);
                            numberPot.remove(randomIndex);

                        }

                        //displayData.setText(valueOf(gameWinners.get(0)));
                        //displayData.setText(valueOf(numberPot));
                        //System.out.println(numberPotClone);

                        //Removing losers from winners.
                        numberPotClone.removeAll(numberPot);

                        //displayData.setText(valueOf(numberPotClone));
                        //System.out.println(numberPotClone);

                        //updating winners
                        for(int clonedWinners = 0; clonedWinners <numberPotClone.size(); clonedWinners++){
                            ArrayList<Integer> finalNumberPotClone = numberPotClone;
                            db.collection(collectionName).whereEqualTo("GameCode",numberPotClone.get(clonedWinners)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){

                                            Map<String, Object> updateWinner = new HashMap<>();
                                            updateWinner.put("GameState","Winner");
                                            GamePortals.document(document.getId()).update(updateWinner);
                                            Toast.makeText(getApplicationContext(),"Winners updated", Toast.LENGTH_SHORT).show();
                                            System.out.println(finalNumberPotClone);

                                        }
                                    }else{
                                        task.getException();
                                    }
                                }
                            });
                        }

                        //updating losers
                        for (int undisputedLosers = 0; undisputedLosers < numberPot.size(); undisputedLosers++){
                            db.collection(collectionName).whereEqualTo("GameCode",numberPot.get(undisputedLosers)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                                    for (QueryDocumentSnapshot document: task.getResult()){
                                        Map<String, Object> updateLoser = new HashMap<>();
                                        updateLoser.put("GameState",Loser);
                                        GamePortals.document(document.getId()).update(updateLoser);
                                        Toast.makeText(getApplicationContext(),"Loser updated", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }

                        try {
                            String GameType = "Four To win";
                            Thread.sleep(300);
                            FirestoreGameRecords(GameType);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        try{

                            Thread.sleep(700);
                            displayResults(userID);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayResults(String userID){
        GamePortals.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint({"SetTextI18n", "MissingPermission"})
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //use a pop up alert to display if won or lost
                    DocumentSnapshot document = task.getResult();
                    //int Amount = parseInt(String.valueOf(document.get("Amount")));

                    if (document.exists()){
                        String msg = String.valueOf(document.get("GameState"));

                        if(document.get("GameState").equals("none")){
                            displayData.setText("Loser !!!");
                        }else{
                            PlayerMessage.setVisibility(View.VISIBLE);
                            if(msg.equals(Winner)){
                                PlayerMessage.setText(winnersMessage);
                            }else{
                                PlayerMessage.setText(losersMessage);
                            }
                            displayData.setText(valueOf(document.get("GameState")));

                            if (Build.VERSION.SDK_INT >= 26){
                                vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE));
                            }else{
                                vibrator.vibrate(100);
                            }
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
        db.collection(collectionName).document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                    //Toast.makeText(getApplicationContext()," AccountBalance:"+balance, Toast.LENGTH_SHORT).show();

                                    if(threeToWinIs){
                                        double AmountWon = UserAmount * 0.3;
                                        double total = balance + AmountWon;

                                        Map<String, Object> updateTotal = new HashMap<>();
                                        updateTotal.put("AmountBalance",total);

                                        //update and verify account in API

                                        Accounts.document(userID).update(updateTotal);

                                        //Storing and deleting records
                                        //storeRecords(userID);
                                        onlineSQLRecords(userID);
                                    }else {
                                        double AmountWon = UserAmount * 0.2;
                                        double total = balance + AmountWon;

                                        Map<String, Object> updateTotal = new HashMap<>();
                                        updateTotal.put("AmountBalance",total);

                                        //update and verify account in API

                                        Accounts.document(userID).update(updateTotal);

                                        //Storing and deleting records
                                        storeRecords(userID);
                                        onlineSQLRecords(userID);
                                    }

                                }
                            });
                        }
                        else if(GameDocument.get("GameState").equals(Loser)){
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
                                    storeRecords(userID);
                                    onlineSQLRecords(userID);
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
        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Check if user balance is qualified for a particular game portal.
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();

                    db.collection(collectionName).document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public void updateSystemAccountV2(String userID){
        db.collection(collectionName).whereEqualTo("GameState","Winner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Check if user balance is qualified for a particular game portal.
                if (task.isSuccessful()) {
                    //Log.d("TAG", task.getResult().size() + "");
                    int numberOfDocuments = task.getResult().size();

                    db.collection(collectionName).document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                                                if (numberOfDocuments == 4){
                                                    double AmountWon = UserAmount * 0.2;
                                                    double total = systemBalance + AmountWon;

                                                    Map<String, Object> updateTotal = new HashMap<>();
                                                    updateTotal.put("AmountBalance",total);
                                                    Accounts.document("systemAccount").update(updateTotal);

                                                }else if (numberOfDocuments == 3){

                                                    double AmountWon = UserAmount * 0.5;
                                                    double total = systemBalance + AmountWon;

                                                    Map<String, Object> updateTotal = new HashMap<>();
                                                    updateTotal.put("AmountBalance",total);
                                                    Accounts.document("systemAccount2").update(updateTotal);

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
                                    //clearPortal(userID);
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
                                    //clearPortal(userID);
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

    //Background operation,
    public void onlineSQLRecords(String userId){
        Runnable sendInBackground = new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, insertInUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response.trim(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());
                    }
                }){
                    @Nullable
                    @org.jetbrains.annotations.Nullable
                    @Override
                    protected Map<String, String> getParams(){

                       db.collection(collectionName).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                               if (task.isSuccessful()){
                                   DocumentSnapshot document = task.getResult();

                                   game_state = String.valueOf(document.get("GameState"));
                                   game_amount = parseInt(String.valueOf(document.get("Amount")));
                                   gameCode = parseInt(String.valueOf(document.get("GameCode")));
                               }
                           }
                       });
                       if(threeToWinIs ){
                           Map<String,String> params = new HashMap<>();
                           params.put("user_id",userId);
                           params.put("collection_id",collectionName);
                           params.put("game_code", String.valueOf(gameCode));
                           params.put("game_state",game_state);
                           params.put("game_amount", String.valueOf(game_amount));
                           params.put("game_type",ThreeToWin );

                           return params;
                       }else{
                           Map<String,String> params = new HashMap<>();
                           params.put("user_id",userId);
                           params.put("collection_id",collectionName);
                           params.put("game_code", String.valueOf(gameCode));
                           params.put("game_state",game_state);
                           params.put("game_amount", String.valueOf(game_amount));
                           params.put("game_type",FourToWin);

                           return params;
                       }
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Activity4.this);
                requestQueue.add(stringRequest);
            }
        };
        Thread sendRequestThread = new Thread(sendInBackground);
        sendRequestThread.start();
    }

    //Background Operation
    public void getAccountBalance(String UserID){
        //Realtime updates.

        Handler handler;
        handler = new Handler();

        Runnable getAccountBalance = new Runnable(){

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
                                        displayBalance.setText(valueOf(document.get("AmountBalance")));
                                    }else {
                                        displayBalance.setText("N/A");
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

    public void getGameCode(String userID){
        db.collection(collectionName).document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        myCode = parseInt(valueOf(document.get("GameCode")));
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

    } //new

    public void playGameVisible(){

        db.collection(collectionName).whereEqualTo("GameStart","Ready").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int readyParticipant = task.getResult().size();
                    if (readyParticipant == 5){
                        playGame.setVisibility(View.VISIBLE);
                    }else {
                        playGame.setVisibility(View.GONE);
                    }
                }else {
                    Intent intent = new Intent(Activity4.this,Activity2.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Something went wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //background operation
    public void FirestoreGameRecords(String GameTypeRec){
        Runnable runnable = new Runnable() {
            String RecordsBackup = "Records";
            @Override
            public void run() {
                db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                               String AmountRec = String.valueOf(documentSnapshot.get("Amount"));
                               String GameCodeRec = String.valueOf(documentSnapshot.get("GameCode"));
                               String GameStateRec = String.valueOf(documentSnapshot.get("GameState"));
                               String userID = String.valueOf(documentSnapshot.get("UserID"));

                               //===================================== Keeping Records (Backup Records) ======================================
                               Map<String, Object> RecordBucket = new HashMap<>();
                               RecordBucket.put("Amount",AmountRec);
                               RecordBucket.put("GameCode",GameCodeRec);
                               RecordBucket.put("GameState",GameStateRec);
                               RecordBucket.put("UserID",userID);
                               RecordBucket.put("TimeRecorded",date);
                               RecordBucket.put("GameType",GameTypeRec);

                               db.collection(collectionName+RecordsBackup).document().set(RecordBucket).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(getApplicationContext(),"Game Back up Successful", Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(getApplicationContext(),"Game Back up failed", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       }else{
                           Toast.makeText(getApplicationContext(),task.getException().toString(), Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }
        };

        Thread gameRecords = new Thread(runnable);
        gameRecords.start();
    }
   /* public void SelectWinnersVersion2(String userID){

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
                    //problem is this implementation contains duplicates when selecting numbers.

                    for(int i= 0; i< numberPot.size() - 1; i++){
                        int randomIndex = random.nextInt(numberPot.size());
                        boolean toGameWinners,gameLoser;

                        //toGameWinners=gameWinners.add(numberPot.get(random.nextInt(numberPot.size())));
                        //gameLoser= numberPot.remove(numberPot.get(random.nextInt(numberPot.size())));

                        toGameWinners=gameWinners.add(numberPot.get(randomIndex));
                        gameLoser= numberPot.remove(numberPot.get(randomIndex));

                        if(toGameWinners == gameLoser){
                           gameWinners.add(numberPot.get(randomIndex));
                           numberPot.remove(numberPot.get(randomIndex));
                       }
                    }

                        Map<String, Object> gameWinner = new HashMap<>();
                        gameWinner.put("ChosenCode0" ,gameWinners.get(0));
                        gameWinner.put("ChosenCode1" ,gameWinners.get(1));
                        gameWinner.put("ChosenCode2" ,gameWinners.get(2));
                        gameWinner.put("ChosenCode3" ,gameWinners.get(3));

                        db.collection(collectionName).document(chosenOnes).set(gameWinner).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"code updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getApplicationContext(),"writing data failed", Toast.LENGTH_SHORT).show();
                            }
                        });


                    //different implementation of display
                    displayResults(userID);


                }else{
                    Toast.makeText(getApplicationContext(),"Some is wrong with Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
}