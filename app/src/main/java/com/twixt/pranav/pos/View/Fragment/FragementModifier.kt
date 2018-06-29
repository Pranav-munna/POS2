package com.twixt.pranav.pos.View.Fragment

import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Adapter.RvAdapterModifier

/**
 * Created by Pranav on 2/21/2018.
 */

/*var tv_total: TextView? = null
private var database: SQLiteHelper? = null*/

class FragementModifier : Fragment(), RvAdapterModifier.ModifierCallBack {

    lateinit var tv_total: TextView
    private lateinit var databaseforModifiers: SQLiteHelper

    lateinit var rv_modifier_list: RecyclerView
    lateinit var back: ImageButton
    lateinit var btn_ok: Button
    lateinit var tv_cancel: TextView
    lateinit var title: TextView
    var total_modifier_amount = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_modifier, container, false)
        val width_px = Resources.getSystem().displayMetrics.widthPixels
        val height_px = Resources.getSystem().displayMetrics.heightPixels
        val pixeldpi = Resources.getSystem().displayMetrics.densityDpi
        val width_dp = width_px / pixeldpi * 220
        val height_dp = height_px / pixeldpi * 220
        if (width_dp > 700 && height_dp > 700)
            activity.window.setLayout(width_dp.toInt(), height_dp.toInt())

        databaseforModifiers = SQLiteHelper(activity)
        rv_modifier_list = mView.findViewById(R.id.rv_modifier_list)
        back = mView.findViewById(R.id.back)
        btn_ok = mView.findViewById(R.id.btn_ok)
        tv_cancel = mView.findViewById(R.id.tv_cancel)
        tv_total = mView.findViewById(R.id.tv_total)
        title = mView.findViewById(R.id.title)


        val cu_language = databaseforModifiers.language
        cu_language.moveToFirst()

        title.setText(cu_language.getString(cu_language.getColumnIndex("MODIFIERS")))
        tv_cancel.setText(cu_language.getString(cu_language.getColumnIndex("DELETE_MODIFIERS")))
        btn_ok.setText(cu_language.getString(cu_language.getColumnIndex("NEXT")))

        val cu_item = databaseforModifiers.getitem(arguments.getString("ITEMID").toInt())
        cu_item.moveToFirst()

//        Toast.makeText(activity, cu_item.getString(cu_item.getColumnIndex("ITEM_NAME")), Toast.LENGTH_SHORT).show()

        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        while (i < (mainstring.size - 1)) {
            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            total_modifier_amount = total_modifier_amount + sub_strig[1].toDouble()
            i++
        }


        tv_total!!.setText("Total : " + String.format("%.2f", (cu_item.getString(cu_item.getColumnIndex("QUANTITY")).toDouble() *
                (cu_item.getString(cu_item.getColumnIndex("PRICE")).toDouble() + total_modifier_amount))))

        val rvAdapterModifier = RvAdapterModifier(activity, FragementModifier())
        rv_modifier_list.adapter = rvAdapterModifier
        rvAdapterModifier.setCallbackModifiers(this)
        rvAdapterModifier.set(arguments.getString("ITEMID"), arguments.getString("COUNT"), databaseforModifiers, tv_total)
        rv_modifier_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        back.setOnClickListener {

            //            database!!.updateModifiers("", arguments.getString("ITEMID"))
            activity.onBackPressed()
        }
        btn_ok.setOnClickListener {
            //            Toast.makeText(context, cu_item.getString(cu_item.getColumnIndex("MODIFIER")), Toast.LENGTH_SHORT).show()
            activity.onBackPressed()
        }
        tv_cancel.setOnClickListener {
            databaseforModifiers.updateModifiers("", arguments.getString("ITEMID"))
            activity.onBackPressed()
        }

        cu_language.close()
        cu_item.close()
        return mView
    }

    /*fun set(modifier_amount: String?, item_id: String) {
        val cu_item = database!!.getitem(item_id.toInt())
        cu_item.moveToFirst()
        var t_amount = 0.0
        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        while (i < (mainstring.size - 1)) {
            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            t_amount = t_amount + sub_strig[1].toDouble()
            i++
        }

        tv_total!!.setText("Total : " + String.format("%.2f", ((cu_item.getString(cu_item.getColumnIndex("PRICE")).toDouble() + t_amount)
                * cu_item.getString(cu_item.getColumnIndex("QUANTITY")).toDouble())))
    }*/

    override fun addModifiers(modifier_amount: String, item_id: String, databaseforModifier: SQLiteHelper, tv_ttl: TextView) {
        val cu_item = databaseforModifier.getitem(item_id.toInt())
        cu_item.moveToFirst()
        var t_amount = 0.0
        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        while (i < (mainstring.size - 1)) {
            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            t_amount = t_amount + sub_strig[1].toDouble()
            i++
        }

        tv_total!!.setText("Total : " + String.format("%.2f", ((cu_item.getString(cu_item.getColumnIndex("PRICE")).toDouble() + t_amount)
                * cu_item.getString(cu_item.getColumnIndex("QUANTITY")).toDouble())))
        cu_item.close()
    }

}