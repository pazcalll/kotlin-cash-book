package com.example.cashbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.example.cashbook.R
import com.example.cashbook.model.Cash

class CashAdapter (var mContext: Context, var resources: Int, var items : List<Cash>) :
    ArrayAdapter<Cash>(mContext, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view:View = layoutInflater.inflate(resources, null)

        val imageView: ImageView = view.findViewById(R.id.row_img)
        val titleTextView: TextView = view.findViewById(R.id.row_title)
        val descTextView: TextView = view.findViewById(R.id.row_desc)
        val dateTextView: TextView = view.findViewById(R.id.row_date)

        var mItem: Cash = items[position]
        imageView.setImageDrawable(mContext.resources.getDrawable(mItem.img))
        titleTextView.text = mItem.title
        for(i in mItem.title){
            if (i.equals('-') == true) {
                titleTextView.setTextColor(getColor(mContext, R.color.red))
                break
            }
            else if(i.equals('+') == true) {
                titleTextView.setTextColor(getColor(mContext, R.color.green))
                break
            }
        }
        descTextView.text = mItem.description
        dateTextView.text = mItem.date

        return view
    }
}