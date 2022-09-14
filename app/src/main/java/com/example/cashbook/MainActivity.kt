package com.example.cashbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Integer.parseInt
import kotlin.jvm.internal.Reflection.function


class MainActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var logout: Button
    lateinit var add_money: LinearLayout
    lateinit var subtract_money: LinearLayout
    lateinit var setting_money : LinearLayout
    lateinit var subtract : TextView
    lateinit var add : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
        logout = findViewById(R.id.logout)
        add_money = findViewById(R.id.money_add)
        subtract_money = findViewById(R.id.money_subtract)
        setting_money = findViewById(R.id.setting)
        subtract = findViewById(R.id.pengeluaran)
        add = findViewById(R.id.pemasukan)

        val checkSession : Boolean = db.sessionCheck("ada")
        if (checkSession == false) {
            val loginIntent : Intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
        val session = db.getSession()
        var user_id = 0
        if (session.moveToFirst()){
            user_id = parseInt(session.getString(session.getColumnIndex("userid")))
        }
        getIncome(user_id)

        getSubtract(user_id)

        logout()

        addMoney()

        subtractMoney()

        settingMoney()
    }

    fun logout() {
        logout.setOnClickListener(View.OnClickListener {
            var updateSession : Boolean = db.upgradeSession("kosong", 1, 0)
            if (updateSession == true) {
                Toast.makeText(applicationContext, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
                val loginIntent : Intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        })
    }

    fun addMoney(){
        add_money.setOnClickListener(View.OnClickListener {
            var addMoneyIntent = Intent(this@MainActivity, IncomeActivity::class.java)
            startActivity(addMoneyIntent)
            finish()
        })
    }

    fun subtractMoney() {
        subtract_money.setOnClickListener(View.OnClickListener {
            var subtractMoneyIntent = Intent(this@MainActivity, SpendingActivity::class.java)
            startActivity(subtractMoneyIntent)
            finish()
        })
    }

    fun settingMoney() {
        setting_money.setOnClickListener(View.OnClickListener {
            var settingIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(settingIntent)
            finish()
        })
    }

    fun getIncome(user_id:Int) {
        val user_amount = db.getAmount(user_id, "add")
        var length = user_amount.toString().length
        var user_amount_string = ""
        for (i in length downTo 1) {
            if (i % 3 == 0){
                user_amount_string = user_amount_string + "."
                user_amount_string = user_amount_string + user_amount.toString()[length-(i-1)-1]
            }
            else if(length-i >= 0) {
                user_amount_string = user_amount_string + user_amount.toString()[length-(i-1)-1]
            }
        }
        if (user_amount_string == "") add.setText("0")
        else add.setText(user_amount_string)
    }

    fun getSubtract(user_id: Int) {
        val user_amount = db.getAmount(user_id, "subtract")
        var length = user_amount.toString().length
        var user_amount_string = ""
        for (i in length downTo 1) {
            if (i % 3 == 0){
                user_amount_string = user_amount_string + "."
                user_amount_string = user_amount_string + user_amount.toString()[length-(i-1)-1]
            }
            else if(length-i >= 0) {
                user_amount_string = user_amount_string + user_amount.toString()[length-(i-1)-1]
            }
        }
        if (user_amount_string == "") subtract.setText("0")
        else subtract.setText(user_amount_string)
    }
}