package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestReceipts
import com.twixt.pranav.pos.Controller.Responce.ResponceReceipts
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Home
import com.twixt.pranav.pos.View.Activity.Settings
import com.twixt.pranav.pos.View.Adapter.RvAdapterReceipts
import java.util.*


/**
 * Created by Pranav on 11/27/2017.
 */
class FragmentReceipts : Fragment() {

    lateinit var rv_receipts: RecyclerView
    lateinit var back: ImageButton
    lateinit var sales_btn: ImageButton
    lateinit var settings_btn: ImageButton
    lateinit var searches: ImageButton
    lateinit var searches_date: ImageButton
    lateinit var search_et: EditText
    lateinit var check: TextView
    lateinit var title: TextView
    lateinit var message: TextView
    lateinit var tit_res: TextView
    lateinit var ok: Button
    lateinit var datepicker: DatePicker

    var count = 0
    var key = ""
    private lateinit var databaseforreceipts: SQLiteHelper

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_receipts, container, false)

        rv_receipts = mView.findViewById(R.id.rv_receipts)
        back = mView.findViewById(R.id.back)
        check = mView.findViewById(R.id.check)
        title = mView.findViewById(R.id.title)
        searches = mView.findViewById(R.id.searches)
        searches_date = mView.findViewById(R.id.searches_date)
        search_et = mView.findViewById(R.id.search_et)
        datepicker = mView.findViewById(R.id.datepicker)
        databaseforreceipts = SQLiteHelper(activity)
        val cu = databaseforreceipts.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("RECEIPTS")).toString())

        if (check.text.toString().equals("tab")) {
            sales_btn = mView.findViewById(R.id.sales_btn)
            settings_btn = mView.findViewById(R.id.settings_btn)
            tit_res = mView.findViewById(R.id.tit_res)
            tit_res.setText(cu.getString(cu.getColumnIndex("RECEIPTS")).toString())
            settings_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Settings::class.java)) })
            sales_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Home::class.java)) })
        }
        key = "R"
        RequestReceipts(activity, ResponceProcessorReceipts()).getReceipts(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"), count)

        /*val rvAdapterReceipts = RvAdapterReceipts(activity)
        rv_receipts.adapter = rvAdapterReceipts
        rv_receipts.getRecycledViewPool().setMaxRecycledViews(0, 0)
        rv_receipts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/

        searches.setOnClickListener(View.OnClickListener {
            datepicker.visibility = View.GONE
            if (search_et.visibility != View.VISIBLE)
                search_et.visibility = View.VISIBLE
            else {
                if (!search_et.text.toString().trim().equals("")) {
                    key = "S"
                    RequestReceipts(activity, ResponceprocessorSearch()).getReceiptSearch(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").toString(),
                            search_et.text.toString())
                    search_et.setText("")
                }
                search_et.visibility = View.GONE
            }
//            search.isClickable = false
        })

        searches_date.setOnClickListener(View.OnClickListener {
            search_et.visibility = View.GONE
            if (datepicker.visibility != View.VISIBLE)
                datepicker.visibility = View.VISIBLE
            else {
                datepicker.visibility = View.GONE
            }
        })



        search_et.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!search_et.text.toString().trim().equals("")) {
                        key = "Search"
                        RequestReceipts(activity, ResponceprocessorSearch()).getReceiptSearch(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").toString(),
                                search_et.text.toString())
                        search_et.visibility = View.GONE
                        search_et.setText("")
                        searches.isClickable = true
                    }
                    return true
                }
                return false
            }

        })


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        datepicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), DatePicker.OnDateChangedListener { datePicker, year, month, dayOfMonth ->
            Log.d("Datee", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth)
            key = year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()
            RequestReceipts(activity, ResponceprocessorSearch()).getReceiptSearchDate(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").toString(),
                    year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString(), 1)


            datePicker.visibility = View.GONE
        })

        back.setOnClickListener(View.OnClickListener { activity.onBackPressed() })
        cu.close()
        return mView
    }


    inner class ResponceprocessorSearch : ProcessResponcceInterphase<Array<ResponceReceipts>> {
        override fun processResponce(responce: Array<ResponceReceipts>) {
            if (responce != null && responce.size > 0) {
//                Toast.makeText(activity, responce[0].orderId.toString(), Toast.LENGTH_SHORT).show()

                val list = ArrayList(Arrays.asList(*responce))
                val rvAdapterReceipts = RvAdapterReceipts(activity)
                rv_receipts.adapter = rvAdapterReceipts
                rv_receipts.getRecycledViewPool().setMaxRecycledViews(0, 0)
                rvAdapterReceipts.set(list, key)
                rv_receipts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            } else if (responce.size == 0) {
                val cu1 = databaseforreceipts.language
                cu1.moveToFirst()
                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("NO_RECIEPTS")).toString(), Toast.LENGTH_SHORT).show()
                cu1.close()
            }
            key = ""
        }

    }

    inner class ResponceProcessorReceipts : ProcessResponcceInterphase<Array<ResponceReceipts>> {
        override fun processResponce(responce: Array<ResponceReceipts>) {
            val cu1 = databaseforreceipts.language
            cu1.moveToFirst()
            if (responce == null) {
                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                ok = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

//                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val list = ArrayList(Arrays.asList(*responce))
                    val rvAdapterReceipts = RvAdapterReceipts(activity)
                    rv_receipts.adapter = rvAdapterReceipts
                    rv_receipts.getRecycledViewPool().setMaxRecycledViews(0, 0)
                    rvAdapterReceipts.set(list, key)
                    rv_receipts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                } catch (e: Exception) {
                }

//                Toast.makeText(activity, responce[0].date+" 0k..!!", Toast.LENGTH_SHORT).show()
            }
            cu1.close()
        }

    }

}