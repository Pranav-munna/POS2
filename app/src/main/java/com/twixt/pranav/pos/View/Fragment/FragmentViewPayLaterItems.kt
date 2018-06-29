package com.twixt.pranav.pos.View.Fragment

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestDeletePayLater
import com.twixt.pranav.pos.Controller.Request.RequestRefund
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponceRefund
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Adapter.RvAdapterViewLaterItems

/**
 * Created by Pranav on 2/5/2018.
 */
var itm = ""
/*var order_id = ""*/

class FragmentViewPayLaterItems : Fragment() {

//    var itm = ""
    var order_id = ""

    lateinit var rv_pay_later: RecyclerView
    lateinit var back: ImageButton
    lateinit var title: TextView
    lateinit var update: TextView
    private lateinit var databaseforViewPayLAterItems: SQLiteHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_view_pay_later_items, container, false)
        itm = ""
        rv_pay_later = mView.findViewById(R.id.rv_pay_later)
        back = mView.findViewById(R.id.back)
        title = mView.findViewById(R.id.title)
        update = mView.findViewById(R.id.update)

        databaseforViewPayLAterItems = SQLiteHelper(activity)
        val cu = databaseforViewPayLAterItems.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("ORDERS")).toString())

        RequestRefund(activity, ResponceProcessorItemList()).getRefundID(arguments.getString("ORDERID"),
                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))


        update.setOnClickListener {
            if (itm.length > 1) {
//                Toast.makeText(activity, itm, Toast.LENGTH_SHORT).show()
                Log.e("itemssss", itm)
                RequestDeletePayLater(activity, ResponceProcessorPayDeletePayLater()).getDeletePayLater(order_id, itm)
                update.isClickable = false
            }
        }

        back.setOnClickListener(View.OnClickListener { activity.onBackPressed() })
        return mView;
    }

    inner class ResponceProcessorPayDeletePayLater : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {

//            Toast.makeText(activity, order_id, Toast.LENGTH_SHORT).show()
//            val intent = Intent(activity, PayLaterList::class.java)
//            intent.putExtra("ID", order_id)
//            activity.startActivity(intent)

            activity.onBackPressed()
            activity.finish()
        }

    }

    inner class ResponceProcessorItemList : ProcessResponcceInterphase<ResponceRefund> {
        override fun processResponce(responce: ResponceRefund) {
            itm = ""
            order_id = responce.id.toString()
            val rvAdapterViewLaterItems = RvAdapterViewLaterItems(activity, FragmentViewPayLaterItems())
            rv_pay_later.adapter = rvAdapterViewLaterItems
            rv_pay_later.getRecycledViewPool().setMaxRecycledViews(0, 0)
            rvAdapterViewLaterItems.set(responce.orderItems!!,databaseforViewPayLAterItems)
            rv_pay_later.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

    }

    fun set(items: String, context: Context) {
        itm = items.substring(1, items.length)
        itm = "[" + itm + "]"

    }
}