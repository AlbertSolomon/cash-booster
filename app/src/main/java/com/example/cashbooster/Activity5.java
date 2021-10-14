package com.example.cashbooster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Activity5 extends AppCompatActivity {

    TextView displayDataRecordsInfo;
    RecyclerView rv_Sqlite_Programs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        displayDataRecordsInfo = findViewById(R.id.displayDataRecordsInfo);
        rv_Sqlite_Programs = findViewById(R.id.rv_Sqlite_Programs);

        myCashBoosterRecords dbHelper = new myCashBoosterRecords(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String myRecords[] = {"gameCode","portalRange","GameState","Amount"};
        Cursor myCursor = db.query("records",myRecords,"*",null,null,null,null);
        myCursor.moveToNext();

       // displayDataRecordsInfo.setText("This feature is coming soon!!! ");



    }
}