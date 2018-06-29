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
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R


/**
 * Created by Pranav on 11/27/2017.
 */
class RvAdapterPayment(var context: Context) : RecyclerView.Adapter<RvAdapterPayment.ViewHolder>() {

    var size = 0
    private lateinit var databaseForpayment: SQLiteHelper
    var flagPosition = -1
    var selectedFlag = 0
    lateinit var total_amount_formated: TextView


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.rv_payment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cu = databaseForpayment.paymentTypedatas(position)
        cu.moveToFirst()

        holder.payment_m.setText(cu.getString(cu.getColumnIndex("PAYMENT_NAME")).toString())

        if (cu.getString(cu.getColumnIndex("PAYMENTTYPE")).toString().equals("Card"))
            holder.payment_m.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_drawable_card, 0, 0, 0)
        if (cu.getString(cu.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash"))
            holder.payment_m.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_drawable_cash, 0, 0, 0)
        if (cu.getString(cu.getColumnIndex("PAYMENTTYPE")).toString().equals("Check"))
            holder.payment_m.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_drawable_check, 0, 0, 0)
        if (cu.getString(cu.getColumnIndex("PAYMENTTYPE")).toString().equals("Other"))
            holder.payment_m.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_drawable_others, 0, 0, 0)

        cu.close()
    }


    override fun getItemCount(): Int {
        return databaseForpayment.paymentTypes.count
    }

    fun set(total_amount_formatedd: TextView, databasePaymentLater: SQLiteHelper) {
        databaseForpayment=databasePaymentLater
        total_amount_formated = total_amount_formatedd

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var payment_m: TextView
        lateinit var message: TextView
        lateinit var ok: Button

        init {
            payment_m = view.findViewById(R.id.payment_m)
            val cu1 = databaseForpayment.language
            cu1.moveToFirst()
            payment_m.setOnClickListener(View.OnClickListener {
                if (selectedFlag == 0) {
                    selectedFlag = 1
                    flagPosition = adapterPosition
                    val cu = databaseForpayment.paymentTypedatas(adapterPosition)
                    var i = 0

                    while (i < databaseForpayment.paymentTypes.count) {
                        if (i != adapterPosition) {
                            notifyItemChanged(i)

                        }
                        i++
                    }

                    cu.moveToFirst()


                    payment_m.setTextColor(Color.RED)
                    /* var i = 0
                 while (i < databaseForpayment.paymentTypes.count) {
                     if (i!=adapterPosition)
                     {
                         payment_m
                     }
                     i++
                 }*/
//                    Toast.makeText(context, cu.getString(cu.getColumnIndex("PAYMENT_NAME")).toString() + " " + cu1.getString(cu1.getColumnIndex("SELECTED")).toString(), Toast.LENGTH_SHORT).show()
                    SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).PAYMENT_TYPE, cu.getString(cu.getColumnIndex("PAYMENT_ID")).toString())
                    SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).CASH, cu.getString(cu.getColumnIndex("PAYMENTTYPE")).toString())
                    cu.close()
                } else
                    if (flagPosition == adapterPosition) {
                        selectedFlag = 0
                        flagPosition = -1
                        payment_m.setTextColor(Color.DKGRAY)
                        SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).PAYMENT_TYPE, "0")
                        SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).CASH, "0")
                    } else {
                        val popup = Dialog(context)
                        popup.setContentView(R.layout.fragement_message_dialog)
                        message = popup.findViewById(R.id.message)
                        ok = popup.findViewById(R.id.ok)
                        popup.setCancelable(false)
                        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)))
                        message.setText(cu1.getString(cu1.getColumnIndex("DESELECTPAYMENT")).toString() + "..!!")
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                        Toast.makeText(context, "Deselect payment method First..!!", Toast.LENGTH_SHORT).show()
                    }
            })
//            cu1.close()
        }

    }
}


/*  listItems = new ArrayList<String>();
    for (int i = 0; i < values.length; ++i) {
        listItems.add(values[i]);
    }*/