package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestPayLaterMails
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLaterMails
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.PaylaterOrders
import java.util.*


/**
 * Created by Pranav on 2/2/2018.
 */
class RvAdapterPayLaterList(var context: Context) : RecyclerView.Adapter<RvAdapterPayLaterList.ViewHolder>() {
    var items = ArrayList<ResponcePayLaterMails>()
    var date = "0000/00/00"
    var count = 2
    private var database: SQLiteHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        database = SQLiteHelper(context)
        val layoutInflator = LayoutInflater.from(context)
        return ViewHolder(layoutInflator.inflate(R.layout.rv_pay_later_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder!!.mail_id.setText(items[position].paylaterId)
//        holder!!.amount.setText(/*String.format("%.2f",*/ items[position].total!!.toString()/*.toDouble())*/)

        if (position == items.size - 1) {
            RequestPayLaterMails(context, ResponceProcessorPayLaterLst()).getPayLaterMails(SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).POS_SHOP_ID, "0").trim(), count++)
        }

    }

    inner class ResponceProcessorPayLaterLst : ProcessResponcceInterphase<Array<ResponcePayLaterMails>> {
        override fun processResponce(responce: Array<ResponcePayLaterMails>) {
            if (responce != null && responce.size != 0) {
                val list = ArrayList(Arrays.asList(*responce))
                items.addAll(list)
                notifyItemRangeInserted(items.size - list.size, list.size)
            }

        }

    }


    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var amount: TextView
        var mail_id: TextView
        var layout_r: RelativeLayout


        init {

            amount = view.findViewById(R.id.amount)
            mail_id = view.findViewById(R.id.mail_id)
            layout_r = view.findViewById(R.id.layout_r)


            layout_r.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, PaylaterOrders::class.java)
                intent.putExtra("ID", items[adapterPosition].paylaterId.toString())
//                Toast.makeText(context,items[adapterPosition].paylaterId.toString(),Toast.LENGTH_SHORT).show()
                context.startActivity(intent)

            })


        }
    }

    fun set(list: ArrayList<ResponcePayLaterMails>) {
        items = list

    }
}