package com.example.cashbook

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class IncomeActivity : AppCompatActivity() {
    lateinit var dateSelector : TextView
    lateinit var back : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        back = findViewById(R.id.back)
        dateSelector = findViewById(R.id.selector_date)
        dateSelector.setFocusable(false);
        dateSelector.setCursorVisible(false);

        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        dateSelector.setOnClickListener(View.OnClickListener {
            val dialog = DatePickerDialog(this@IncomeActivity, AlertDialog.THEME_HOLO_DARK, DatePickerDialog.OnDateSetListener{ view, year, month,
                    dayOfMonth ->
                    var month = month
                    month = month + 1
                    val date = "$dayOfMonth/$month/$year"
                    dateSelector.setText(date)
                }, year, month, day
            )
            dialog.show()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(parentActivityIntent)
        finish()
    }
}