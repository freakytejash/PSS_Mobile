package com.example.pssmobile.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, chckpoinID TEXT, dateID TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        onCreate(db)
    }

    companion object {
        var DATABASE_NAME = "pssuser"
        const val KEY_CHCKPOINID = "chckpoinID"
        const val KEY_DATEID = "dateID"
        const val KEY_ID = "id"
        const val TABLE_NAME = "user"
    }
}