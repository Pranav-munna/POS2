package com.twixt.pranav.pos.View.Fragment.Tab

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Adapter.RvAdapterfavorites
import com.twixt.pranav.pos.View.Fragment.FragmentHome
import java.text.DecimalFormat


/**
 * Created by Pranav on 11/24/2017.
 */
//private var database: SQLiteHelper? = null

class HomeTabFavorites : Fragment(), RvAdapterfavorites.getProgressCallBack {

    private lateinit var databasefav: SQLiteHelper
    //    var countx = 0
    lateinit var progres: ProgressDialog
    var formatter = DecimalFormat("#,###,##0.00")
    //    val handler = Handler()
//    val handler2 = Handler()
    lateinit var recyclerview_list: RecyclerView
    var count = 1
    lateinit var message: TextView


    //itemadd
    lateinit var one: Button
    lateinit var two: Button
    lateinit var three: Button
    lateinit var four: Button
    lateinit var five: Button
    lateinit var six: Button
    lateinit var seven: Button
    lateinit var eight: Button
    lateinit var nine: Button
    lateinit var zero: Button
    lateinit var dot: Button
    lateinit var ok: Button
    lateinit var popup: Dialog
    var dot_comma_int = 1.1
    var dot_comma = ""
    lateinit var total: TextView
    lateinit var quen: TextView
    lateinit var delete: ImageButton
    var qty = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.pv_tab_home, container, false)
        recyclerview_list = mView.findViewById(R.id.recyclerview)
        popup = Dialog(context)
        popup.setContentView(R.layout.popup_kg)
        one = popup.findViewById(R.id.one)
        two = popup.findViewById(R.id.two)
        three = popup.findViewById(R.id.three)
        four = popup.findViewById(R.id.four)
        five = popup.findViewById(R.id.five)
        six = popup.findViewById(R.id.six)
        seven = popup.findViewById(R.id.seven)
        eight = popup.findViewById(R.id.eight)
        nine = popup.findViewById(R.id.nine)
        zero = popup.findViewById(R.id.zero)
        dot = popup.findViewById(R.id.dot)
        ok = popup.findViewById(R.id.ok)
        total = popup.findViewById(R.id.total)
        delete = popup.findViewById(R.id.delete)
        quen = popup.findViewById(R.id.quen)
        popup.setCancelable(true)



        if (activity != null)
            databasefav = SQLiteHelper(activity)

        val cu = databasefav.language
        cu.moveToFirst()

        val adapterItemList_Rv = RvAdapterfavorites(activity, FragmentHome())

        recyclerview_list.adapter = adapterItemList_Rv
        adapterItemList_Rv.set1()
        adapterItemList_Rv.setCallback(this)

        val width_px = Resources.getSystem().displayMetrics.widthPixels
        val height_px = Resources.getSystem().displayMetrics.heightPixels
        val pixeldpi = Resources.getSystem().displayMetrics.densityDpi

        val width_dp = width_px / pixeldpi * 220
        val height_dp = height_px / pixeldpi * 220


        if (width_dp > 600 && height_dp > 600)
            recyclerview_list.layoutManager = GridLayoutManager(activity, 2)
        else
            recyclerview_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        cu.close()
        return mView
    }

    override fun StartProgress(progress: ProgressDialog) {
        progres = progress
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            progres.dismiss()
        } catch (e: Exception) {
        }

    }

    override fun AddItemToCart(adapterPosition: Int, fragments: FragmentHome) {

        val cu1: Cursor?
//                if (flag.equals("2"))
        try {
            cu1 = databasefav.getAllItems(adapterPosition)
            cu1.moveToFirst()


            if (cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString().equals("1")) {

                try {
                    if (formatter.format(dot_comma_int).toDouble() == 1.1) {
                        dot_comma = "."
                    }
                } catch (e: Exception) {
                    dot_comma = ","
                }
                dot.setText(dot_comma)

                val cu = databasefav.getLanguage()
                cu.moveToFirst()
                quen.setText(cu.getString(cu.getColumnIndex("QUANTITY")).toString())
                ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
                popup.show()
                one.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "1")
                })
                two.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "2")
                })
                three.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "3")
                })
                four.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "4")
                })
                five.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "5")
                })
                six.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "6")
                })
                seven.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "7")
                })
                eight.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "8")
                })
                nine.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "9")
                })
                zero.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + "0")
                })
                dot.setOnClickListener(View.OnClickListener {
                    total.setText(total.text.toString() + dot_comma)
                })
                ok.setOnClickListener(View.OnClickListener {
                    try {
                        if (total.text.equals("") || total.text.toString().replace(",", ".").toDouble() <= 0.001) {
                            popup.dismiss()
                            total.setText("")
                        } else if (databasefav.insetCartData(cu1.getString(cu1.getColumnIndex("ID")).toString().toInt(),
                                        cu1.getString(cu1.getColumnIndex("NAME")).toString(),
                                        total.text.toString().replace(",", ".").toDouble(),
                                        cu1.getString(cu1.getColumnIndex("PRICE")).toString(),
                                        cu1.getString(cu1.getColumnIndex("SHAPE")).toString(),
                                        cu1.getString(cu1.getColumnIndex("COLOR")).toString(),
                                        cu1.getString(cu1.getColumnIndex("IMAGE")).toString(),
                                        cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString(), "",
                                        cu1.getString(cu1.getColumnIndex("COUNT")).toString(),
                                        cu1.getString(cu1.getColumnIndex("TAX")).toString())) {

                            fragments.incrementcart2(context, databasefav)
                            popup.dismiss()

                            val cu_modifier = databasefav.getModifier(0, cu1.getString(cu1.getColumnIndex("ID")).toString())
                            cu_modifier.moveToFirst()



                            if (cu_modifier.count != 0) {
                                val next = Intent(context, com.twixt.pranav.pos.View.Activity.Modifier::class.java)
                                next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ID")).toString())
                                next.putExtra("COUNT", databasefav.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString())
                                context.startActivity(next)
                            }

                            cu_modifier.close()
                            cu1.close()
                            total.setText("")
                        }
                    } catch (e: Exception) {
                        total.setText("")

                    }


                })
                delete.setOnClickListener(View.OnClickListener {
                    if (total.text.toString().length != 0)
                        total.setText(total.text.toString().substring(0, total.text.toString().length - 1))
                })
                cu.close()
            } else {
                if (databasefav != null) {
                    val cu = databasefav.getLanguage()
                    cu.moveToFirst()
                    if (databasefav.insetCartData(cu1.getString(cu1.getColumnIndex("ID")).toInt(),
                                    cu1.getString(cu1.getColumnIndex("NAME")).toString(),
                                    qty.toDouble(),
                                    cu1.getString(cu1.getColumnIndex("PRICE")).toString(),
                                    cu1.getString(cu1.getColumnIndex("SHAPE")).toString(),
                                    cu1.getString(cu1.getColumnIndex("COLOR")).toString(),
                                    cu1.getString(cu1.getColumnIndex("IMAGE")).toString(),
                                    cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString(), "",
                                    cu1.getString(cu1.getColumnIndex("COUNT")).toString(),
                                    cu1.getString(cu1.getColumnIndex("TAX")).toString())) {
                        fragments.incrementcart2(context, databasefav)
//                                fragments.itemdeleted(context)

                        try {

                            val cu_modifier = databasefav.getModifier(0, cu1.getString(cu1.getColumnIndex("ID")).toString())
                            cu_modifier.moveToFirst()



                            if (cu_modifier.count != 0) {
                                val next = Intent(context, com.twixt.pranav.pos.View.Activity.Modifier::class.java)
                                next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ID")).toString())
                                next.putExtra("COUNT", databasefav.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString())
                                context.startActivity(next)
                            }

                            cu_modifier.close()
                        } catch (e: Exception) {
                            Log.e("error_is_this ->", e.toString())
                        }
                    }
                    cu.close()
                }
            }
        } catch (e: Exception) {
            Log.e("error_is_this ->", e.toString())
        }
    }


}