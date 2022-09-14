package com.example.cashbook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "cash_book.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("CREATE TABLE session(id integer PRIMARY KEY, login text, userid integer)")
            db.execSQL("CREATE TABLE user(id integer PRIMARY KEY AUTOINCREMENT, username text, password text)")
            db.execSQL("CREATE TABLE money(id integer PRIMARY KEY AUTOINCREMENT, userid integer, note text, activity text, amount biginteger, datetime timestamp)")
            db.execSQL("INSERT INTO session(id, login, userid) VALUES(1, 'kosong', 0)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS session")
            db.execSQL("DROP TABLE IF EXISTS user")
            db.execSQL("DROP TABLE IF EXISTS money")
            onCreate(db)
        }
    }

    // check session
    fun sessionCheck(sessionVal: String) : Boolean {
        var db : SQLiteDatabase = this.readableDatabase
        var cursor : Cursor = db.rawQuery("SELECT * FROM session where login = ?", arrayOf<String>(sessionVal))
        return cursor.count > 0
    }

//    get session
    fun getSession() : Cursor {
        var db : SQLiteDatabase = this.readableDatabase
        var cursor : Cursor = db.rawQuery("SELECT * FROM session where login = 'ada'", null)

        return cursor
    }

    //    get user
    fun getUser(strUsername: String, strPassword: String): Cursor {
        var db: SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT * FROM user where username = '$strUsername' and password = '$strPassword'", null)
    }

    // uopdate session
    fun upgradeSession(sessionValues : String, id: Int, userid : Int) : Boolean {
        var db : SQLiteDatabase = this.writableDatabase
        var contentValues : ContentValues = ContentValues()
        contentValues.put("login", sessionValues)
        contentValues.put("userid", userid)
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
        return insert != -1L || insert != 0L
    }

    // login check
    fun checkLogin(username: String, password: String) : Boolean {
        var db : SQLiteDatabase = this.readableDatabase
        var cursor : Cursor = db.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?", arrayOf<String>(username, password))
        return cursor.count > 0
    }

//    save income
    fun saveAmount(userid: Int, activity: String, note: String, amount: Int, date: String): Boolean {
        var db = this.readableDatabase
        var contentValues : ContentValues = ContentValues()
        contentValues.put("userid", userid)
        contentValues.put("note", note)
        contentValues.put("activity", activity)
        contentValues.put("amount", amount)
        contentValues.put("datetime", date)
        var cursor = db.insert("money", null, contentValues)
        return cursor == 1L || cursor != 0L
    }

    fun getAmount(userid: Int, activity: String) : Int {
        var db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM money WHERE userid = $userid and activity = '$activity'", null)
        var res = 0
        while (cursor.moveToNext()){
            res += Integer.parseInt(cursor.getString(4))
        }
        return res
    }

    fun changePassword(userid: Int, pw_old: String, pw_new: String) : Boolean {
        var db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM user WHERE id = $userid and password = '$pw_old'", null)
        if (cursor.count > 0) {
            var contentValues : ContentValues = ContentValues()
            contentValues.put("password", pw_new)
            var update : Int = db.update("user", contentValues, "id="+userid, null)
            return update == 1
        }
        return false
    }
}