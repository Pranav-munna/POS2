package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestRefund
import com.twixt.pranav.pos.Controller.Responce.ResponceRefund
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Print
import com.twixt.pranav.pos.View.Activity.Receipts
import com.twixt.pranav.pos.View.Activity.RefundItems
import com.twixt.pranav.pos.View.Adapter.RvAdapterReceiptsItems
import com.twixt.pranav.pos.View.Adapter.RvAdapterReceiptsRefundItems
import com.twixt.pranav.pos.View.Adapter.RvAdapterRefund
import java.text.DecimalFormat

/**
 * Created by Pranav on 1/1/2018.
 */
class FragmentRefund : Fragment() {
    lateinit var rv_item_list: RecyclerView
    lateinit var rv_item_list_refunded: RecyclerView
    lateinit var rv_items: RecyclerView
    lateinit var refund: TextView
    lateinit var tv_tax: TextView
    lateinit var receipt_id: TextView
    lateinit var date: TextView
    lateinit var cashier: TextView
    lateinit var total_amount: TextView
    lateinit var total: TextView
    lateinit var title: TextView
    lateinit var tv_discount_refund: TextView
    lateinit var tv_discount_amount_refund: TextView
    lateinit var back: ImageButton

    lateinit var tv_discount: TextView
    lateinit var tv_discount_amount: TextView

    lateinit var message: TextView
    lateinit var ok: Button
    lateinit var print: Button

    var print_items = ""
    var print_items_return = ""
    var print_discount = ""
    var print_discount_amount = ""
    var print_total = ""
    var print_receiptNo = ""
    var print_date = ""
    var tax = ""
    var refunded_discount = ""
    var formatter = DecimalFormat("#,###,##0.00")


    private lateinit var databaseforrefund: SQLiteHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_refund, container, false)

        rv_item_list = mView.findViewById(R.id.rv_item_list)
        rv_item_list_refunded = mView.findViewById(R.id.rv_item_list_refunded)
        rv_items = mView.findViewById(R.id.rv_items)
        refund = mView.findViewById(R.id.refund)
        back = mView.findViewById(R.id.back)
        receipt_id = mView.findViewById(R.id.receipt_id)
        date = mView.findViewById(R.id.date)
        cashier = mView.findViewById(R.id.cashier)
        total_amount = mView.findViewById(R.id.total_amount)
        title = mView.findViewById(R.id.title)
        total = mView.findViewById(R.id.total)
        print = mView.findViewById(R.id.print)
        tv_discount_amount = mView.findViewById(R.id.tv_discount_amount)
        tv_discount = mView.findViewById(R.id.tv_discount)
        tv_tax = mView.findViewById(R.id.tv_tax)
        tv_discount_refund = mView.findViewById(R.id.tv_discount_refund)
        tv_discount_amount_refund = mView.findViewById(R.id.tv_discount_amount_refund)

        databaseforrefund = SQLiteHelper(activity)
        val cu = databaseforrefund.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("RECEIPTS")).toString())
        refund.setText(cu.getString(cu.getColumnIndex("REFUND")).toString())
        total.setText(cu.getString(cu.getColumnIndex("TOTAL")).toString())
        print.setText(cu.getString(cu.getColumnIndex("PRINT")).toString())
        tv_discount.setText(cu.getString(cu.getColumnIndex("DISCOUNT")).toString())
        tv_discount_refund.setText(cu.getString(cu.getColumnIndex("REFUNDEDDISCOUNT")).toString())

        val orderid = arguments.getString("orderId")

        receipt_id.setText("#" + orderid.toString())
        receipt_id.setText("#" + orderid.toString())
        cashier.setText(cu.getString(cu.getColumnIndex("CASHIER")).toString() + " : " + SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_NAME, "Owner"))


        RequestRefund(activity, ResponceProcessorRefund()).getRefundID(orderid.toString(), SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))

        refund.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, RefundItems::class.java)
            intent.putExtra("orderId", orderid)
            activity.finish()
            startActivity(intent)
        })
        back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, Receipts::class.java))
//            activity.onBackPressed()
        })

        print.setOnClickListener(View.OnClickListener {

            val next_pg = Intent(activity, Print::class.java)
            next_pg.putExtra("PRINT_ITEMS", print_items)
//            Log.e("helphelps", print_items_return)
            next_pg.putExtra("PRINT_ITEMS_REFUND", print_items_return)
            next_pg.putExtra("PRINT_DATE", print_date)
            next_pg.putExtra("PRINT_DISCOUNT", print_discount)
            next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", print_discount_amount)
            next_pg.putExtra("PRINT_TOTAL", print_total)
            next_pg.putExtra("PRINT_RECEIPT_NO", print_receiptNo)
            next_pg.putExtra("TAX", tax)
            next_pg.putExtra("REFUNDED_DISCOUNT", refunded_discount)
            startActivity(next_pg)
        })
        cu.close()
        return mView
    }

    inner class ResponceProcessorRefund : ProcessResponcceInterphase<ResponceRefund> {
        override fun processResponce(responce: ResponceRefund) {
            val cu = databaseforrefund.language
            cu.moveToFirst()

            print.visibility = View.VISIBLE
            if (responce == null) {

                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                ok = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                message.setText(cu.getString(cu.getColumnIndex("REFUND_UNAVAILABLE")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("REFUND_UNAVAILABLE")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                print_date = responce.createdAt!!.trim()
                print_discount = responce.discountType!!.trim()
                print_discount_amount = (responce.discountAmount!!.toString().replace(",", ".").toDouble() + responce.refunded_discount.toString().replace(",", ".").toDouble()).toString().trim()
                print_total = responce.payments!![0].amount.toString()
                print_receiptNo = responce.id!!.toString().trim()
                print_date = responce.createdAt!!.toString().trim()
                tv_tax.setText("(" + cu.getString(cu.getColumnIndex("INCLUSIVEOFTAXES")).toString() + ":" + formatter.format(responce.totalTax!!.toDouble()) + ")")
                tax = responce.totalTax.toString()
                refunded_discount = responce.refunded_discount.toString()
                date.setText(responce.createdAt!!.trim())
                if (responce.refunded_discount!!.toDouble() > 0) {

                    tv_discount_refund.visibility = View.VISIBLE
                    tv_discount_amount_refund.visibility = View.VISIBLE
                    tv_discount_amount_refund.setText(cu.getString(cu.getColumnIndex("RS")).toString() + "." + formatter.format(responce.refunded_discount.toString().toDouble()))
                }

                tv_discount_amount.setText(cu.getString(cu.getColumnIndex("RS")).toString() + ".-" + formatter.format(responce.discountAmount.toString().toDouble()))
                total_amount.setText(cu.getString(cu.getColumnIndex("RS")).toString() + "." + formatter.format(responce.total.toString().toDouble()))

                val rvAdapterRefund = RvAdapterRefund(activity)
                rv_items.adapter = rvAdapterRefund
                rv_items.getRecycledViewPool().setMaxRecycledViews(0, 0)
                rvAdapterRefund.set(responce.payments!!)
                rv_items.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)

                val rvAdapterReceiptsItems = RvAdapterReceiptsItems(activity)
                rv_item_list.adapter = rvAdapterReceiptsItems
                rv_item_list.getRecycledViewPool().setMaxRecycledViews(0, 0)
                rvAdapterReceiptsItems.set(responce.orderItems!!)
                rv_item_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


                var i = 0
                while (i < responce.orderItems!!.size) {

                    var j = 0
                    var modifier = " "
                    while (j < responce.orderItems!![i].modifiers!!.size) {
                        modifier = modifier + responce.orderItems!![i].modifiers!![j].modifier_name + "(" + responce.orderItems!![i].modifiers!![j].amount + "),"
                        j++
                    }


                    if (i == 0)
                        print_items = print_items + (responce.orderItems!![i].itemName + " ## " +
                                responce.orderItems!![i].quantity + " x " + formatter.format(responce.orderItems!![i].price.toString().replace(",", ".").toDouble()) + " ## " +
                                String.format("%.2f", responce.orderItems!![i].quantity!!.toDouble() * responce.orderItems!![i].price!!.toDouble()) + "##" + modifier)
                    else
                        print_items = print_items + " !& " + (responce.orderItems!![i].itemName + " ## " +
                                responce.orderItems!![i].quantity + " x " + formatter.format(responce.orderItems!![i].price.toString().replace(",", ".").toDouble()) + " ## " +
                                String.format("%.2f", responce.orderItems!![i].quantity!!.toDouble() * responce.orderItems!![i].price!!.toDouble()) + "##" + modifier)
                    i++
                }

//                Toast.makeText(activity, print_items, Toast.LENGTH_SHORT).show()

                val rvAdapterReceiptsRefundItems = RvAdapterReceiptsRefundItems(activity)
                rv_item_list_refunded.adapter = rvAdapterReceiptsRefundItems
                rv_item_list_refunded.getRecycledViewPool().setMaxRecycledViews(0, 0)
                rvAdapterReceiptsRefundItems.set(responce.refunded_items!!)
                rv_item_list_refunded.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                var j = 0
                while (j < responce.refunded_items!!.size) {
                    if (j == 0)
                        print_items_return = print_items_return + (responce.refunded_items!![j].itemName + " ## " +
                                responce.refunded_items!![j].quantity + " x " + formatter.format(responce.refunded_items!![j].price.toString().replace(",", ".").toDouble()) + " ## " +
                                /*String.format("%.2f", */responce.refunded_items!![j].quantity!!.toDouble() * responce.refunded_items!![j].price!!.toDouble()/*)*/ + "## ")
                    else
                        print_items_return = print_items_return + " !& " + (responce.refunded_items!![j].itemName + " ## " +
                                responce.refunded_items!![j].quantity + " x " + formatter.format(responce.refunded_items!![j].price.toString().replace(",", ".").toDouble()) + " ## " +
                                /*String.format("%.2f",*/ responce.refunded_items!![j].quantity!!.toDouble() * responce.refunded_items!![j].price!!.toDouble()/*)*/ + "## ")
                    j++
                }
//                Toast.makeText(activity, print_items_return, Toast.LENGTH_SHORT).show()


            }
            cu.close()
        }

    }
}