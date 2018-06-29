package com.twixt.pranav.pos.View.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Modifier
import com.twixt.pranav.pos.View.Fragment.FragmentCart
import com.twixt.pranav.pos.View.Fragment.FragmentHome
import com.twixt.pranav.pos.View.Fragment.FragmentItems
import java.text.DecimalFormat
import java.util.*


/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterCart(var context: Context, val fragmentHome: FragmentHome?, val fragmentItems: FragmentItems?) : RecyclerView.Adapter<RvAdapterCart.ViewHolder>() {
    private lateinit var databaseForCarts: SQLiteHelper
    var btnPay: Button? = null
    var btnPay_hm: Button? = null
    var btnPay_itm: Button? = null
    var cartt: TextView? = null
    var ttal = 0.0
    var flag = -1
    var dot_comma_int = 1.1
    var dot_comma = ""
    var formatter = DecimalFormat("#,###,##0.00")
    var formatter_ = DecimalFormat("###.0")
    val handler = Handler()
    lateinit var a_carts: TextView
    var progress = ProgressDialog(context)
    private lateinit var mprogressCartCallBAck: progressCartCallBAck

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_cart, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rnd = Random()
        val clr = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        holder.color.setImageDrawable(ColorDrawable(clr))


        val cucheck = databaseForCarts.cartdatas(position)
        cucheck.moveToFirst()
        if (!(cucheck.getString(cucheck.getColumnIndex("SELLINGTYPE")).equals("1"))){


        }


        try {
            if (databaseForCarts != null) {
                val cu = databaseForCarts!!.cartdatas(position)
                cu.moveToFirst()

                if (cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    holder.rv_layout.visibility = View.GONE

                    var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    params.height = 0
                    holder.rv_layout.layoutParams.height = 0
                } else {

                    if (cucheck.getString(cucheck.getColumnIndex("SELLINGTYPE")).equals("1")){
                        holder.title.setText(
                                substring(cu.getString(cu.getColumnIndex("ITEM_NAME")), 0, if (cu.getString(cu.getColumnIndex("ITEM_NAME")).length > 12)
                                    12
                                else
                                    cu.getString(cu.getColumnIndex("ITEM_NAME")).length) + " x " + "(" +/* formatter_.format(*/cu.getString(cu.getColumnIndex("QUANTITY")).toDouble()/*)*/ + ")")

                    }else{
                        holder.title.setText(
                                substring(cu.getString(cu.getColumnIndex("ITEM_NAME")), 0, if (cu.getString(cu.getColumnIndex("ITEM_NAME")).length > 12)
                                    12
                                else
                                    cu.getString(cu.getColumnIndex("ITEM_NAME")).length) + " x " + "(" +/* formatter_.format(*/cu.getString(cu.getColumnIndex("QUANTITY")).toInt()/*)*/ + ")")
                    }



                    val cu_item = databaseForCarts.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                    cu_item.moveToFirst()
                    var t_amount = 0.0
                    var modifier_name = ""
                    var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    var i = 0
                    while (i < (mainstring.size - 1)) {
                        var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        t_amount = t_amount + sub_strig[1].toDouble()
                        modifier_name = modifier_name + "," + sub_strig[0]
                        i++
                    }


                    holder.amount.setText(formatter.format((String.format("%.2f", ((cu.getString(cu.getColumnIndex("PRICE")).toDouble() + t_amount) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble()))).replace(",", ".").toDouble()))

                    holder.base_amount.setText(formatter.format((cu.getString(cu.getColumnIndex("PRICE"))).toDouble()))


                    holder.tv_modifier_names.setText(modifier_name)



                    if (cu.getString(cu.getColumnIndex("SHAPE")).equals("")) {
                        Glide.with(context)
                                .asBitmap()
                                .load((cu.getString(7)).toString())
                                .into(holder.color)

                    } else {
                        if (cu.getString(cu.getColumnIndex("SHAPE")).equals("shape-1"))
                            holder.color.setImageResource(R.drawable.shape1)
                        if (cu.getString(cu.getColumnIndex("SHAPE")).equals("shape-2"))
                            holder.color.setImageResource(R.drawable.shape2)
                        if (cu.getString(cu.getColumnIndex("SHAPE")).equals("shape-3"))
                            holder.color.setImageResource(R.drawable.shape3)
                        if (cu.getString(cu.getColumnIndex("SHAPE")).equals("shape-4"))
                            holder.color.setImageResource(R.drawable.shape4)
                        holder.color.setColorFilter(Color.parseColor("#" + cu.getString(cu.getColumnIndex("COLOR"))))
                    }
                    cu_item.close()
                }
                cu.close()
            }
        } catch (e: Exception) {
            Log.e("xxx", e.toString())
        }


    }


    override fun getItemCount(): Int {
        return SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var color: ImageView
        lateinit var title: TextView
        lateinit var amount: TextView
        var base_amount: TextView

        lateinit var rv_layout: RelativeLayout

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
        lateinit var total: TextView
        lateinit var quen: TextView
        lateinit var tv_modifier_names: TextView
        lateinit var delete: ImageButton
        lateinit var delete_item: ImageButton
        lateinit var image_rv: ImageView


        lateinit var count: TextView


        init {
            color = view.findViewById(R.id.colorss)
            title = view.findViewById(R.id.title)
            amount = view.findViewById(R.id.amount)
            base_amount = view.findViewById(R.id.base_amount)
            rv_layout = view.findViewById(R.id.rv_layout)
            delete_item = view.findViewById(R.id.delete_item)
            image_rv = view.findViewById(R.id.image_rv)
            tv_modifier_names = view.findViewById(R.id.tv_modifier_names)
            val cu = databaseForCarts.language
            cu.moveToFirst()
            progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
            progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
            progress.setCancelable(false)

            val popup = Dialog(context)

            var thread = object : Thread() {
                public fun myrun(adapter_position: Int) {
                    val cu1 = databaseForCarts.cartdatas(adapterPosition)
                    cu1.moveToFirst()
                    /* if (cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).equals("1")) {*/
                    if (databaseForCarts.updateQuantity("0",
                                    cu1.getString(cu1.getColumnIndex("ITEM_ID")))) {
                        title.setText(cu1.getString(cu1.getColumnIndex("ITEM_NAME")) + " x " + "(0)")
//                        amount.setText(String.format("%.2f", (cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() * 0)))
                        ttal = 0.0

                        for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                            val cu1a = databaseForCarts.cartdatas(i)
                            cu1a.moveToFirst()

                            val cu_item = databaseForCarts.getitem(cu1a.getString(cu1a.getColumnIndex("ITEM_ID")).toInt())
                            cu_item.moveToFirst()
                            var t_amount = 0.0
                            var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                            var i = 0
                            while (i < (mainstring.size - 1)) {
                                var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                t_amount = t_amount + sub_strig[1].toDouble()
                                i++
                            }


                            ttal = ttal + ((cu1a.getString(cu1a.getColumnIndex("PRICE")).toDouble() + t_amount) * cu1a.getString(cu1a.getColumnIndex("QUANTITY")).toDouble())
                            cu1a.close()
                            cu_item.close()
                        }

                        if (SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).TAB, "0").equals("0"))
//                            fragmentCart!!.set(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context)
                            mprogressCartCallBAck.CartsetCallback(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context, databaseForCarts, cartt)
                        else
                            if (flag == 1) {
                                fragmentHome!!.itemdeleted(context, databaseForCarts)
                                fragmentHome!!.set(btnPay_hm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                            } else {
                                fragmentItems!!.itemdeleted(context, a_carts, databaseForCarts)
                                fragmentItems!!.set(btnPay_itm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                            }
                    }
                    /*} else {
                        val cu = databaseForCarts.language
                        cu.moveToFirst()
                        if (databaseForCarts.updateQuantity("0",
                                        cu1.getString(cu1.getColumnIndex("ITEM_ID")))) {
                            title.setText(cu1.getString(cu1.getColumnIndex("ITEM_NAME")) + " x " + "(0)")
                            amount.setText(String.format("%.2f", (cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() * 0)))
                            ttal = 0.0
                            for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {

                                try {
                                    val cu1 = databaseForCarts.cartdatas(i)
                                    cu1.moveToFirst()

                                    val cu_item = databaseForCarts.getitem(cu1.getString(cu1.getColumnIndex("ITEM_ID")).toInt())
                                    cu_item.moveToFirst()
                                    var t_amount = 0.0
                                    var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                    var i = 0
                                    while (i < (mainstring.size - 1)) {
                                        var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                        t_amount = t_amount + sub_strig[1].toDouble()
                                        i++
                                    }

                                    ttal = ttal + ((cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() + t_amount) * cu1.getString(cu1.getColumnIndex("QUANTITY")).toDouble())

                                } catch (e: Exception) {

                                }

                            }
                            if (SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).TAB, "0").equals("0"))
                                fragmentCart!!.set(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context)
                            else
                                if (flag == 1) {
                                    fragmentHome!!.itemdeleted(context)
                                    fragmentHome!!.set(btnPay_hm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                                } else {
                                    fragmentItems!!.itemdeleted(context)
                                    fragmentItems!!.set(btnPay_itm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                                }
                        }
                    }*/

                    cu1.close()
                }

                override fun run() {
                    super.run()
                }

            }

            rv_layout.setOnClickListener(View.OnClickListener {
                try {
                    val cu1 = databaseForCarts.cartdatas(adapterPosition)
                    cu1.moveToFirst()

                    if (cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).equals("1")) {
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
                        try {
                            if (formatter.format(dot_comma_int).toDouble() == 1.1) {
                                dot_comma = "."
                            }
                        } catch (e: Exception) {
                            dot_comma = ","
                        }
                        dot.setText(dot_comma)
                        ok = popup.findViewById(R.id.ok)
                        total = popup.findViewById(R.id.total)
                        delete = popup.findViewById(R.id.delete)
                        quen = popup.findViewById(R.id.quen)
                        popup.setCancelable(true)
                        /*val cu = databaseForCarts.getLanguage()
                        cu.moveToFirst()*/
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
                                if (total.text.equals("")) {
                                    popup.dismiss()

                                    val cu_modifier = databaseForCarts.getModifier(0, cu1.getString(cu1.getColumnIndex("ITEM_ID")).toString())
                                    cu_modifier.moveToFirst()

                                    if (cu_modifier.count != 0) {
                                        val next = Intent(context, Modifier::class.java)
                                        next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ITEM_ID")).toString())
                                        next.putExtra("COUNT", databaseForCarts.getModifierSize(cu1.getString(cu1.getColumnIndex("ITEM_ID")).toInt()).count.toString())
                                        context.startActivity(next)
                                    }
                                    cu_modifier.close()
                                } else {
                                    if (databaseForCarts.updateQuantity(total.text.toString().replace(",", "."),
                                                    cu1.getString(cu1.getColumnIndex("ITEM_ID")))) {
                                        title.setText(cu1.getString(cu1.getColumnIndex("ITEM_NAME")) + " x " + "(" + total.text.toString() + ")")
                                        amount.setText(formatter.format((cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() * total.text.toString().replace(",", ".").toDouble())))
                                        ttal = 0.0
                                        if (total.text.toString().equals("0"))
                                            image_rv.visibility = View.VISIBLE
                                        else
                                            image_rv.visibility = View.INVISIBLE
                                        for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                                            val cu1 = databaseForCarts.cartdatas(i)
                                            cu1.moveToFirst()


                                            val cu_item = databaseForCarts.getitem(cu1.getString(cu1.getColumnIndex("ITEM_ID")).toInt())
                                            cu_item.moveToFirst()
                                            var t_amount = 0.0
                                            var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                            var i = 0
                                            while (i < (mainstring.size - 1)) {
                                                var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                                t_amount = t_amount + sub_strig[1].toDouble()
                                                i++
                                            }



                                            ttal = ttal + ((cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() + t_amount) * cu1.getString(cu1.getColumnIndex("QUANTITY")).toDouble())
                                            cu1.close()
                                            cu_item.close()
                                        }
                                        if (SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).TAB, "0").equals("0"))
//                                            fragmentCart!!.set(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context)
                                            mprogressCartCallBAck.CartsetCallback(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context, databaseForCarts, cartt)
                                        else
                                            if (flag == 1)
                                                fragmentHome!!.set(btnPay_hm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                                            else
                                                fragmentItems!!.set(btnPay_itm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                                        popup.dismiss()


                                        val cu_modifier = databaseForCarts.getModifier(0, cu1.getString(cu1.getColumnIndex("ITEM_ID")).toString())
                                        cu_modifier.moveToFirst()

                                        if (cu_modifier.count != 0) {
                                            val next = Intent(context, Modifier::class.java)
                                            next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ITEM_ID")).toString())
                                            next.putExtra("COUNT", databaseForCarts.getModifierSize(cu1.getString(cu1.getColumnIndex("ITEM_ID")).toInt()).count.toString())
                                            context.startActivity(next)
                                        }
                                        cu_modifier.close()
                                    }
                                }
                            } catch (e: Exception) {
                                total.setText("")
                                popup.dismiss()
                            }
                        })
                        delete.setOnClickListener(View.OnClickListener {
                            if (total.text.toString().length != 0)
                                total.setText(total.text.toString().substring(0, total.text.toString().length - 1))
                        })
                    } else {
                        popup.setContentView(R.layout.popup_count)
//                    popup.window.setLayout(1000, 610)
                        count = popup.findViewById(R.id.count)
                        ok = popup.findViewById(R.id.ok)
                        quen = popup.findViewById(R.id.quen)

                        popup.setCancelable(false)
                        popup.show()
                        /*val cu = databaseForCarts.language
                        cu.moveToFirst()*/
                        quen.setText(cu.getString(cu.getColumnIndex("QUANTITY")).toString())
                        ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())

                        count.setText(cu1.getString(cu1.getColumnIndex("QUANTITY")).toString())

                        count.setOnTouchListener(object : View.OnTouchListener {
                            override fun onTouch(v: View, event: MotionEvent): Boolean {
                                if (event.getAction() === MotionEvent.ACTION_UP) {
                                    val textLocation = IntArray(2)
                                    count.getLocationOnScreen(textLocation)

                                    if (event.getRawX() <= textLocation[0] + count.getTotalPaddingLeft()) {
                                        if (!count.text.toString().equals("1"))
                                            count.setText((count.text.toString().toInt() - 1).toString())

                                        return true
                                    }


                                    if (event.getRawX() >= textLocation[0] + count.getWidth() - count.getTotalPaddingRight()) {
                                        count.setText((count.text.toString().toInt() + 1).toString())
                                        return true
                                    }
                                }
                                return true
                            }
                        })
                        ok.setOnClickListener(View.OnClickListener {

                            if (databaseForCarts.updateQuantity(count.text.toString(),
                                            cu1.getString(cu1.getColumnIndex("ITEM_ID")))) {
                                title.setText(cu1.getString(cu1.getColumnIndex("ITEM_NAME")) + " x " + "(" + count.text.toString() + ")")
                                amount.setText(formatter.format((cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() * count.text.toString().toDouble())))
                                ttal = 0.0
                                for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                                    try {
                                        val cu1 = databaseForCarts.cartdatas(i)
                                        cu1.moveToFirst()
                                        val cu_item = databaseForCarts.getitem(cu1.getString(cu1.getColumnIndex("ITEM_ID")).toInt())
                                        cu_item.moveToFirst()
                                        var t_amount = 0.0
                                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                        var i = 0
                                        while (i < (mainstring.size - 1)) {
                                            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                            t_amount = t_amount + sub_strig[1].toDouble()
                                            i++
                                        }
                                        ttal = ttal + ((cu1.getString(cu1.getColumnIndex("PRICE")).toDouble() + t_amount) * cu1.getString(cu1.getColumnIndex("QUANTITY")).toDouble())
                                        cu1.close()
                                        cu_item.close()
                                    } catch (e: Exception) {
                                    }
                                }
                                if (count.text.toString().equals("0"))
                                    image_rv.visibility = View.VISIBLE
                                else
                                    image_rv.visibility = View.INVISIBLE
                                if (SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).TAB, "0").equals("0"))
//                                    fragmentCart!!.set(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context)
                                    mprogressCartCallBAck.CartsetCallback(btnPay, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)), context, databaseForCarts, cartt)
                                else
                                    if (flag == 1)
                                        fragmentHome!!.set(btnPay_hm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                                    else
                                        fragmentItems!!.set(btnPay_itm, (cu.getString(cu.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(ttal)))
                                popup.dismiss()


                                val cu_modifier = databaseForCarts.getModifier(0, cu1.getString(cu1.getColumnIndex("ITEM_ID")).toString())
                                cu_modifier.moveToFirst()


                                if (cu_modifier.count != 0) {
                                    val next = Intent(context, Modifier::class.java)
                                    next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ITEM_ID")).toString())
                                    next.putExtra("COUNT", databaseForCarts.getModifierSize(cu1.getString(cu1.getColumnIndex("ITEM_ID")).toInt()).count.toString())
                                    context.startActivity(next)
                                }

                                cu_modifier.close()
                            }

                        })
                    }
//                    cu1.close()
                } catch (e: Exception) {

//                    context.startActivity(Intent(context,Home::class.java))
                }


            })

            delete_item.setOnClickListener(View.OnClickListener {

                /*val dialog = AlertDialog.Builder(context)
                        .setTitle("Delete this item?")
                        .setPositiveButton("Yes") { DialogInterface, i ->
                            delete_item.isEnabled = false
                            rv_layout.visibility = View.GONE
                            val params = rv_layout.getLayoutParams()
                            params.height = 0
                            params.width = 0
                            rv_layout.layoutParams = params

                            progress.show()
                            mprogressCartCallBAck.StartProgressCart(progress)
                            handler.postDelayed({
                                progress.dismiss()
                            }, 1000)

                            try {
                                thread.myrun(adapterPosition)
                                thread.start()
                            } catch (e: Exception) {
                                Log.e("pos RvAdapterCart", e.toString())
                            }
                        }
                        .setNegativeButton("No") { DialogInterface, i ->

                        }
                        .show()*/
                delete_item.isEnabled = false
                rv_layout.visibility = View.GONE
                val params = rv_layout.getLayoutParams()
                params.height = 0
                params.width = 0
                rv_layout.layoutParams = params
                    thread.myrun(adapterPosition)
                    thread.start()

            })

//            cu.close()
        }

    }

    fun set(btn_pay: Button, cart: TextView?, databaseForCart: SQLiteHelper) {
        databaseForCarts = databaseForCart
        ttal = 0.0
        btnPay = btn_pay
        cartt = cart
    }

    fun setactivity(payment: Button?, i: Int, cart: TextView, database: SQLiteHelper) {
        databaseForCarts = database
        flag = i
        btnPay_hm = payment
        cartt = cart
    }

    fun setItems(payment: Button?, i: Int, a_cart: TextView, databaseforItem: SQLiteHelper) {
        databaseForCarts = databaseforItem
        a_carts = a_cart
        flag = i
        btnPay_itm = payment
    }

    fun setCallBack(fragmentCart: FragmentCart) {
        mprogressCartCallBAck = fragmentCart

    }

    fun setCallBack2(fragmentHome: FragmentHome) {
        mprogressCartCallBAck = fragmentHome
    }

    fun setCallBack3(fragmentItems: FragmentItems) {
        mprogressCartCallBAck = fragmentItems
    }

    public interface progressCartCallBAck {
        fun StartProgressCart(progress: ProgressDialog)
        fun CartsetCallback(btnPay: Button?, s: String, context: Context, databaseForCarts: SQLiteHelper, cartt: TextView?)
    }

}