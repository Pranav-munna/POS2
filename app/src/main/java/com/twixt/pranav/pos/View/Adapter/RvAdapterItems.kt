package com.twixt.pranav.pos.View.Adapter

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.twixt.pranav.pos.Controller.Responce.ResponceProducts
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Modifier
import com.twixt.pranav.pos.View.Fragment.FragmentItems
import java.text.DecimalFormat
import java.util.*


/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterItems(var context: Context, var fragments: FragmentItems) : RecyclerView.Adapter<RvAdapterItems.ViewHolder>() {
    var items = ArrayList<ResponceProducts>()
    lateinit var databaseforItem: SQLiteHelper
    var qty = 1
    var size = 0
    var flag = ""
    var count = 2
    var catId = ""
    var dot_comma_int = 1.1
    var dot_comma = ""
    var cm: MenuItem? = null
    var checks: TextView? = null
    var recyclerview_lists: RecyclerView? = null
    var payments: Button? = null
    lateinit var a_carts: TextView
    var formatter = DecimalFormat("#,###,##0.00")

    var countx = 0
    lateinit var progress: ProgressDialog
    val handler = Handler()
    val handler2 = Handler()
    private lateinit var mItemsCallback: ItemsCallback

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        progress = ProgressDialog(context)
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_items, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (flag.equals("1")) {

            /* databaseforItem.insertItems(position.toString(),
                     items[position].id.toString(),
                     items[position].name.toString(),
                     items[position].price.toString(),
                     items[position].shape.toString(),
                     items[position].color.toString(),
                     items[position].image.toString(),
                     catId,
                     items[position].sellingType.toString(),
                     items[position].tax.toString())

 //            Toast.makeText(context, size.toString(), Toast.LENGTH_SHORT).show()

             if (items[position].shape.toString().length <= 0) {
                 Glide.with(context)
                         .asBitmap()
                         .load(items[position].image)
                         .into(holder.colors)
             } else {
                 if (items[position].shape.toString().equals("shape-1"))
                     holder.colors.setImageResource(R.drawable.shape1)
                 if (items[position].shape.toString().equals("shape-2"))
                     holder.colors.setImageResource(R.drawable.shape2)
                 if (items[position].shape.toString().equals("shape-3"))
                     holder.colors.setImageResource(R.drawable.shape3)
                 if (items[position].shape.toString().equals("shape-4"))
                     holder.colors.setImageResource(R.drawable.shape4)
                 holder.colors.setColorFilter(Color.parseColor("#" + items[position].color.toString()))
 //                Toast.makeText(context,"#" + items[position].color.toString(),Toast.LENGTH_SHORT).show()
             }
             holder.title.setText(items[position].name.toString())
             holder.amount.setText("kr " + formatter.format(items[position].price!!.toDouble()))
             try {
                 if (position == items.size - 1) {
                     RequestProducts(context, ResponceProcessorItems_()).getProducts(catId, count++)
                 }
             } catch (e: Exception) {
             }
             */

        } else if (flag.equals("0")) {

            val cu = databaseforItem.getItems(position, catId)
            cu.moveToFirst()

            if (cu.getString(cu.getColumnIndex("SHAPE")).toString().length <= 0) {
                Glide.with(context)
                        .asBitmap()
                        .load(cu.getString(cu.getColumnIndex("IMAGE")).toString())
                        .into(holder.colors)
            } else {
                if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-1"))
                    holder.colors.setImageResource(R.drawable.shape1)
                if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-2"))
                    holder.colors.setImageResource(R.drawable.shape2)
                if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-3"))
                    holder.colors.setImageResource(R.drawable.shape3)
                if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-4"))
                    holder.colors.setImageResource(R.drawable.shape4)
                holder.colors.setColorFilter(Color.parseColor("#" + cu.getString(cu.getColumnIndex("COLOR")).toString()))
            }
            holder.title.setText(cu.getString(cu.getColumnIndex("NAME")).toString())
            holder.amount.setText("kr " + formatter.format(cu.getString(cu.getColumnIndex("PRICE")).toDouble()))
            cu.close()
        } else {
            try {
                val cu = databaseforItem.getItems(position)
                cu.moveToFirst()
                if (cu.getString(cu.getColumnIndex("SHAPE")).toString().length <= 0) {
                    Glide.with(context)
                            .asBitmap()
                            .load(cu.getString(cu.getColumnIndex("IMAGE")).toString())
                            .into(holder.colors)
                } else {
                    if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-1"))
                        holder.colors.setImageResource(R.drawable.shape1)
                    if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-2"))
                        holder.colors.setImageResource(R.drawable.shape2)
                    if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-3"))
                        holder.colors.setImageResource(R.drawable.shape3)
                    if (cu.getString(cu.getColumnIndex("SHAPE")).toString().equals("shape-4"))
                        holder.colors.setImageResource(R.drawable.shape4)
                    holder.colors.setColorFilter(Color.parseColor("#" + cu.getString(cu.getColumnIndex("COLOR")).toString()))
                }
                holder.title.setText(cu.getString(cu.getColumnIndex("NAME")).toString())
                holder.amount.setText("kr " + formatter.format(cu.getString(cu.getColumnIndex("PRICE")).toDouble()))
                cu.close()
            } catch (e: Exception) {

            }


        }
    }

    /*inner class ResponceProcessorItems_ : ProcessResponcceInterphase<Array<ResponceProducts>> {
        override fun processResponce(responce: Array<ResponceProducts>) {
            if (responce != null) {
                val list = ArrayList(Arrays.asList(*responce))
                items.addAll(list)
                notifyItemRangeInserted(items.size - list.size, list.size)
            }
        }
    }*/


    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var colors: ImageView
        lateinit var layout_rl: RelativeLayout
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
        lateinit var title: TextView
        lateinit var amount: TextView
        lateinit var quen: TextView
        lateinit var delete: ImageButton
        val popup = Dialog(context)


        init {
            colors = view.findViewById(R.id.colorss)
            layout_rl = view.findViewById(R.id.layout_rl)
            title = view.findViewById(R.id.title)
            amount = view.findViewById(R.id.amount)
            val cu = databaseforItem.getLanguage()
            cu.moveToFirst()

            progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
            progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
            progress.setCancelable(false)

            mItemsCallback.itemprogres(progress)

            var thread = object : Thread() {

                public fun iteminsert(adapter_position: Int) {
                    val cu1: Cursor?
                    if (flag.equals("2"))
                        cu1 = databaseforItem.getItems(adapter_position)
                    else
                        cu1 = databaseforItem.getItems(adapter_position, catId)

                    try {
                        cu1.moveToFirst()
//                Toast.makeText(context,(cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),Toast.LENGTH_SHORT).show()

                        if (flag.equals("0") || flag.equals("2")) {
                            if (cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString().equals("1")) {
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
                                val cu = databaseforItem.getLanguage()
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
                                        } else if (databaseforItem.insetCartData(cu1.getString(cu1.getColumnIndex("ID")).toString().toInt(),
                                                        cu1.getString(cu1.getColumnIndex("NAME")).toString(),
                                                        total.text.toString().replace(",", ".").toDouble(),
                                                        cu1.getString(cu1.getColumnIndex("PRICE")).toString(),
                                                        cu1.getString(cu1.getColumnIndex("SHAPE")).toString(),
                                                        cu1.getString(cu1.getColumnIndex("COLOR")).toString(),
                                                        cu1.getString(cu1.getColumnIndex("IMAGE")).toString(),
                                                        cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString(), "",
                                                        (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                                        (cu1.getString(cu1.getColumnIndex("TAX")).toDouble() + 1).toString())) {

                                            popup.dismiss()
                                            fragments.incrementcart2(context, checks, recyclerview_lists, payments, a_carts, databaseforItem)
                                            fragments.itemdeleted(context, a_carts, databaseforItem)


                                            val cu_modifier = databaseforItem.getModifier(0, cu1.getString(cu1.getColumnIndex("ID")).toString())
                                            cu_modifier.moveToFirst()



                                            if (cu_modifier.count != 0) {
                                                val next = Intent(context, Modifier::class.java)
                                                next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ID")).toString())
                                                next.putExtra("COUNT", databaseforItem.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString())
                                                context.startActivity(next)
                                            }

                                            cu_modifier.close()
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
                                if (databaseforItem != null) {
                                    val cu = databaseforItem!!.getLanguage()
                                    cu.moveToFirst()
                                    if (databaseforItem.insetCartData(cu1.getString(cu1.getColumnIndex("ID")).toInt(),
                                                    cu1.getString(cu1.getColumnIndex("NAME")).toString(),
                                                    qty.toDouble(),
                                                    cu1.getString(cu1.getColumnIndex("PRICE")).toString(),
                                                    cu1.getString(cu1.getColumnIndex("SHAPE")).toString(),
                                                    cu1.getString(cu1.getColumnIndex("COLOR")).toString(),
                                                    cu1.getString(cu1.getColumnIndex("IMAGE")).toString(),
                                                    cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString(), "",
                                                    (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                                    cu1.getString(cu1.getColumnIndex("TAX")).toString())) {
//                                Toast.makeText(context, cu1.getString(cu1.getColumnIndex("NAME")).toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                                        fragments.incrementcart2(context, checks, recyclerview_lists, payments, a_carts, databaseforItem)
                                        fragments.itemdeleted(context, a_carts, databaseforItem)

                                        try {
                                            val cu_modifier = databaseforItem.getModifier(0, cu1.getString(cu1.getColumnIndex("ID")).toString())
                                            cu_modifier.moveToFirst()

//                                Toast.makeText(context, databaseforItem.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString(),Toast.LENGTH_SHORT).show()

                                            if (cu_modifier.count != 0) {
                                                val next = Intent(context, Modifier::class.java)
                                                next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ID")).toString())
                                                next.putExtra("COUNT", databaseforItem.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString())
                                                context.startActivity(next)
                                            }
                                            cu_modifier.close()
                                        } catch (e: Exception) {
                                        }


                                    }
                                    cu.close()
                                }
                            }
                        } else {
                            if (items[adapter_position].sellingType.toString().equals("1")) {
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
                                val cu = databaseforItem.getLanguage()
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
                                        if (total.text.equals("")) {
                                            popup.dismiss()
                                        } else if (databaseforItem.insetCartData(items[adapter_position].id!!.toInt(),
                                                        items[adapter_position].name.toString(),
                                                        total.text.toString().replace(",", ".").toDouble(),
                                                        items[adapter_position].price!!.toString(),
                                                        items[adapter_position].shape.toString(),
                                                        items[adapter_position].color.toString(),
                                                        items[adapter_position].image.toString(),
                                                        items[adapter_position].sellingType.toString(), "",
                                                        (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                                        items[adapter_position].tax.toString())) {
//                                Toast.makeText(context, items[adapter_position].name.toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                                            popup.dismiss()
                                            fragments.incrementcart(cm, context, checks, recyclerview_lists, payments, a_carts)
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
                                if (databaseforItem != null) {
                                    val cu = databaseforItem.getLanguage()
                                    cu.moveToFirst()
                                    if (databaseforItem.insetCartData(items[adapter_position].id!!.toInt(),
                                                    items[adapter_position].name.toString(),
                                                    qty.toDouble(),
                                                    items[adapter_position].price!!.toString(),
                                                    items[adapter_position].shape.toString(),
                                                    items[adapter_position].color.toString(),
                                                    items[adapter_position].image.toString(),
                                                    items[adapter_position].sellingType.toString(), "",
                                                    (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                                    items[adapter_position].tax.toString())) {
//                                Toast.makeText(context, items[adapter_position].name.toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                                        fragments.incrementcart(cm, context, checks, recyclerview_lists, payments, a_carts)
                                    }
                                    cu.close()
                                }
                            }
                        }
                        cu1.close()
                    } catch (e: Exception) {
                    }
                }

                override fun run() {
                    super.run()
                }
            }


            layout_rl.setOnClickListener(View.OnClickListener {


                mItemsCallback.PassItemToCart(adapterPosition, flag, context, catId, checks, recyclerview_lists, payments, a_carts, databaseforItem)


               /* countx++
                handler2.removeCallbacksAndMessages(null)
                try {
                    if (countx == 6) {
                        try {
                            progress.show()
                        } catch (e: Exception) {
                        }

                        handler.postDelayed({
                            try {
                                progress.dismiss()
                            } catch (e: Exception) {
                            }

                            countx = 0
                        }, 2000)
                    }
                    handler2.postDelayed({
                        countx = 0
                    }, 2000)
                } catch (e: Exception) {
                }*/

                /*try {
                    thread.iteminsert(adapterPosition)
                    thread.start()
                } catch (e: Exception) {
                }*/


            })
            cu.close()
        }
    }

    fun set(list: ArrayList<ResponceProducts>, cartMenu: MenuItem?, check: TextView, recyclerview_list: RecyclerView, payment: Button?, cat_id: String) {
        countx = 0
        catId = cat_id
        items = list
        cm = cartMenu
        checks = check
        recyclerview_lists = recyclerview_list
        payments = payment
        flag = "1"
        size = list.size
    }

    fun set1(check: TextView?, recyclerview_list_cart: RecyclerView, payment: Button?, cat_id: String/*, cartMenu: MenuItem*/, a_cart: TextView, databaseforItems: SQLiteHelper) {
        databaseforItem = databaseforItems
        countx = 0
        a_carts = a_cart
        catId = cat_id
//        cm = cartMenu
        checks = check
        recyclerview_lists = recyclerview_list_cart
        payments = payment
        flag = "0"
        size = databaseforItems.getItemssize(cat_id).count
    }

    fun set3(check: TextView?, recyclerview_list_cart: RecyclerView?, payment: Button?/*, cartMenu: MenuItem*/, a_cart: TextView, databaseforItems: SQLiteHelper) {
        databaseforItem = databaseforItems
        countx = 0
        a_carts = a_cart
        checks = check
        recyclerview_lists = recyclerview_list_cart
        payments = payment
        flag = "2"
        size = databaseforItems.getItemssize().count

    }

    fun setCallback(onItemSelectedListener: FragmentItems) {
        mItemsCallback = onItemSelectedListener
    }


    public interface ItemsCallback {
        fun itemprogres(progress: ProgressDialog)
        fun PassItemToCart(adapterPosition: Int, flag: String, context: Context, catId: String, checks: TextView?, recyclerview_lists: RecyclerView?, payments: Button?, a_carts: TextView, databaseforItem: SQLiteHelper)
    }
}