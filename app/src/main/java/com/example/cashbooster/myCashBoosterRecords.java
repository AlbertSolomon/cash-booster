package com.example.cashbooster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class myCashBoosterRecords extends SQLiteOpenHelper {

    public myCashBoosterRecords(@Nullable @org.jetbrains.annotations.Nullable Context context) {
        super(context, "GameRecords", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table records (recordID text, gameCode text, portalRange text, GameState text, Amount text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists records");
        onCreate(sqLiteDatabase);
    }
}
