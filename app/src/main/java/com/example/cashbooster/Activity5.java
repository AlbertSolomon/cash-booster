package com.example.cashbooster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cashbooster.databinding.Activity5Binding;
import com.google.firebase.auth.FirebaseAuth;

public class Activity5 extends AppCompatActivity {

    TextView displayDataRecordsInfo;
    Activity5Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getCurrentUser().getUid();



















        //displayDataRecordsInfo = findViewById(R.id.displayDataRecordsInfo);
        //rv_Sqlite_Programs = findViewById(R.id.rv_Sqlite_Programs);

        //myCashBoosterRecords dbHelper = new myCashBoosterRecords(this);
        //SQLiteDatabase db = dbHelper.getReadableDatabase();

       // String myRecords[] = {"gameCode","portalRange","GameState","Amount"};
        //Cursor myCursor = db.query("records",myRecords,null,null,null,null,null);
        //myCursor.moveToNext();

       // displayDataRecordsInfo.setText("This feature is coming soon!!! ");



    }
}