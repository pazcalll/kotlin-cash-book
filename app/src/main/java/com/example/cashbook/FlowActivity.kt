package com.example.cashbook

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import com.example.cashbook.adapter.CashAdapter
import com.example.cashbook.model.Cash
import java.lang.Integer.parseInt

class FlowActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var userTransaction : Cursor
    lateinit var userSession : Cursor
    lateinit var back : Button
    var userId : Int = 0
    var list = mutableListOf<Cash>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)

        varSetting()

        listView()
        back()
    }
    fun varSetting() {
        db = DBHelper(this)
        userSession = db.getSession()
        back = findViewById(R.id.back)
    }
    fun listView() {
        if (userSession.moveToFirst()) {
            userId = parseInt(userSession.getString(2))
            userTransaction = db.getUserTransaction(userId)
            while (userTransaction.moveToNext()){
                if (userTransaction.getString(3) == "subtract"){
                    list.add(Cash("(-) "+userTransaction.getString(4), userTransaction.getString(2), R.drawable.sign_down_icon, userTransaction.getString(5)))
                }
                else {
                    list.add(Cash("(+) "+userTransaction.getString(4), userTransaction.getString(2), R.drawable.sign_up_icon_2, userTransaction.getString(5)))
                }
            }
            val listView = findViewById<ListView>(R.id.cash_list_view)
            listView.adapter = CashAdapter(this, R.layout.row_cash, list)
        }
    }
    fun back(){
        back.setOnClickListener(View.OnClickListener {
            startActivity(parentActivityIntent)
            finish()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(parentActivityIntent)
        finish()
    }
}