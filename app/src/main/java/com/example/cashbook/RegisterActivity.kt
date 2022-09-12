package com.example.cashbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var login : Button
    lateinit var register : Button
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var c_password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = DBHelper(this)

        username = findViewById(R.id.name_regist)
        password = findViewById(R.id.password_regist)
        c_password = findViewById(R.id.c_password_regist)
        login = findViewById(R.id.login_button_regist)
        register = findViewById(R.id.register_button_regist)

//        login
        login.setOnClickListener(View.OnClickListener {
            val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        })

//        register
        register.setOnClickListener(View.OnClickListener {
            val strUsername : String = username.text.toString()
            val strPassword : String = password.text.toString()
            val strCPassword : String = c_password.text.toString()

            if (strPassword.equals(strCPassword) == true) {
                val signup = db.insertUser(strUsername, strPassword)
                if (signup == true) {
                    Toast.makeText(applicationContext, "Daftar Berhasil", Toast.LENGTH_SHORT).show()
                    val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }
                else {
                    Toast.makeText(applicationContext, "Daftar Gagal", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(applicationContext, "Password Tidak Sesuai", Toast.LENGTH_SHORT).show()
            }
        })
    }
}