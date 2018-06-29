package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.twixt.pranav.pos.Controller.Responce.ResponcePaymentSplit
import com.twixt.pranav.pos.R
import de.hdodenhof.circleimageview.CircleImageView
import java.nio.file.WatchKey

/**
 * Created by Pranav on 11/29/2017.
 */
class RvAdapterSplitPayment(var context: Context) : RecyclerView.Adapter<RvAdapterSplitPayment.ViewHolder>() {

    internal var dataArray = arrayOfNulls<String>(10)
    var count = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(context)
        return ViewHolder(layoutInflator.inflate(R.layout.rv_split_payment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder!!.amount.setText(dataArray[position]!!.split("#")[0])
        holder!!.card_type.setText(dataArray[position]!!.split("#")[1])
    }

    override fun getItemCount(): Int {
        return count
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var amount: TextView
        var card_type: TextView

        init {
            amount = view.findViewById(R.id.amount)
            card_type = view.findViewById(R.id.card_type)
        }
    }

    fun set(s: String, i: Int) {
        dataArray[i] = s
        count = i + 1
    }

}