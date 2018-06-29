package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestRefund
import com.twixt.pranav.pos.Controller.Request.RequestRefundItems
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponceRefund
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Receipts
import com.twixt.pranav.pos.View.Adapter.RvAdapterRefundItems
import java.text.DecimalFormat

/**
 * Created by Pranav on 1/1/2018.
 */


//var split: TextView? = null

class FragmentRefundItems : Fragment() {
    var ttl_amt = 0.0
    lateinit var rv_refund_items: RecyclerView
    lateinit var back: ImageButton
    lateinit var title: TextView

    lateinit var split: TextView

    internal lateinit var rvAdapterRefundItems: RvAdapterRefundItems
    var orderid = ""
    var paymentType = "1"
    var percentage = 100.0
    lateinit var progress: ProgressDialog

    lateinit var message: TextView
    lateinit var ok: Button
    var formatter = DecimalFormat("#,###,##0.00")

    private lateinit var databaseforRefundItems: SQLiteHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_refund_items, container, false)

        rv_refund_items = mView.findViewById(R.id.rv_refund_items)
        back = mView.findViewById(R.id.back)
        split = mView.findViewById(R.id.split)
        title = mView.findViewById(R.id.title)
        databaseforRefundItems = SQLiteHelper(activity)
        progress = ProgressDialog(activity)

        val cu = databaseforRefundItems.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("RECEIPTS")).toString())
        cu.getString(cu.getColumnIndex("REFUND")).toString()

        progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
        progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
        progress.setCancelable(true)

        split!!.setText(cu.getString(cu.getColumnIndex("REFUND")).toString() +formatter.format( 0.00))

        rvAdapterRefundItems = RvAdapterRefundItems(activity, FragmentRefundItems(), cu)
        orderid = arguments.getString("orderId")

        RequestRefund(activity, ResponceProcessorRefunds()).getRefundID(orderid.toString(), SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))

        back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, Receipts::class.java))
        })

        split!!.setOnClickListener(View.OnClickListener {
            //            Toast.makeText(activity, rvAdapterRefundItems.gettotal() + "   " + paymentType +  orderid, Toast.LENGTH_SHORT).show()
            if (rvAdapterRefundItems.get().replace("#,", "").length > 1) {
                RequestRefundItems(activity, ResponceProcessorRefundItems()).refunditems(orderid,
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, ""),
                        "[" + rvAdapterRefundItems.get().replace("#,", "") + "]",
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, ""),
                        rvAdapterRefundItems.gettotal(), paymentType)
                progress.show()
            } else {
                activity.finish()
                startActivity(Intent(activity, Receipts::class.java))
            }
        })
        cu.close()
        return mView
    }

    inner class ResponceProcessorRefundItems : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
                progress.dismiss()
                Toast.makeText(activity, responce.status, Toast.LENGTH_SHORT).show()
                activity.finish()
                startActivity(Intent(activity, Receipts::class.java))
            }
        }

    }

    inner class ResponceProcessorRefunds : ProcessResponcceInterphase<ResponceRefund> {
        override fun processResponce(responce: ResponceRefund) {
            val cu = databaseforRefundItems.language
            cu.moveToFirst()
            if (responce == null) {
                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                ok = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                message.setText(cu.getString(cu.getColumnIndex("REFUND_UNAVAILABLE")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu.getString(cu.getColumnIndex("REFUND_UNAVAILABLE")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                try {
                    orderid = responce.orderItems!![0].orderId.toString()
                    if (responce.paymentType == null)
                        paymentType = responce.payments!![0].paymentTypeId.toString()
                    else
                        paymentType = responce.paymentType.toString()

                    percentage = (((responce.discountAmount!! + responce.refunded_discount.toString().replace(",", ".").toDouble()) * 100) / responce.total!!).toDouble()

//                    Toast.makeText(activity, responce.orderItems!![0].price.toString(), Toast.LENGTH_SHORT).show()
                    rv_refund_items.adapter = rvAdapterRefundItems
                    rv_refund_items.getRecycledViewPool().setMaxRecycledViews(0, 0)
                    rvAdapterRefundItems.set(responce.orderItems!!, percentage,split)
                    rv_refund_items.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                } catch (e: Exception) {
                }


            }
            cu.close()
        }

    }

    fun set(total_amt: Double, cu: Cursor, splits: TextView) {
        /*databaseforRefundItems = SQLiteHelper(activity)
        val cu = databaseforRefundItems.getLanguage()
        cu.moveToFirst()*/
        ttl_amt = total_amt
        splits.setText(cu.getString(cu.getColumnIndex("REFUND")).toString() + " " + formatter.format( ttl_amt))
//        cu.close()
    }
    override fun onDestroy() {
        super.onDestroy()
        try{progress.dismiss()}catch (e:Exception){}
    }
}