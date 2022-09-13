package com.example.cashbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.lang.Integer.parseInt

class LoginActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var login : Button
    lateinit var register : Button
    lateinit var username : EditText
    lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DBHelper(this)

        username = findViewById(R.id.name)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login_button)
        register = findViewById(R.id.register_button)

//        register
        register.setOnClickListener(View.OnClickListener {
            val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(registerIntent)
            finish()
        })

        login.setOnClickListener(View.OnClickListener {
            val strUsername : String = username.text.toString()
            val strPassword : String = password.text.toString()

            val masuk : Boolean = db.checkLogin(strUsername, strPassword)
            val getUser = db.getUser(strUsername, strPassword)
            var id : Int = 0
            if (getUser.moveToFirst()){
                id = parseInt(getUser.getString(0))
            }
            if (masuk == true) {
                val updateSession  = db.upgradeSession("ada", 1, id)
                if (updateSession == true) {
                    Toast.makeText(applicationContext, "Berhasil Masuk", Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }
            }
            else {
                Toast.makeText(applicationContext, "Masuk Gagal", Toast.LENGTH_SHORT).show()
            }
        })
    }

}