package com.example.cashbook

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class IncomeActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    lateinit var dateSelector : EditText
    lateinit var back : Button
    lateinit var submit : Button
    lateinit var amount : EditText
    lateinit var note : EditText
    lateinit var strAmount : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        db = DBHelper(this)
        back = findViewById(R.id.back)
        submit = findViewById(R.id.submit_pemasukan)
        dateSelector = findViewById(R.id.selector_date)
        dateSelector.setFocusable(false);
        dateSelector.setCursorVisible(false);
        amount = findViewById(R.id.amount)
        strAmount = findViewById(R.id.str_amount)

        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val maxYear = Calendar.getInstance()
        maxYear.set(2045, 11, 31)
        val minYear = Calendar.getInstance()
        minYear.set(2000, 0, 1)
        val dialog = DatePickerDialog(this@IncomeActivity, AlertDialog.THEME_HOLO_DARK,
            DatePickerDialog.OnDateSetListener{ view, year, month,
            dayOfMonth ->
                var month = month + 1
                lateinit var date : String
                if (month < 10) {
                    date = "$dayOfMonth/0$month/$year"
                }else{
                    date = "$dayOfMonth/$month/$year"
                }
                dateSelector.setText(date)
            },
            year, month, day
        )
        dialog.datePicker.minDate = minYear.timeInMillis
        dialog.datePicker.maxDate = maxYear.timeInMillis

        dateSelector.setOnClickListener(View.OnClickListener {
            dialog.show()
        })

        amount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var user_amount = amount.text.toString()
                var length = user_amount.length
                var user_amount_string = ""
                for (i in length downTo 1) {
                    if (i % 3 == 0){
                        if (i != length) user_amount_string = user_amount_string + "."
                        user_amount_string = user_amount_string + user_amount[length-(i-1)-1]
                    }
                    else if(length-i >= 0) {
                        user_amount_string = user_amount_string + user_amount[length-(i-1)-1]
                    }
                }
                if (user_amount_string == "") strAmount.setText("Rp. 0")
                else strAmount.setText("Rp. "+user_amount_string)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        submit.setOnClickListener(View.OnClickListener {
            Log.i("cash", dateSelector.text.toString())
            amount = findViewById(R.id.amount)
            note = findViewById(R.id.note)

            val amount_money = amount.text.toString()
            val note_additional = note.text.toString()
            val date = dateSelector.text.toString()
            val getUser = db.getSession()
            if (getUser.moveToFirst()){
                var userid = Integer.parseInt(getUser.getString(2))
                var status = db.saveAmount(userid, "add", note_additional, Integer.parseInt(amount_money), date)
                if (status == true) Toast.makeText(applicationContext, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                else Toast.makeText(applicationContext, "Aksi Gagal", Toast.LENGTH_SHORT).show()
            }
            onBackPressed()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(parentActivityIntent)
        finish()
    }
}