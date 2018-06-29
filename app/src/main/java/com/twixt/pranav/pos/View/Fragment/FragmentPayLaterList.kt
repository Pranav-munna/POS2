package com.twixt.pranav.pos.View.Fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestPayLaterMails
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLaterMails
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Home
import com.twixt.pranav.pos.View.Activity.Settings
import com.twixt.pranav.pos.View.Adapter.RvAdapterPayLaterList
import java.util.*

/**
 * Created by Pranav on 2/2/2018.
 */
class FragmentPayLaterList : Fragment() {

    lateinit var rv_receipts: RecyclerView
    lateinit var back: ImageButton
    lateinit var sales_btn: ImageButton
    lateinit var settings_btn: ImageButton
    lateinit var check: TextView
    lateinit var title: TextView
    lateinit var message: TextView
    lateinit var ok: Button

    var count = 1
    private var database: SQLiteHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_pay_later_list, container, false)

        rv_receipts = mView.findViewById(R.id.rv_receipts)
        back = mView.findViewById(R.id.back)
        check = mView.findViewById(R.id.check)
        title = mView.findViewById(R.id.title)
        database = SQLiteHelper(activity)
        val cu = database!!.getLanguage()
        cu.moveToFirst()

        title.setText(cu.getString(cu.getColumnIndex("PAYLATER")).toString())

        if (check.text.toString().equals("tab")) {
            sales_btn = mView.findViewById(R.id.sales_btn)
            settings_btn = mView.findViewById(R.id.settings_btn)
            settings_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Settings::class.java)) })
            sales_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Home::class.java)) })
        }

        RequestPayLaterMails(activity, ResponceProcessorPayLaterList()).getPayLaterMails(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(), count++)



        back.setOnClickListener(View.OnClickListener { activity.finish() })
        cu.close()
        return mView
    }

    inner class ResponceProcessorPayLaterList : ProcessResponcceInterphase<Array<ResponcePayLaterMails>> {
        override fun processResponce(responce: Array<ResponcePayLaterMails>) {
            if (responce != null) {
                val list = ArrayList(Arrays.asList(*responce))
                val rvAdapterReceipts = RvAdapterPayLaterList(activity)
                rv_receipts.adapter = rvAdapterReceipts
                rv_receipts.getRecycledViewPool().setMaxRecycledViews(0, 0)
                rvAdapterReceipts.set(list)
                rv_receipts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        }

    }


}