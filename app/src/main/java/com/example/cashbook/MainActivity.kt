package com.example.cashbook

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.lang.Integer.parseInt
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var logout: Button
    lateinit var add_money: LinearLayout
    lateinit var subtract_money: LinearLayout
    lateinit var setting_money : LinearLayout
    lateinit var detail_money : LinearLayout
    lateinit var subtract : TextView
    lateinit var add : TextView
    lateinit var spinner : Spinner
    lateinit var spinner2 : Spinner
    lateinit var filter : Button
    lateinit var mpchart : LineChart

    var user_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
        logout = findViewById(R.id.logout)
        add_money = findViewById(R.id.money_add)
        subtract_money = findViewById(R.id.money_subtract)
        setting_money = findViewById(R.id.setting)
        detail_money = findViewById(R.id.info_money)
        subtract = findViewById(R.id.pengeluaran)
        add = findViewById(R.id.pemasukan)
        spinner = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)
        filter = findViewById(R.id.filter)
        mpchart = findViewById(R.id.mpchart)

        val checkSession : Boolean = db.sessionCheck("ada")
        if (checkSession == false) {
            val loginIntent : Intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
        val session = db.getSession()
        if (session.moveToFirst()){
            user_id = parseInt(session.getString(session.getColumnIndex("userid")))
        }

        spinner1()

        spinner2()

        filter()

        getInfo()

        logout()

        addMoney()

        subtractMoney()

        settingMoney()

        filter.performClick()
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

    fun getInfo() {
        detail_money.setOnClickListener(View.OnClickListener {
            var detailIntent = Intent(this@MainActivity, FlowActivity::class.java)
            startActivity(detailIntent)
            finish()
        })
    }

    fun getIncome(user_id:Int, month:Int, year: Int) {
        val user_amount = db.getAmount(user_id, "add", month, year)
        var length = user_amount.toString().length
        var user_amount_string = ""
        for (i in length downTo 1) {
            if (i % 3 == 0){
                if (i != length) user_amount_string = user_amount_string + "."
                user_amount_string = user_amount_string + user_amount.toString()[length-(i-1)-1]
            }
            else if(length-i >= 0) {
                user_amount_string = user_amount_string + user_amount.toString()[length-(i-1)-1]
            }
        }
        if (user_amount_string == "") add.setText("0")
        else add.setText(user_amount_string)
    }

    fun getSubtract(user_id: Int, month: Int, year: Int) {
        val user_amount = db.getAmount(user_id, "subtract", month, year)
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

    fun spinner1() {
        val adapter : ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.months, com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        spinner.setSelection(month)
    }

    fun mpChartMake() {
        mpchart.clear()
        mpchart.notifyDataSetChanged()
        mpchart.invalidate()

        var listAdd = arrayListOf<Entry>()
        var listSubtract = arrayListOf<Entry>()

        val userTransaction = db.getUserTransaction(user_id, spinner.selectedItemPosition, spinner2.selectedItem.toString().toInt())
        for (i in 0 .. 31) {
            listAdd.add(Entry(i.toFloat(), 0F))
            listSubtract.add(Entry(i.toFloat(), 0F))
        }
        while (userTransaction.moveToNext()){
            var transactionDate = SimpleDateFormat("d/MM/yyyy").parse(userTransaction.getString(5))
            if (transactionDate.month == spinner.selectedItemPosition){
                if (userTransaction.getString(3).equals("add")) {
                    var tmpAdd = listAdd[transactionDate.date]
                    if (tmpAdd.y == 0F)
                        listAdd.set(transactionDate.date, Entry(tmpAdd.x, userTransaction.getString(4).toFloat()))
                    else
                        listAdd.set(transactionDate.date, Entry(tmpAdd.x, userTransaction.getString(4).toFloat()+tmpAdd.y))
                }
                else if(userTransaction.getString(3).equals("subtract")) {
                    if (listSubtract[transactionDate.date].y == 0F)
                        listSubtract[transactionDate.date].y = userTransaction.getString(4).toFloat()
                    else
                        listSubtract[transactionDate.date].y += userTransaction.getString(4).toFloat()
                }
            }
            println(transactionDate.month == spinner.selectedItemPosition)
            println(transactionDate.month)
            println(spinner.selectedItemPosition)
        }
        val lineAdd = LineDataSet(listAdd, "Pemasukan")
        lineAdd.color = resources.getColor(R.color.green)
        val lineSubtract = LineDataSet(listSubtract, "Pengeluaran")
        lineSubtract.color = resources.getColor(R.color.red)

        val datasets = arrayListOf<ILineDataSet>()
        datasets.add(lineAdd)
        datasets.add(lineSubtract)

        val lineData = LineData(datasets)

        mpchart.data = lineData
        mpchart.labelFor
        mpchart.xAxis.labelCount = 31
        mpchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        mpchart.setBackgroundColor(resources.getColor(R.color.white))
    }

    fun spinner2() {
        var tmpSpinner2 = arrayListOf<String>()
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        for (i in 2000 .. 2045){
            tmpSpinner2.add(i.toString())
        }
        var adapterSpinner2 = ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, tmpSpinner2)
        spinner2.adapter = adapterSpinner2
        spinner2.setSelection(adapterSpinner2.getPosition(year.toString()))
    }

    fun filter() {
        filter.setOnClickListener(View.OnClickListener {
            getIncome(user_id, spinner.selectedItemPosition, spinner2.selectedItem.toString().toInt())
            getSubtract(user_id, spinner.selectedItemPosition, spinner2.selectedItem.toString().toInt())
            mpChartMake()
        })
    }
}