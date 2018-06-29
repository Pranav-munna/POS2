package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLaterRecieptDetails
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.PaymentForLater
import com.twixt.pranav.pos.View.Activity.ViewPayLaterItems
import java.text.DecimalFormat
import java.util.*


/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterPayLaterOrders(var context: Context) : RecyclerView.Adapter<RvAdapterPayLaterOrders.ViewHolder>() {
    var items = ArrayList<ResponcePayLaterRecieptDetails>()
    private lateinit var databasePayLaterorder: SQLiteHelper
    var formatter = DecimalFormat("#,###,##0.00")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_paylater_order, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var cu_lan = databasePayLaterorder.language
        cu_lan.moveToFirst()

        holder.receipt_id.setText("#" + items[position].orderId)
        holder.date.setText(items[position].date)
        holder.cashier.setText("Casier : " + items[position].cashier)
        holder.total_amount.setText(formatter.format(items[position].total!!.toDouble()))

        holder.btn_pay.setText(cu_lan.getString(cu_lan.getColumnIndex("PAY")).toString())
        holder.btn_view.setText(cu_lan.getString(cu_lan.getColumnIndex("VIW")).toString())

        cu_lan.close()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var receipt_id: TextView
        var date: TextView
        var cashier: TextView
        var total: TextView
        var total_amount: TextView
        var btn_view: Button
        var btn_pay: Button
//        var cu: Cursor

        init {
            receipt_id = view.findViewById(R.id.receipt_id)
            date = view.findViewById(R.id.date)
            cashier = view.findViewById(R.id.cashier)
            total = view.findViewById(R.id.total)
            total_amount = view.findViewById(R.id.total_amount)
            btn_view = view.findViewById(R.id.btn_view)
            btn_pay = view.findViewById(R.id.btn_pay)

            databasePayLaterorder = SQLiteHelper(context)
//            cu = databasePayLaterorder.language
//            cu.moveToFirst()


            btn_view.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, ViewPayLaterItems::class.java)
                intent.putExtra("ORDERID", items[adapterPosition].orderId)
                context.startActivity(intent)
            })

            btn_pay.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, PaymentForLater::class.java)
                intent.putExtra("TOTAL", items[adapterPosition].total)
                intent.putExtra("ORDER_ID", items[adapterPosition].orderId)
                intent.putExtra("TAX", items[adapterPosition].tax)
                context.startActivity(intent)
            })

//            cu.close()
        }
    }

    fun set(list: ArrayList<ResponcePayLaterRecieptDetails>, databasePayLaterOrders: SQLiteHelper) {
        databasePayLaterorder = databasePayLaterOrders
        items = list
    }

}