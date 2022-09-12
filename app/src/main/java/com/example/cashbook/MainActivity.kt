package com.example.cashbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.internal.Reflection.function


class MainActivity : AppCompatActivity() {
    lateinit var db : DBHelper;
    lateinit var logout: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
        logout = findViewById(R.id.logout)

        val checkSession : Boolean = db.sessionCheck("ada")
        if (checkSession == false) {
            val loginIntent : Intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        logout.setOnClickListener(View.OnClickListener {
            var updateSession : Boolean = db.upgradeSession("kosong", 1)
            if (updateSession == true) {
                Toast.makeText(applicationContext, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
                val loginIntent : Intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        })
    }
}