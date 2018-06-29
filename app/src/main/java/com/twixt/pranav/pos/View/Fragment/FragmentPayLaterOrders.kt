package com.twixt.pranav.pos.View.Fragment

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestpayLaterRecieptDetails
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLaterRecieptDetails
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Adapter.RvAdapterPayLaterOrders
import java.util.*

/**
 * Created by Pranav on 2/2/2018.
 */
class FragmentPayLaterOrders : Fragment() {

    lateinit var pay_all: TextView
    lateinit var title: TextView
    lateinit var rv_orders: RecyclerView
    lateinit var back: ImageButton
    private lateinit var databasePayLaterOrder: SQLiteHelper
    lateinit var progress: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_paylater_orders, container, false)
        pay_all = mView.findViewById(R.id.pay_all)
        title = mView.findViewById(R.id.title)
        rv_orders = mView.findViewById(R.id.rv_orders)
        back = mView.findViewById(R.id.back)

        progress = ProgressDialog(activity)
        databasePayLaterOrder = SQLiteHelper(activity)
        val cu = databasePayLaterOrder.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("ORDERS")).toString())

        progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
        progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
        progress.show()

        RequestpayLaterRecieptDetails(activity, ResponceProcessor())
                .getPayLaterRecieptDetails(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"),
                        arguments.getString("ID").toString())


        back.setOnClickListener(View.OnClickListener { activity.onBackPressed() })
        cu.close()
        return mView
    }

    override fun onResume() {
        super.onResume()
        progress.show()
        RequestpayLaterRecieptDetails(activity, ResponceProcessor())
                .getPayLaterRecieptDetails(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"),
                        arguments.getString("ID").toString())

    }

    inner class ResponceProcessor : ProcessResponcceInterphase<Array<ResponcePayLaterRecieptDetails>> {
        override fun processResponce(responce: Array<ResponcePayLaterRecieptDetails>) {
            var list = ArrayList(Arrays.asList(*responce))

            try {
                val rvAdapterPayLaterOrders = RvAdapterPayLaterOrders(activity)
                rv_orders.adapter = rvAdapterPayLaterOrders
                rv_orders.getRecycledViewPool().setMaxRecycledViews(0, 0)
                rvAdapterPayLaterOrders.set(list,databasePayLaterOrder)
                rv_orders.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                progress.dismiss()
            } catch (e: Exception) {
                Log.e("error activity", e.toString())
            }

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        try{progress.dismiss()}catch (e:Exception){}
    }
}