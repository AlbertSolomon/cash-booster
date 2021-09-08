package com.example.cashbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button button, signupButton;
    EditText PasswordText, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        signupButton = (Button)findViewById(R.id.signupButton);
        userName=(EditText)findViewById(R.id.userName);
        PasswordText=(EditText)findViewById(R.id.PasswordText);

        //final EditText PasswordText = (EditText)findViewById(R.id.PasswordText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity();
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingUp();
            }
        });
    }

    public void openNextActivity(){
        String username = userName.getText().toString();
        String password = PasswordText.getText().toString();

        Intent intent = new Intent(MainActivity.this,Activity2.class);
        startActivity(intent);
    }

    public void signingUp(){
        Intent intent = new Intent(MainActivity.this,Activity3.class);
        startActivity(intent);
    }
}