package com.example.cashbooster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cashbooster.databinding.Activity5Binding;
import com.google.firebase.auth.FirebaseAuth;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.*;
import static java.lang.Integer.parseInt;

public class Activity5 extends AppCompatActivity {

    TextView displayDataRecordsInfo;
    Activity5Binding binding;
    String dataUrl = "https://cashbooster.000webhostapp.com/showGames.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = Activity5Binding.inflate(getLayoutInflater());
        //binding = Activity5Binding.inflate(LayoutInflater.from(getParent()),null,false);
        setContentView(binding.getRoot());

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getCurrentUser().getUid();

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        //onlineSQLRecords(currentUser);


        String[] amount = {"2000","5000"};
        String[] gameCodes = {"9898","3435"};
        String[] gameStates = {"Winner","Loser"};
        String[] gameType = {"3Winners", "4Winners"};


        ArrayList<Records> userArrayList = new ArrayList<>();
        for (int i = 0; i < gameCodes.length; i++){

            Records records = new Records(amount[i],gameCodes[i],gameStates[i],gameType[i]);
            userArrayList.add(records);
        }

        ListAdapter listAdapter = new ListAdapter(Activity5.this,userArrayList);
        binding.myRecords.setAdapter(listAdapter);

        //displayDataRecordsInfo = findViewById(R.id.displayDataRecordsInfo);
        //rv_Sqlite_Programs = findViewById(R.id.rv_Sqlite_Programs);

        //myCashBoosterRecords dbHelper = new myCashBoosterRecords(this);
        //SQLiteDatabase db = dbHelper.getReadableDatabase();

       // String myRecords[] = {"gameCode","portalRange","GameState","Amount"};
        //Cursor myCursor = db.query("records",myRecords,null,null,null,null,null);
        //myCursor.moveToNext();

       // displayDataRecordsInfo.setText("This feature is coming soon!!! ");

    }

    //Background operation,
    /*public void onlineSQLRecords(String userId){
        Runnable sendInBackground = new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, dataUrl ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response.trim(), Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //String userData = jsonObject.optString("GamePorts");

                                    JSONArray results = jsonObject.getJSONArray("GamePorts");


                                    ArrayList<String> Game_State = new ArrayList<>();
                                    ArrayList<String> Game_Type = new ArrayList<>();
                                    ArrayList<String> Game_Code = new ArrayList<>();
                                    ArrayList<String> Game_Amount = new ArrayList<>();
                                    for (int jsonCounter = 0; jsonCounter < results.length(); jsonCounter++){
                                        JSONObject result = results.getJSONObject(jsonCounter);

                                        String gameState = result.getString("game_state");
                                        String gameAmount = result.getString("game_amount");
                                        String gameCode = result.getString("game_code");
                                        String gameType = result.getString("game_type");


                                        Game_Amount.add(gameAmount);
                                        Game_Code.add(gameCode);
                                        Game_Type.add(gameType);
                                        Game_State.add(gameState);

                                    }

                                    ArrayList<Records> userArrayList = new ArrayList<>();
                                    for (int i = 0; i < Game_Code.size(); i++){

                                        Records records = new Records(Game_Amount.get(i),Game_Code.get(i),Game_State.get(i),Game_Type.get(i));
                                        userArrayList.add(records);
                                    }

                                    ListAdapter listAdapter = new ListAdapter(Activity5.this,userArrayList);
                                    binding.myRecords.setAdapter(listAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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
                        Map<String,String> params = new HashMap<>();
                            params.put("user_id",userId);
                            return params;

                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Activity5.this);
                requestQueue.add(stringRequest);
            }
        };
        Thread sendRequestThread = new Thread(sendInBackground);
        sendRequestThread.start();
    }*/

}