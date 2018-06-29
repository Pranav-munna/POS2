package com.twixt.pranav.pos.View.Adapter

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.twixt.pranav.pos.Controller.Responce2Array.OrderItem
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentViewPayLaterItems
import java.text.DecimalFormat

/**
 * Created by Pranav on 2/5/2018.
 */
class RvAdapterViewLaterItems(var context: Context, val fragmentViewPayLaterItems: FragmentViewPayLaterItems?) : RecyclerView.Adapter<RvAdapterViewLaterItems.ViewHolder>() {
    private lateinit var databaseViewLater: SQLiteHelper
    lateinit var orderItems: List<OrderItem>
    var items = ""
    var formatter = DecimalFormat("#,###,##0.00")
    var formatter_ = DecimalFormat("###.#")
    var dot_comma_int = 1.1
    var dot_comma = ""

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.rv_view_later_items, parent, false))
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder!!.txt_pro_name.setText(orderItems[position].itemName)
        holder!!.txt_quantity.setText( formatter_.format(orderItems[position].quantity!!.toDouble()) + " x " + formatter.format(orderItems[position].price))

        var i = 0
        var modifier = ""
        while (i < orderItems[position].modifiers!!.size) {
            modifier = modifier + orderItems[position].modifiers!![i].modifier_name + "(" + orderItems[position].modifiers!![i].amount + "),"
            i++
        }

        holder.tv_modifier.setText(modifier)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt_pro_name: TextView
        var txt_quantity: TextView
        var tv_modifier: TextView
        var id_layout: RelativeLayout
        var checking: CheckBox

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
        lateinit var delete: ImageButton
        lateinit var count: TextView


        init {
            txt_quantity = view.findViewById(R.id.txt_quantity)
            txt_pro_name = view.findViewById(R.id.txt_pro_name)
            tv_modifier = view.findViewById(R.id.tv_modifier)
            id_layout = view.findViewById(R.id.id_layout)
            checking = view.findViewById(R.id.checking)

            val popup = Dialog(context)

            id_layout.setOnClickListener {

                //                txt_quantity.setText(" x" + orderItems[position].quantity + " " + orderItems[position].price)

                var qty = txt_quantity.text.toString().replace("x", "")
                qty = qty.replace("  " + formatter.format(orderItems[adapterPosition].price), "")

//                Toast.makeText(context, qty, Toast.LENGTH_SHORT).show()

                if (orderItems[adapterPosition].sellingType.equals("1")) {

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
                    val cu = databaseViewLater.language
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
                            if (total.text.equals("") || total.text.toString().replace(",", ".").toDouble() <= 0) {
                                popup.dismiss()
                            } else {
                                popup.dismiss()
                                if (checking.isChecked == true) {
                                    items = items.replace(""",{"itemId":"""" + orderItems[adapterPosition].itemId +
                                            """","quantity":"""" + qty + """","price":"""" + orderItems[adapterPosition].price + """"}""", "")

                                }
                                items = items + """,{"itemId":"""" + orderItems[adapterPosition].itemId +
                                        """","quantity":"""" + total.text.toString().replace(",", ".") + """","price":"""" + orderItems[adapterPosition].price + """"}"""

                                checking.isChecked = true
                                txt_quantity.setText( total.text.toString() + " x " + formatter.format(orderItems[adapterPosition].price))
                                fragmentViewPayLaterItems!!.set(items, context)

                            }
                        } catch (e: Exception) {
                            total.setText("")
                        }

                    })
                    delete.setOnClickListener(View.OnClickListener {
                        if (total.text.toString().length != 0)
                            total.setText(total.text.toString().substring(0, total.text.toString().length - 1))
                    })
//                    cu.close()
                } else {

                    popup.setContentView(R.layout.popup_count)
//                    popup.window.setLayout(1000, 610)
                    count = popup.findViewById(R.id.count)
                    ok = popup.findViewById(R.id.ok)
                    quen = popup.findViewById(R.id.quen)

                    popup.setCancelable(false)
                    popup.show()
                    val cu = databaseViewLater.language
                    cu.moveToFirst()
                    quen.setText(cu.getString(cu.getColumnIndex("QUANTITY")).toString())
                    ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())

                    count.setText(orderItems[adapterPosition].quantity)

                    count.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View, event: MotionEvent): Boolean {
                            if (event.getAction() === MotionEvent.ACTION_UP) {
                                val textLocation = IntArray(2)
                                count.getLocationOnScreen(textLocation)

                                if (event.getRawX() <= textLocation[0] + count.getTotalPaddingLeft()) {
                                    if (!count.text.toString().equals("0"))
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
                        popup.dismiss()
                        if (checking.isChecked == true) {
                            items = items.replace(""",{"itemId":"""" + orderItems[adapterPosition].itemId +
                                    """","quantity":"""" + qty + """","price":"""" + orderItems[adapterPosition].price + """"}""", "")
                        }
                        items = items + """,{"itemId":"""" + orderItems[adapterPosition].itemId +
                                """","quantity":"""" + count.text.toString() + """","price":"""" + orderItems[adapterPosition].price + """"}"""

                        checking.isChecked = true
                        txt_quantity.setText( count.text.toString() + " x " + formatter.format(orderItems[adapterPosition].price))
                        fragmentViewPayLaterItems!!.set(items, context)

                    })
//                    cu.close()
                }


            }

        }

    }

    fun set(orderItemz: List<OrderItem>, databaseforViewPayLAterItems: SQLiteHelper) {
        databaseViewLater=databaseforViewPayLAterItems
        orderItems = orderItemz

    }
}