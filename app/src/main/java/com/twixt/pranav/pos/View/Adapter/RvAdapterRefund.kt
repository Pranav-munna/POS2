package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twixt.pranav.pos.Controller.Responce2Array.Payments
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import java.text.DecimalFormat

/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterRefund(var context: Context) : RecyclerView.Adapter<RvAdapterRefund.ViewHolder>() {
    private var database: SQLiteHelper? = null
    lateinit var payment: List<Payments>
    var formatter = DecimalFormat("#,###,##0.00")


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        database = SQLiteHelper(context)
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_refund, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cu = database!!.getLanguage()
        cu.moveToFirst()

        if (payment[position].returnStatus == 1) {
            holder.refunded.setText(cu.getString(cu.getColumnIndex("R_REFUNDED")).toString())
            holder.refunded.visibility = View.VISIBLE
            holder.rate.setText(cu.getString(cu.getColumnIndex("RS")).toString() + ".-" + formatter.format(payment[position].amount!!.toDouble()))
        } else
            holder.rate.setText(cu.getString(cu.getColumnIndex("RS")).toString() + "." + formatter.format(payment[position].amount!!.toDouble()))

        for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).PAY_COUNT, 1) - 1)) {
            val cu = database!!.paymentTypedatas(i)
            cu.moveToFirst()
            if (cu.getString(cu.getColumnIndex("PAYMENT_ID")).toString().equals(payment[position].paymentTypeId.toString())) {
                holder.cash.setText(cu.getString(cu.getColumnIndex("PAYMENT_NAME")).toString())
            }
            cu.close()
        }
        cu.close()
    }


    override fun getItemCount(): Int {
        return payment.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rate: TextView
        var cash: TextView
        var amount_due: TextView
        lateinit var refunded: TextView

        init {
            rate = view.findViewById(R.id.rate)
            cash = view.findViewById(R.id.cash)
            amount_due = view.findViewById(R.id.amount_due)
            refunded = view.findViewById(R.id.refunded)

            val cu = database!!.getLanguage()
            cu.moveToFirst()
            amount_due.setText(cu.getString(cu.getColumnIndex("AMOUNT")).toString())

            cu.close()
        }

    }

    fun set(payments: List<Payments>) {
        payment = payments
    }
}