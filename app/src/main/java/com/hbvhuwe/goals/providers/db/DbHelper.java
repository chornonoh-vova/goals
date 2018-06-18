package com.hbvhuwe.goals.providers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Goals.db";
    private static final int DB_VERSION = 3;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_GOALS);
        db.execSQL(DbContract.SQL_CREATE_STAGES);
        Log.d("db", "database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.SQL_DROP_GOALS);
        db.execSQL(DbContract.SQL_DROP_STAGES);
        onCreate(db);
    }
}
