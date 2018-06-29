package com.twixt.pranav.pos.View.Adapter

import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.Responce2Array.OrderItem
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentRefundItems
import java.text.DecimalFormat

/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterRefundItems(var context: Context, val fragmentRefundItems: FragmentRefundItems, cu: Cursor) : RecyclerView.Adapter<RvAdapterRefundItems.ViewHolder>() {
    private var database: SQLiteHelper? = null
    lateinit var orderItem: List<OrderItem>
    var refunditems = ""
    var count_quantity = ""
    var total_amt = 0.0
    var percentage = 100.0
    var dot_comma_int = 1.1
    var dot_comma = ""
    var cu1 = cu
    lateinit var splits: TextView
    var formatter = DecimalFormat("#,###,##0.00")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        database = SQLiteHelper(context)
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_refund_items, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.checkBox.setText(orderItem[position].itemName)
        holder.amount.setText(orderItem[position].quantity + " x " + formatter.format((orderItem[position].price!! * (100 - percentage) / 100).toDouble()))
//        Toast.makeText(context, "" + (percentage), Toast.LENGTH_LONG).show()
    }


    override fun getItemCount(): Int {
        return orderItem.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var amount: TextView
        var checkBox: CheckBox


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
            amount = view.findViewById(R.id.amount)
            checkBox = view.findViewById(R.id.checkBox)

            val popup = Dialog(context)

            checkBox.setOnClickListener(View.OnClickListener {

                if (checkBox.isChecked) {
//                    orderItem[adapterPosition].quantity = "0"
                    checkBox.isChecked = false
                    if (orderItem[adapterPosition].sellingType.equals("1")) {
                        //region x
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
                        popup.setCancelable(false)
                        val cu = database!!.language
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
                        //endregion
                        ok.setOnClickListener(View.OnClickListener {
                            try {
                                if (total.text.equals("")/*|| total.text.toString().replace(",", ".").toDouble() <= 0*/) {
                                    checkBox.isChecked = false
                                    popup.dismiss()
                                } else {
                                    checkBox.isChecked = true
                                    if (total.text.toString().replace(",", ".").toDouble() <= orderItem[adapterPosition].quantity.toString().toDouble()) {
                                        count_quantity = total.text.toString().trim().replace(",", ".")
                                        orderItem[adapterPosition].count = count_quantity
                                        amount.setText(count_quantity + " x " + formatter.format((orderItem[adapterPosition].price!! * (100 - percentage) / 100).toDouble()))

                                        total_amt = total_amt + (orderItem[adapterPosition].price.toString().toDouble() * (100 - percentage) / 100 * /*orderItem[adapterPosition].quantity.toString()*/count_quantity.toDouble())
                                        if (refunditems.equals(""))
                                            refunditems = """#,{"productId":"""" + orderItem[adapterPosition].itemId + """","count":"""" + /*orderItem[adapterPosition].quantity*/count_quantity + """"}"""
                                        else
                                            refunditems = refunditems + """,{"productId":"""" + orderItem[adapterPosition].itemId + """","count":"""" + /*orderItem[adapterPosition].quantity*/count_quantity + """"}"""
                                        fragmentRefundItems.set(total_amt, cu,splits)
                                        popup.dismiss()
                                    } else {
                                        checkBox.isChecked = false
                                        total.setText(orderItem[adapterPosition].quantity.toString())
                                        fragmentRefundItems.set(total_amt, cu,splits)
                                    }
                                }
                            } catch (e: Exception) {
                                total.setText("")
                            }
                        })
                        delete.setOnClickListener(View.OnClickListener {
                            if (total.text.toString().length != 0)
                                total.setText(total.text.toString().substring(0, total.text.toString().length - 1))
                        })
//                        cu.close()
                    } else {
                        popup.setContentView(R.layout.popup_count)

                        count = popup.findViewById(R.id.count)
                        ok = popup.findViewById(R.id.ok)
                        quen = popup.findViewById(R.id.quen)

                        popup.setCancelable(false)
                        popup.show()
                        val cu = database!!.language
                        cu.moveToFirst()
                        quen.setText(cu.getString(cu.getColumnIndex("QUANTITY")).toString())
                        ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())

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
                                        if (!count.text.toString().equals(orderItem[adapterPosition].quantity))
                                            count.setText((count.text.toString().toInt() + 1).toString())
                                        return true
                                    }
                                }
                                return true
                            }
                        })
                        ok.setOnClickListener(View.OnClickListener {
                            checkBox.isChecked = true
                            count_quantity = count.text.toString().trim()
                            orderItem[adapterPosition].count = count_quantity
                            amount.setText(count_quantity + " x " + String.format("%.2f", (orderItem[adapterPosition].price!! * (100 - percentage) / 100).toDouble()))

                            total_amt = total_amt + (orderItem[adapterPosition].price.toString().toDouble() * (100 - percentage) / 100 * /*orderItem[adapterPosition].quantity.toString()*/count_quantity.toDouble())
                            if (refunditems.equals(""))
                                refunditems = """#,{"productId":"""" + orderItem[adapterPosition].itemId + """","count":"""" + /*orderItem[adapterPosition].quantity*/count_quantity + """"}"""
                            else
                                refunditems = refunditems + """,{"productId":"""" + orderItem[adapterPosition].itemId + """","count":"""" + /*orderItem[adapterPosition].quantity*/count_quantity + """"}"""
                            fragmentRefundItems.set(total_amt, cu,splits)
                            popup.dismiss()
                        })
//                        cu.close()
                    }
                } else if (!checkBox.isChecked) {
//                    Toast.makeText(context, orderItem[adapterPosition].count.toString(), Toast.LENGTH_SHORT).show()
                    total_amt = total_amt - ((orderItem[adapterPosition].price!! * (100 - percentage) / 100).toString().replace(",", ".").toDouble() * orderItem[adapterPosition].count.toString().toDouble())
                    refunditems = refunditems.replace(""",{"productId":"""" + orderItem[adapterPosition].itemId + """","count":"""" + orderItem[adapterPosition].count + """"}""", "")
                    fragmentRefundItems.set(total_amt, cu1,splits)
                }

//                fragmentRefundItems.set(total_amt)
            })
        }
    }

    fun set(orderItems: List<OrderItem>, percen: Double, split: TextView) {
        splits = split
        orderItem = orderItems
        percentage = percen
    }

    fun get(): String {
        return refunditems
    }

    fun gettotal(): String {
        return total_amt.toString()
    }
}