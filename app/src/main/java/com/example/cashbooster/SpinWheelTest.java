package com.example.cashbooster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinWheelTest extends AppCompatActivity {

    LuckyWheel luckyWheel;
    Button start;
    List<WheelItem> wheelItemList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String collectionName = "GamePortals";
    boolean Winner;
    Random random = new Random();
    ArrayList<Integer> winnerIndex = new ArrayList<>();
    ArrayList<Integer> loserIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel_test);
        FirebaseApp.initializeApp(this);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setLooping(false);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getCurrentUser().getUid();

        settingTarget(currentUser);
        generateWheelItems();

        start = findViewById(R.id.start);
        luckyWheel = findViewById(R.id.luckyWheel);

        luckyWheel.addWheelItems(wheelItemList);
        luckyWheel.setTarget(1);


        luckyWheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                Toast.makeText(getApplicationContext(),"Target Reached", Toast.LENGTH_SHORT).show();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Winner){
                    luckyWheel.rotateWheelTo(1);
                }else{
                    luckyWheel.rotateWheelTo(4);
                    //create an array that holds indexes for loser on spinner then select randomly from it

                }

                //mediaPlayer.start();
            }
        });
    }

    private void generateWheelItems() {

        /*wheelItemList = new ArrayList<>();
        wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                "Winner"));
        wheelItemList.add(new WheelItem(Color.parseColor("#FF6347"),
                BitmapFactory.decodeResource(getResources(),R.drawable.game_over),
                "Loser"));
        wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                "Winner"));
        wheelItemList.add(new WheelItem(Color.parseColor("#FF6347"),
                BitmapFactory.decodeResource(getResources(),R.drawable.game_over),
                "Loser"));
        wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                "Winner"));*/

        boolean threeToWinIs = true;
        if (threeToWinIs){
            wheelItemList = new ArrayList<>();
            wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    ""));
            wheelItemList.add(new WheelItem(Color.parseColor("#FF6347"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.game_over),
                    ""));
            wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    ""));
            wheelItemList.add(new WheelItem(Color.parseColor("#FF6347"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.game_over),
                    ""));
            wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    ""));
        }else{
            wheelItemList = new ArrayList<>();
            wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    "Winner"));
            wheelItemList.add(new WheelItem(Color.parseColor("#FF6347"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.game_over),
                    "Loser"));
            wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    "Winner"));
            wheelItemList.add(new WheelItem(Color.parseColor("#FF6347"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    ""));
            wheelItemList.add(new WheelItem(Color.parseColor("#87C3F6"),
                    BitmapFactory.decodeResource(getResources(),R.drawable.winner),
                    "")); //<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>


            //game over <div>Icons made by <a href="https://www.flaticon.com/authors/darius-dan" title="Darius Dan">Darius Dan</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
        }
    }

    public void settingTarget(String userId){
        db.collection(collectionName).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    String winnerState = "Winner";
                    String stateOfGame = String.valueOf(document.get("GameState"));
                        if (stateOfGame.equals(winnerState)){
                            Winner = true;
                        }else{
                            Winner = false;
                        }
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
