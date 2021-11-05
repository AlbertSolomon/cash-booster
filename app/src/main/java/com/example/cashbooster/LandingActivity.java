package com.example.cashbooster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    Button enterPortals,enterPortals3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);



        enterPortals = findViewById(R.id.enterPortals);
        enterPortals3 = findViewById(R.id.enterPortals3);

        enterPortals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterArenas();
            }
        });

        enterPortals3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThreeWinners();
            }
        });
    }

    public void enterArenas(){
        Intent intent = new Intent(LandingActivity.this,Activity2.class);
        startActivity(intent);
    }

    public void goToThreeWinners(){
        Intent intent = new Intent(LandingActivity.this,ThreeToWin.class);
        startActivity(intent);
    }
}