package com.example.cashbooster;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Activity2 extends AppCompatActivity {

    Button activity2Button;
    TextView textView2;
    private FirebaseAnalytics TestFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        activity2Button = (Button)findViewById(R.id.activity2Button);
        textView2 = (TextView)findViewById(R.id.textView2);
        TestFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        activity2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView2.setText(String.valueOf(TestFirebaseAnalytics));
            }
        });


    }
}