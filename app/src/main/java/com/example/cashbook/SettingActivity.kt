package com.example.cashbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.lang.Integer.parseInt

class SettingActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var save_pw : Button
    lateinit var back : Button
    lateinit var pw_old : EditText
    lateinit var pw_new : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        db = DBHelper(this)
        save_pw = findViewById(R.id.save_pw)
        back = findViewById(R.id.back)
        pw_old = findViewById(R.id.password_old)
        pw_new = findViewById(R.id.password_new)

        back.setOnClickListener(View.OnClickListener {
            startActivity(parentActivityIntent)
            finish()
        })

        save_pw.setOnClickListener(View.OnClickListener {
            val getSession = db.getSession()
            var userid = 0
            if (getSession.moveToFirst()){
                userid = parseInt(getSession.getString(2))
                val update = db.changePassword(userid, pw_old.text.toString(), pw_new.text.toString())
                if (update == true){
                    Toast.makeText(applicationContext, "Password telah diganti", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else Toast.makeText(applicationContext, "Aksi Gagal", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(parentActivityIntent)
        finish()
    }
}