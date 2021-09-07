package com.example.cashbooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity {

    Button Activity3button;
    EditText userName3, editTextPhone, passwordActivity3, passwordActivity3Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Activity3button = (Button)findViewById(R.id.Activity3button);
        userName3 = (EditText)findViewById(R.id.userName3);
        editTextPhone = (EditText)findViewById(R.id.editTextPhone);
        passwordActivity3 = (EditText)findViewById(R.id.passwordActivity3);
        passwordActivity3Confirm = (EditText)findViewById(R.id.passwordActivity3Confirm);

       Activity3button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openUserDashboard();
           }
       });
    }
    public void openUserDashboard(){
        String username = userName3.getText().toString();
        //String password = PasswordText.getText().toString();

        Intent intent = new Intent(Activity3.this,Activity2.class);
        startActivity(intent);
    }
}