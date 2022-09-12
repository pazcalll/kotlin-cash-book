package com.example.cashbook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, "cash_book.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("CREATE TABLE session(id integer PRIMARY KEY, login text)")
            db.execSQL("CREATE TABLE user(id integer PRIMARY KEY AUTOINCREMENT, username text, password text)")
            db.execSQL("INSERT INTO session(id, login) VALUES(1, 'kosong')")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS session")
            db.execSQL("DROP TABLE IF EXISTS user")
            onCreate(db)
        }
    }

    // check session
    fun sessionCheck(sessionVal: String) : Boolean {
        var db : SQLiteDatabase = this.readableDatabase
        var cursor : Cursor = db.rawQuery("SELECT * FROM session where login = ?", arrayOf<String>(sessionVal))
        return cursor.count > 0
    }

    // upgradesession
    fun upgradeSession(sessionValues : String, id: Int) : Boolean {
        var db : SQLiteDatabase = this.writableDatabase
        var contentValues : ContentValues = ContentValues()
        contentValues.put("login", sessionValues)
        var update : Int = db.update("session", contentValues, "id="+id, null)
        return update != -1
    }

    // insert User
    fun insertUser(username: String, password: String) : Boolean {
        var db : SQLiteDatabase = this.writableDatabase
        var contentValues : ContentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        var insert : Long = db.insert("user", null, contentValues)
        return insert == -1L
    }

    // login check
    fun checkLogin(username: String, password: String) : Boolean {
        var db : SQLiteDatabase = this.readableDatabase
        var cursor : Cursor = db.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?", arrayOf<String>(username, password))
        return cursor.count > 0
    }
}