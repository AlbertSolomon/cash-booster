package com.example.cashbooster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class cashBoosterService extends Service {
    String userID,gameResult,ready,collectionName;
    Integer gameAmount,gameCode;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences testSp = getApplicationContext().getSharedPreferences("gameCollections", Context.MODE_PRIVATE);
        collectionName = testSp.getString("collectionName","");

        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        userID = document.getId();
                        gameResult = String.valueOf(document.get("GameState"));
                        ready = String.valueOf(document.get("GameStart"));
                        gameAmount = parseInt(String.valueOf(document.get("Amount")));
                        gameCode = parseInt(String.valueOf(document.get("GameCode")));

                        //Creating a JSON array/which will be sent on the server 000webHost (CashBooster API site)
                        JsonOfGamePortals myGsonObject = new JsonOfGamePortals();
                        myGsonObject.setUserID(userID);
                        myGsonObject.setGameState(gameResult);
                        myGsonObject.setGameStart(ready);
                        myGsonObject.setGameAmount(gameAmount);
                        myGsonObject.setGameCode(gameCode);

                        Gson gamePortalJson = new Gson();

                        ArrayList<JsonOfGamePortals> portal = new ArrayList<>();
                        portal.add(myGsonObject);
                        String responseArray = gamePortalJson.toJson(portal);

                        //writing to a file.json to be uploaded on a server...
                        try {
                            FileWriter fileWriter = new FileWriter(collectionName+".json");
                            fileWriter.write(responseArray);
                            fileWriter.flush();
                            fileWriter.close();

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        //Send Json Array to server 000webHost (CashBooster API site) https://cashbooster.000webhostapp.com/
                        String url = "https://cashbooster.000webhostapp.com/";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,response -> System.out.println("posting successful "),
                                error -> System.out.println("failed ")){

                        };
                        
                    }
                }
            }
        });

        return START_STICKY;
    }
}
