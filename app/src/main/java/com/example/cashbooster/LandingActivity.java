package com.example.cashbooster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {
    Button enterPortals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        enterPortals = findViewById(R.id.enterPortals);

        enterPortals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterArenas();
            }
        });
    }

    public void enterArenas(){
        Intent intent = new Intent(LandingActivity.this,Activity2.class);
        startActivity(intent);
    }
}