package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestReceipts
import com.twixt.pranav.pos.Controller.Responce.ResponceReceipts
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Refund
import java.text.DecimalFormat
import java.util.*


/**
 * Created by Pranav on 11/27/2017.
 */
class RvAdapterReceipts(var context: Context) : RecyclerView.Adapter<RvAdapterReceipts.ViewHolder>() {
    var items = ArrayList<ResponceReceipts>()
    var date = "0000/00/00"
    var count = 2
    var key = ""
    private var database: SQLiteHelper? = null
    var formatter = DecimalFormat("#,###,##0.00")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        database = SQLiteHelper(context)
        val layoutInflator = LayoutInflater.from(context)
        return ViewHolder(layoutInflator.inflate(R.layout.rv_receipts, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val cu = database!!.getLanguage()
        cu.moveToFirst()
//        holder!!.day.setText(df.format(c.getTime()).split(" ")[0])

        if (!items[position].refundStatus.equals("0")) {
            holder!!.colorss.setColorFilter(ContextCompat.getColor(context, R.color.text_red))
        }

        if (position == 0) {
            holder!!.day.visibility = View.VISIBLE
            holder!!.view1.visibility = View.VISIBLE
            holder!!.day.setText(items[position].date.toString())
        }

        holder!!.time.setText(items[position].time.toString())
        if (items[position].paidAs.toString().equals("split"))
            holder!!.type.setText(cu.getString(cu.getColumnIndex("SPLIT")).toString() + " " + cu.getString(cu.getColumnIndex("PAYMENT")).toString())
        else
            holder!!.type.setText(items[position].payment_type.toString())
//        holder!!.cash.setText(cu.getString(cu.getColumnIndex("RS")).toString() + ". "  + formatter.format((String.format("%.2f",items[position].amount!!.toDouble())).replace(",",".").toDouble()) + " (#" + items[position].orderId + ")")
        holder!!.cash.setText(cu.getString(cu.getColumnIndex("RS")).toString() + ". "  + formatter.format(items[position].amount!!.toString().replace(",",".").toDouble()) + " (#" + items[position].orderId + ")")
        date = items[position].date.toString()

        try {
            if (items[position - 1].date.toString() != items[position].date.toString()) {
                holder!!.day.visibility = View.VISIBLE
                holder!!.view1.visibility = View.VISIBLE
                holder!!.day.setText(items[position].date.toString())
            }
            Log.e("checkkk...", items[position].date.toString() + " - " + items[position - 1].date.toString())
            if (position == items.size - 2) {
                if (key.equals("R"))
                    RequestReceipts(context, ResponceProcessorReceiptsAdapters()).getReceipts(SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).POS_SHOP_ID, "0"), count++)
                if (key.length > 7)
                    RequestReceipts(context, ResponceProcessorReceiptsAdapters()).getReceiptSearchDate(SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).POS_SHOP_ID, "0"), key, count++)
            }
        } catch (e: Exception) {
        }
        cu.close()
    }

    inner class ResponceProcessorReceiptsAdapters : ProcessResponcceInterphase<Array<ResponceReceipts>> {
        override fun processResponce(responce: Array<ResponceReceipts>) {
            val list = ArrayList(Arrays.asList(*responce))
            items.addAll(list)
            notifyItemRangeInserted(items.size - list.size, list.size)
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var day: TextView
        lateinit var type: TextView
        lateinit var cash: TextView
        lateinit var time: TextView
        lateinit var colorss: ImageView
        lateinit var view1: View
        lateinit var _layout: LinearLayout


        init {
            day = view.findViewById(R.id.day)
            cash = view.findViewById(R.id.cash)
            type = view.findViewById(R.id.type)
            time = view.findViewById(R.id.time)
            view1 = view.findViewById(R.id.view)
            _layout = view.findViewById(R.id._layout)
            colorss = view.findViewById(R.id.colorss)

            _layout.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, Refund::class.java)
                intent.putExtra("orderId", items[adapterPosition].orderId.toString())
                context.startActivity(intent)
            })
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun set(list: ArrayList<ResponceReceipts>, key: String) {
        items = list
        this.key = key
    }
}