package com.example.pssmobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "userdata";
    public static final String KEY_CHCKPOINID = "chckpoinID";
    public static final String KEY_DATEID = "dateID";
    public static final String KEY_ID = "id";
    public static final String TABLE_NAME = "user";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, chckpoinID TEXT, dateID TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
