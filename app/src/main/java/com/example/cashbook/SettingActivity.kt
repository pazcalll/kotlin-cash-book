package com.example.cashbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class SettingActivity : AppCompatActivity() {
    lateinit var save_pw : Button
    lateinit var back : Button
    lateinit var pw_old : EditText
    lateinit var pw_new : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        save_pw = findViewById(R.id.save_pw)
        back = findViewById(R.id.back)
        pw_old = findViewById(R.id.password_old)
        pw_new = findViewById(R.id.password_new)

        back.setOnClickListener(View.OnClickListener {
            startActivity(parentActivityIntent)
            finish()
        })


    }
}