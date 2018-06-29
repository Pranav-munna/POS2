package com.twixt.pranav.pos.View.Fragment

import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.ErrorPage
import com.twixt.pranav.pos.View.Activity.Home
import com.twixt.pranav.pos.View.Activity.Payment
import com.twixt.pranav.pos.View.Adapter.RvAdapterCart
import java.text.DecimalFormat

/**
 * Created by Pranav on 11/27/2017.
 */

/*var cart: TextView? = null
private var database: SQLiteHelper? = null*/

class FragmentCart : Fragment(), RvAdapterCart.progressCartCallBAck {


    lateinit var cart: TextView
    private lateinit var databaseForCart: SQLiteHelper

    lateinit var recyclerview_list: RecyclerView
    lateinit var back: ImageButton
    lateinit var btn_pay: Button
    lateinit var progres: ProgressDialog

    lateinit var title: TextView
    lateinit var cr: Cursor
    var i = 0
    var c_count = 0
    var total = 0.0
    var formatter = DecimalFormat("#,###,##0.00")


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_cart, container, false)

        recyclerview_list = mView.findViewById(R.id.recyclerview)
        back = mView.findViewById(R.id.back)
        btn_pay = mView.findViewById(R.id.btn_pay)
        cart = mView.findViewById(R.id.cart)
        title = mView.findViewById(R.id.title)

        databaseForCart = SQLiteHelper(activity)
        try {
            val cu = databaseForCart.language
            cu.moveToFirst()
            title.setText(cu.getString(cu.getColumnIndex("CART")).toString())
            btn_pay.setText(cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " 0.00")


            if (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0).toInt() > 0) {

                if (databaseForCart != null) {

                    for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                        val cu = databaseForCart.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databaseForCart.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                        cu_item.moveToFirst()
                        var t_amount = 0.0
                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        var i = 0
                        while (i < (mainstring.size - 1)) {
                            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            t_amount = t_amount + sub_strig[1].toDouble()
                            i++
                        }

                        total = total + ((cu.getString(cu.getColumnIndex("PRICE")).toDouble() + t_amount) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())

                        cu.close()
                        cu_item.close()
                    }
                }


                btn_pay.setText(cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(total))
                val adapterCart_Rv = RvAdapterCart(activity, FragmentHome(), FragmentItems())
                recyclerview_list.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack(this)
                recyclerview_list.getRecycledViewPool().setMaxRecycledViews(0, 0)
                adapterCart_Rv.set(btn_pay, cart, databaseForCart)
                recyclerview_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            } else
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("NO_ITEMS")).toString(), Toast.LENGTH_SHORT).show()
            cu.close()
        } catch (e: Exception) {

            Log.e("error_is_this ->", e.toString())
        }


        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                startActivity(Intent(activity, ErrorPage::class.java))
            }
        }
        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                try {
                    val cu = databaseForCart.cartdatas(i)
                    cu.moveToFirst()
                    if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                        ++c_count
                    }
                    cu.close()
                } catch (e: Exception) {
                    Log.e("error_here->", e.toString())
                }

            }
        }
        cart!!.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())





        btn_pay.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Payment::class.java)) })
        back.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Home::class.java)) })
        return mView
    }

    override fun onResume() {
        super.onResume()
        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                val cu = databaseForCart.cartdatas(i)
                cu.moveToFirst()
                if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            }
        }
        cart!!.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())
//        cart.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0)).toString())
    }

    fun set(btnPay: Button?, ttal: String, context: Context) {
        btnPay!!.setText(ttal)

        c_count = 0
        if (!(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                val cu = databaseForCart.cartdatas(i)
                cu.moveToFirst()
                if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            }
        }
        cart!!.setText((SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString())

//        cartt.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())

    }

    override fun CartsetCallback(btnPay: Button?, ttal: String, context: Context, databaseForCarts: SQLiteHelper, cartt: TextView?) {

        btnPay!!.setText(ttal)

        c_count = 0
        if (!(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                val cu = databaseForCarts.cartdatas(i)
                cu.moveToFirst()
                if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            }
        }
        cartt!!.setText((SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString())

//        cartt.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())
    }

    override fun StartProgressCart(progress: ProgressDialog) {
        progres = progress
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            progres.dismiss()
        } catch (e: Exception) {
        }

    }

}