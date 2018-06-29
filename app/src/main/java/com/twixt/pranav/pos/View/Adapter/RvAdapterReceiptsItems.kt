package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twixt.pranav.pos.Controller.Responce2Array.OrderItem
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import java.text.DecimalFormat

/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterReceiptsItems(var context: Context) : RecyclerView.Adapter<RvAdapterReceiptsItems.ViewHolder>() {
    private var database: SQLiteHelper? = null
    lateinit var orderItem: List<OrderItem>
    var print_items = ""
    var formatter = DecimalFormat("#,###,##0.00")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        database = SQLiteHelper(context)
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_receipts_items, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.setText(orderItem[position].itemName)
        holder.amount.setText(orderItem[position].quantity + " x " + formatter.format(orderItem[position].price!!.toDouble()))

        var i = 0
        var modifier = ""
        while (i < orderItem[position].modifiers!!.size) {
            modifier = modifier + orderItem[position].modifiers!![i].modifier_name + "(" + formatter.format(orderItem[position].modifiers!![i].amount.toString().toDouble() )+ "),"
        i++
        }

        holder.tv_modifier.setText(modifier)

        /* if (print_items.length == 0)
             print_items = print_items + (orderItem[position].itemName + " ## " +
                     orderItem[position].quantity + " x " + orderItem[position].price + " ## " +
                     String.format("%.2f", orderItem[position].quantity!!.toDouble() * orderItem[position].price!!.toDouble()))
         else
             print_items = print_items + (orderItem[position].itemName + " ## " +
                     orderItem[position].quantity + " x " + orderItem[position].price + " ## " +
                     String.format("%.2f", orderItem[position].quantity!!.toDouble() * orderItem[position].price!!.toDouble())) + " !& "*/
    }


    override fun getItemCount(): Int {
        return orderItem.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var amount: TextView
        var checkBox: TextView
        lateinit var tv_modifier: TextView


        init {
            amount = view.findViewById(R.id.amount)
            checkBox = view.findViewById(R.id.checkBox)
            tv_modifier = view.findViewById(R.id.tv_modifier)
        }
    }

    fun set(orderItems: List<OrderItem>) {
        orderItem = orderItems
    }

    fun getItemsRefund(): String {
        return print_items
    }
}