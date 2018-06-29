package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twixt.pranav.pos.Controller.Responce2Array.RefundedItems
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import java.text.DecimalFormat

/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterReceiptsRefundItems(var context: Context) : RecyclerView.Adapter<RvAdapterReceiptsRefundItems.ViewHolder>() {
    private var database: SQLiteHelper? = null
    lateinit var refunded_item: List<RefundedItems>
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
        val cu = database!!.getLanguage()
        cu.moveToFirst()
        holder.checkBox.setText(refunded_item[position].itemName)
        holder.amount.setText(refunded_item[position].quantity + " x " + formatter.format(refunded_item[position].price!!.toDouble()))
        holder.refunded.setText(cu.getString(cu.getColumnIndex("R_REFUNDED")).toString())
        holder.refunded.visibility = View.VISIBLE


        /*if (print_items.length == 0)
            print_items = print_items + (refunded_item[position].itemName + " ## " +
                    refunded_item[position].quantity + " x " + refunded_item[position].price + " ## " +
                    String.format("%.2f", refunded_item[position].quantity!!.toDouble() * refunded_item[position].price!!.toDouble()))
        else
            print_items = print_items + (refunded_item[position].itemName + " ## " +
                    refunded_item[position].quantity + " x " + refunded_item[position].price + " ## " +
                    String.format("%.2f", refunded_item[position].quantity!!.toDouble() * refunded_item[position].price!!.toDouble())) + " !& "*/
        cu.close()
    }


    override fun getItemCount(): Int {
        return refunded_item.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var amount: TextView
        var checkBox: TextView
        lateinit var refunded: TextView


        init {
            amount = view.findViewById(R.id.amount)
            checkBox = view.findViewById(R.id.checkBox)
            refunded = view.findViewById(R.id.refunded)
        }
    }

    fun getItems(): String {
        return print_items
    }

    fun set(refunded_items: List<RefundedItems>) {
        refunded_item = refunded_items
    }

    /*fun set(orderItems: List<OrderItem>) {

    }*/
}