package com.twixt.pranav.pos.View.Fragment

import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import java.text.DecimalFormat

class FragmentCalculationSplit : Fragment() {

    lateinit var back: ImageButton
    lateinit var lbl_total: TextView
    lateinit var tv_total: TextView
    lateinit var tv_cash: TextView
    lateinit var et_cash: EditText
    lateinit var tv_change: TextView
    lateinit var et_change_amount: TextView
    //    lateinit var btn_twenty: Button
    lateinit var btn_fifty: Button
    lateinit var btn_hundred: Button
    lateinit var btn_twohundrad: Button
    lateinit var btn_fivehundard: Button
    lateinit var btn_thousand: Button
    lateinit var btn_checkout: Button

    lateinit var database: SQLiteHelper
    var formatter = DecimalFormat("#,###,##0.00")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_calculation, container, false)
        database = SQLiteHelper(activity)
        val cu_language = database!!.language
        cu_language.moveToFirst()
//        Toast.makeText(activity, arguments.getString("TOTAL"), Toast.LENGTH_SHORT).show()


        back = mView.findViewById(R.id.back)
        lbl_total = mView.findViewById(R.id.lbl_total)
        tv_total = mView.findViewById(R.id.tv_total)
        tv_cash = mView.findViewById(R.id.tv_cash)
        et_cash = mView.findViewById(R.id.et_cash)
        tv_change = mView.findViewById(R.id.tv_change)
        et_change_amount = mView.findViewById(R.id.et_change_amount)
//        btn_twenty = mView.findViewById(R.id.btn_twenty)
        btn_fifty = mView.findViewById(R.id.btn_fifty)
        btn_hundred = mView.findViewById(R.id.btn_hundred)
        btn_twohundrad = mView.findViewById(R.id.btn_twohundrad)
        btn_fivehundard = mView.findViewById(R.id.btn_fivehundard)
        btn_thousand = mView.findViewById(R.id.btn_thousand)
        btn_checkout = mView.findViewById(R.id.btn_checkout)

        val width_px = Resources.getSystem().displayMetrics.widthPixels
        val height_px = Resources.getSystem().displayMetrics.heightPixels
        val pixeldpi = Resources.getSystem().displayMetrics.densityDpi

        val width_dp = width_px / pixeldpi * 220
        val height_dp = height_px / pixeldpi * 220


        if (width_dp > 700 && height_dp > 700)
            activity.window.setLayout(width_dp.toInt(), height_dp.toInt())

        lbl_total.setText(cu_language.getString(cu_language.getColumnIndex("TOTAL")) + " :")
        tv_cash.setText(cu_language.getString(cu_language.getColumnIndex("CASH")))
        tv_change.setText(cu_language.getString(cu_language.getColumnIndex("CHANGE")))
        btn_checkout.setText(cu_language.getString(cu_language.getColumnIndex("CHARGE")))
        tv_total.setText(formatter.format(arguments.getString("TOTAL").replace(",", ".").toDouble()))

        et_cash.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (et_cash.text.equals("")) {
                    et_change_amount.setText("0")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (et_cash.text.equals("")) {
                    et_change_amount.setText("0")
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_cash.text.equals("")) {
                    et_change_amount.setText("0")
                } else
                    try {
                        et_change_amount.setText(formatter.format(et_cash.text.toString().replace(",", ".").toDouble() - arguments.getString("TOTAL").toDouble()))
//                        if (et_change_amount.text.toString().replace(",", ".").toDouble() >= 0) {
                        if (et_change_amount.text.substring(0, 1) != "-") {
                            btn_checkout.visibility = View.VISIBLE
                        } else
                            btn_checkout.visibility = View.INVISIBLE
                    } catch (e: Exception) {
                        et_change_amount.setText("0")
                    }
            }
        })
        btn_fifty.setOnClickListener {
            if (et_cash.text.toString().equals("")) {
                et_cash.setText("50")
            } else et_cash.setText((et_cash.text.toString().replace(",", ".").toDouble() + 50).toString())
        }
        btn_hundred.setOnClickListener {
            if (et_cash.text.toString().equals("")) {
                et_cash.setText("100")
            } else et_cash.setText((et_cash.text.toString().replace(",", ".").toDouble() + 100).toString())
        }
        btn_twohundrad.setOnClickListener {
            if (et_cash.text.toString().equals("")) {
                et_cash.setText("200")
            } else et_cash.setText((et_cash.text.toString().replace(",", ".").toDouble() + 200).toString())
        }
        btn_fivehundard.setOnClickListener {
            if (et_cash.text.toString().equals("")) {
                et_cash.setText("500")
            } else et_cash.setText((et_cash.text.toString().replace(",", ".").toDouble() + 500).toString())
        }
        btn_thousand.setOnClickListener {
            if (et_cash.text.toString().equals("")) {
                et_cash.setText("1000")
            } else et_cash.setText((et_cash.text.toString().replace(",", ".").toDouble() + 1000).toString())
        }

        back.setOnClickListener { activity.onBackPressed() }
        btn_checkout.setOnClickListener { activity.onBackPressed() }
        cu_language.close()
        return mView
    }
}
