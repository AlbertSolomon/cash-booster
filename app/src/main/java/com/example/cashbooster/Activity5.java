package com.example.cashbooster;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Activity5 extends AppCompatActivity {

    TextView displayDataRecordsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        displayDataRecordsInfo = findViewById(R.id.displayDataRecordsInfo);

        myCashBoosterRecords dbHelper = new myCashBoosterRecords(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String myRecords[] = {"gameCode","portalRange","GameState","Amount"};
        Cursor myCursor = db.query("records",myRecords,null,null,null,null,null);
        myCursor.moveToFirst();

        displayDataRecordsInfo.setText("This feature is coming soon!!! ");

       // for (int counter = 1 ; counter < 100; counter++){
           // displayDataRecordsInfo.setText(myCursor.getString(counter));
       // }

    }
}