package com.twixt.pranav.pos.View.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentPayment

/**
 * Created by Pranav on 1/15/2018.
 */
class RvAdapterDiscount(var context: Context, val fragmentPayment: FragmentPayment) : RecyclerView.Adapter<RvAdapterDiscount.ViewHolder>() {
    lateinit var discount_aplayed: TextView
    lateinit var ttl_amount: TextView
    lateinit var iOT: TextView
    lateinit var total_amount_formateds: TextView
    private val database: SQLiteHelper? = SQLiteHelper(context)
    var flagPosition = -1
    var selectedFlag = 0
    var tax_disp = 0.0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
//        database = SQLiteHelper(context)
        val layoutInflator = LayoutInflater.from(context)
        return ViewHolder(layoutInflator.inflate(R.layout.rv_discount, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val cu = database!!.discountTypes(position)
        cu.moveToFirst()

        holder!!.discount_name.setText(cu.getString(cu.getColumnIndex("LABEL")))

        if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1"))
            holder!!.discount_amount.setText(cu.getString(cu.getColumnIndex("DISCOUNT")) + " %")
        else
            holder!!.discount_amount.setText(cu.getString(cu.getColumnIndex("DISCOUNT")))
        cu.close()
    }

    override fun getItemCount(): Int {
        return database!!.discountSize.count
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var discount_name: TextView
        var discount_amount: TextView
        lateinit var message: TextView
        lateinit var ok: Button

        init {
            discount_name = view.findViewById(R.id.discount_name)
            discount_amount = view.findViewById(R.id.discount_amount)

            discount_name.setOnClickListener(View.OnClickListener {
                val cu = database!!.discountTypes(adapterPosition)
                cu.moveToFirst()
                if (selectedFlag == 0) {
                    selectedFlag = 1
                    flagPosition = adapterPosition
                    discount_name.setTextColor(Color.RED)
                    discount_amount.setTextColor(Color.RED)

                    /*Toast.makeText(context, cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")) + " "
                            + cu.getString(cu.getColumnIndex("DISCOUNT"))+ " "
                            + cu.getString(cu.getColumnIndex("LABEL")), Toast.LENGTH_SHORT).show()*/

                    fragmentPayment.set(cu, context, discount_aplayed, ttl_amount, 1, adapterPosition, total_amount_formateds, iOT)
                } else
                    if (flagPosition == adapterPosition) {
                        selectedFlag = 0
                        flagPosition = -1
                        discount_name.setTextColor(Color.DKGRAY)
                        discount_amount.setTextColor(Color.DKGRAY)
                        fragmentPayment.set(cu, context, discount_aplayed, ttl_amount, 0, adapterPosition, total_amount_formateds, iOT)
                    } else {
                        val popup = Dialog(context)
                        popup.setContentView(R.layout.fragement_message_dialog)
                        message = popup.findViewById(R.id.message)
                        ok = popup.findViewById(R.id.ok)
                        popup.setCancelable(false)
                        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)))
                        message.setText("Deselect Applied Discount First..!!")
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                        Toast.makeText(context, "Deselect Applied Discount First..!!", Toast.LENGTH_SHORT).show()
                    }
                cu.close()
            })
        }
    }

    fun set(discount_applayed: TextView, total_amount: TextView, total_amount_formated: TextView, i_o_t: TextView) {
        total_amount_formateds = total_amount_formated
        ttl_amount = total_amount
        discount_aplayed = discount_applayed
        iOT = i_o_t
    }
}