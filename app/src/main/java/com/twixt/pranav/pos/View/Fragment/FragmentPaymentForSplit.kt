package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
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
import com.twixt.pranav.pos.Controller.Request.RequestPay
import com.twixt.pranav.pos.Controller.Request.RequestPayLater
import com.twixt.pranav.pos.Controller.Request.RequestReceiptEmail
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponcePay
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.ErrorPage
import com.twixt.pranav.pos.View.Activity.Home
import com.twixt.pranav.pos.View.Activity.PaymentSplit
import com.twixt.pranav.pos.View.Adapter.RvAdapterDiscount
import com.twixt.pranav.pos.View.Adapter.RvAdapterPayment

/**
 * Created by Pranav on 11/27/2017.
 */
/*private var database: SQLiteHelper? = null*/

class FragmentPaymentForSplit : Fragment() {

    private lateinit var databaseSplit: SQLiteHelper

    lateinit var payment_rv: RecyclerView
    lateinit var discount_rv: RecyclerView
    lateinit var back: ImageButton
    lateinit var split: TextView
    lateinit var total_amount: TextView
    lateinit var title: TextView
    lateinit var i_o_t: TextView
    lateinit var discount_applayed: TextView
    lateinit var t_amount: TextView
    lateinit var offer_applied_title: TextView
    lateinit var payment_type_title: TextView
    lateinit var total_amount_formated: TextView
    lateinit var charge: Button
    lateinit var btn_send: Button
    lateinit var btn_cancel: Button
    lateinit var bn_pay_later: Button
    lateinit var et_email: AppCompatEditText
    lateinit var tv_email: AppCompatTextView

    lateinit var message: TextView
    lateinit var ok: Button

    var s: String = ""
    var total = 0.0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_payment, container, false)

        payment_rv = mView.findViewById(R.id.payment_rv)
        discount_rv = mView.findViewById(R.id.discount_rv)
        back = mView.findViewById(R.id.back)
        split = mView.findViewById(R.id.split)
        total_amount = mView.findViewById(R.id.total_amount)
        charge = mView.findViewById(R.id.charge)
        total_amount_formated = mView.findViewById(R.id.total_amount_formated)
        title = mView.findViewById(R.id.title)
        i_o_t = mView.findViewById(R.id.i_o_t)
        discount_applayed = mView.findViewById(R.id.discount_applayed)
        t_amount = mView.findViewById(R.id.t_amount)
        offer_applied_title = mView.findViewById(R.id.offer_applied_title)
        payment_type_title = mView.findViewById(R.id.payment_type_title)
        bn_pay_later = mView.findViewById(R.id.bn_pay_later)

        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))

        databaseSplit = SQLiteHelper(activity)
        val cu = databaseSplit.getLanguage()
        cu.moveToFirst()

        ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
        title.setText(cu.getString(cu.getColumnIndex("PAYMENT")).toString())
        t_amount.setText(cu.getString(cu.getColumnIndex("TOTALAMOUNT")).toString())
        i_o_t.setText(cu.getString(cu.getColumnIndex("INCLUSIVEOFTAXES")).toString())
        split.setText(cu.getString(cu.getColumnIndex("SPLIT")).toString())
        charge.setText(cu.getString(cu.getColumnIndex("CHARGE")).toString())
        offer_applied_title.setText(cu.getString(cu.getColumnIndex("OFFERS_APPLIED")).toString())
        payment_type_title.setText(cu.getString(cu.getColumnIndex("PAYMENT_TYPE")).toString())


        val rvAdapterPayment = RvAdapterPayment(activity)
        payment_rv.adapter = rvAdapterPayment
        payment_rv.getRecycledViewPool().setMaxRecycledViews(0, 0)
        payment_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val rvAdapterDiscount = RvAdapterDiscount(activity, FragmentPayment())
        discount_rv.adapter = rvAdapterDiscount
        discount_rv.getRecycledViewPool().setMaxRecycledViews(0, 0)
        rvAdapterDiscount.set(discount_applayed, total_amount, total_amount_formated, i_o_t)
        discount_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                startActivity(Intent(activity, ErrorPage::class.java))
            }

        }


        val width_px = Resources.getSystem().displayMetrics.widthPixels
        val height_px = Resources.getSystem().displayMetrics.heightPixels
        val pixeldpi = Resources.getSystem().displayMetrics.densityDpi

        val width_dp = width_px / pixeldpi * 220
        val height_dp = height_px / pixeldpi * 220


        if (width_dp > 700 && height_dp > 700)
            activity.window.setLayout(width_dp.toInt(), height_dp.toInt())


//        RequestPaymentTypes(activity, ResponceprocessorPaymentType()).getPaymentTypes(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))

        if (databaseSplit != null && (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) != 0)) {

            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                val cu = databaseSplit.cartdatas(i)
                cu.moveToFirst()
                total = total + (cu.getString(cu.getColumnIndex("PRICE")).toDouble() * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())
                if (i == (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                    s = s + """{"itemId":"""" + cu.getString(cu.getColumnIndex("ITEM_ID")) +
                            """","quantity":"""" + cu.getString(cu.getColumnIndex("QUANTITY")) +
                            """","itemName":"""" + cu.getString(cu.getColumnIndex("ITEM_NAME")) +
                            """","price":"""" + cu.getString(cu.getColumnIndex("PRICE")) + """","tax":"10"}"""
                } else {
                    s = s + """{"itemId":"""" + cu.getString(cu.getColumnIndex("ITEM_ID")) +
                            """","quantity":"""" + cu.getString(cu.getColumnIndex("QUANTITY")) +
                            """","itemName":"""" + cu.getString(cu.getColumnIndex("ITEM_NAME")) +
                            """","price":"""" + cu.getString(cu.getColumnIndex("PRICE")) + """","tax":"10"},"""
                }
                cu.close()
            }
        }

//        total_amount.setText((Math.round(total * 100.0) / 100.0).toString())
        total_amount.setText(String.format("%.2f", total.toDouble()))

        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, total_amount.text.toString())


        charge.setOnClickListener(View.OnClickListener {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0").equals("0")) {
                message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString(), Toast.LENGTH_SHORT).show()
            } else
                if (databaseSplit != null && (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) != 0)) {

                    val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = cm.activeNetworkInfo
                    val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
                    var discount_no = ""
                    var discount_amount = ""
                    if (discount_applayed.text.trim().length > 0) {
                        val cu2 = databaseSplit.getLanguage()
                        cu2.moveToFirst()

                        val cu = databaseSplit.discountTypesString(discount_applayed.text.toString().replace(cu2.getString(cu2.getColumnIndex("APPLIED")) + ": ", ""))
                        cu.moveToFirst()
                        discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                        if (discount_no == "0")
                            discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                        else {
                            discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
//                            Toast.makeText(activity, discount_amount, Toast.LENGTH_SHORT).show()
                        }
//                        Toast.makeText(activity, cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")) + "\n" + cu.getString(cu.getColumnIndex("DISCOUNT")), Toast.LENGTH_SHORT).show()
                        cu.close()
                        cu2.close()
                    }

                    if (isConnected) {

                        //region PAY
                        RequestPay(activity, ResponceprocessorPay()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                total_amount.text.toString(),
                                discount_amount,
                                discount_amount,
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                discount_no,
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                "[" + s + "]")
                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_PAY, "0").equals("1")) {
                            var i = 0

                            while (i < databaseSplit.pendingPaySize.count) {
                                val cu = databaseSplit.getPendingPay(i)
                                cu.moveToFirst()
//                                Toast.makeText(activity, cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")), Toast.LENGTH_SHORT).show()

                                RequestPay(activity, ResponceprocessorPay()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                        cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                                        cu.getString(cu.getColumnIndex("DISCOUNT")),
                                        cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                                        cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                                        cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                                        cu.getString(cu.getColumnIndex("PAYMENT_TYPE")),
                                        cu.getString(cu.getColumnIndex("ITEMS")))

                                i++
                                cu.close()
                            }

                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_PAY, "0")
                            databaseSplit.deletePay()
                        }

                        //endregion

                    } else {

                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                            startActivity(Intent(activity, ErrorPage::class.java))
                        } else {
                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_PAY, "1")
                            databaseSplit.insertPay(total_amount.text.toString(),
                                    discount_amount,
                                    discount_amount,
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                    discount_no,
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                    "[" + s + "]")

                            if (databaseSplit.deleteCart()) {
                                SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                                Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                            }

                            startActivity(Intent(activity, Home::class.java))
                        }
                    }

                } else {
                    message.setText(cu.getString(cu.getColumnIndex("NO_ITEMS")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("NO_ITEMS")).toString(), Toast.LENGTH_SHORT).show()
                }
        })

        split.setOnClickListener(View.OnClickListener {
            var discount_no = ""
            var discount_amount = ""
            if (discount_applayed.text.trim().length > 0) {
                val cu = databaseSplit.discountTypesString(discount_applayed.text.toString().replace(" Applied: ", ""))
                cu.moveToFirst()
                discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                if (discount_no == "0")
                    discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                else {
                    discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
                }
                cu.close()
            }

            val next_pg = Intent(activity, PaymentSplit::class.java)
            next_pg.putExtra("TOTAL_AMOUNT", /*(Math.round(total * 100.0) / 100.0).toString()*/total_amount.text.toString())
            next_pg.putExtra("ITEMS", s)
            next_pg.putExtra("DISCOUNT_NO", discount_no)
            next_pg.putExtra("DISCOUNT_AMOUNT", discount_amount)
            startActivity(next_pg)
            activity.finish()
        })

        bn_pay_later.setOnClickListener(View.OnClickListener {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

            if (databaseSplit != null && (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) != 0)) {

                val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
                var discount_no = ""
                var discount_amount = ""
                if (discount_applayed.text.trim().length > 0) {
                    val cu2 = databaseSplit.getLanguage()
                    cu2.moveToFirst()

                    val cu = databaseSplit.discountTypesString(discount_applayed.text.toString().replace(cu2.getString(cu2.getColumnIndex("APPLIED")) + ": ", ""))
                    cu.moveToFirst()
                    discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                    if (discount_no == "0")
                        discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                    else {
                        discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
                    }
                    cu2.close()
                    cu.close()
                }

                if (isConnected) {
                    val popupemail = Dialog(activity)
                    popupemail.setContentView(R.layout.popup_email_receipt)
                    btn_cancel = popupemail.findViewById(R.id.btn_cancel)
                    btn_send = popupemail.findViewById(R.id.btn_send)
                    et_email = popupemail.findViewById(R.id.et_email)
                    tv_email = popupemail.findViewById(R.id.tv_email)
//                    tv_email.setText("Pay Later Id:")

                    btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                    btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                    tv_email.setText(cu.getString(cu.getColumnIndex("PAYLATERID")).toString() + " :")

                    btn_cancel.visibility = View.INVISIBLE
                    popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                    popupemail.show()

                    btn_send.setOnClickListener(View.OnClickListener {

                        /*if (isValidEmail(et_email.text.toString())) {*/
                        RequestPayLater(activity, ResponceprocessorPayLater()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                total_amount.text.toString(),
                                discount_amount,
                                discount_amount,
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                discount_no,
                                "0",
                                "[" + s + "]", et_email.text.toString(), "")

                        popupemail.dismiss()

                        /*} else {
                            message.setText(cu.getString(cu.getColumnIndex("INVALIDEMAIL")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }*/
                    })

                } else {
                    val popupemail = Dialog(activity)
                    popupemail.setContentView(R.layout.popup_email_receipt)
                    btn_cancel = popupemail.findViewById(R.id.btn_cancel)
                    btn_send = popupemail.findViewById(R.id.btn_send)
                    et_email = popupemail.findViewById(R.id.et_email)
                    tv_email = popupemail.findViewById(R.id.tv_email)
                    tv_email.setText("Customer email:")

                    btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                    btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                    tv_email.setText(cu.getString(cu.getColumnIndex("SENDRECIPTS")).toString() + " :")

                    btn_cancel.visibility = View.INVISIBLE
                    popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                    popupemail.show()

                    btn_send.setOnClickListener(View.OnClickListener {
                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                            startActivity(Intent(activity, ErrorPage::class.java))
                        } else {
                            if (isValidEmail(et_email.text.toString())) {
                                //region pay later offline
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAY_LATER, "1")
                                databaseSplit.insertPayLater(total_amount.text.toString(),
                                        discount_amount,
                                        discount_amount,
                                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                        discount_no,
                                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                        "[" + s + "]", et_email.text.toString(), " ")

                                if (databaseSplit.deleteCart()) {
                                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
                                }

                                startActivity(Intent(activity, Home::class.java))

                                //endregion
                                popupemail.dismiss()

                            } else {
                                message.setText(cu.getString(cu.getColumnIndex("INVALIDEMAIL")).toString())
                                popup.show()
                                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                            }
                        }

                        /*RequestPayLater(activity, ResponceprocessorPayLater()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                total_amount.text.toString(),
                                discount_amount,
                                discount_amount,
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                discount_no,
                                "0",
                                "[" + s + "]", et_email.text.toString())*/


                    })
                }

            } else {
                message.setText(cu.getString(cu.getColumnIndex("NO_ITEMS")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("NO_ITEMS")).toString(), Toast.LENGTH_SHORT).show()
            }


        })

        back.setOnClickListener(View.OnClickListener { activity.finish() })
        cu.close()
        return mView
    }

    inner class ResponceprocessorPayLater : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
            if (responce.status.equals("success")) {
                if (databaseSplit.deleteCart()) {
                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(activity, Home::class.java))
            }

        }

    }

    inner class ResponceprocessorPay : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
            val cu = databaseSplit.language
            cu.moveToFirst()
            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            popup.setCancelable(false)
            if (responce != null) {
                if (databaseSplit.deleteCart()) {
                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                }
                val popupemail = Dialog(activity)
                popupemail.setContentView(R.layout.popup_email_receipt)
                btn_cancel = popupemail.findViewById(R.id.btn_cancel)
                btn_send = popupemail.findViewById(R.id.btn_send)
                et_email = popupemail.findViewById(R.id.et_email)
                tv_email = popupemail.findViewById(R.id.tv_email)
//                tv_email.setText("Send Receipt to :")

                btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                tv_email.setText(cu.getString(cu.getColumnIndex("SENDRECIPTS")).toString() + " :")

                popupemail.setCancelable(false)
                popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                popupemail.show()
                btn_cancel.setOnClickListener(View.OnClickListener {
                    popupemail.dismiss()
                    startActivity(Intent(activity, Home::class.java))
                })
                btn_send.setOnClickListener(View.OnClickListener {

                    if (isValidEmail(et_email.text.toString())) {
                        RequestReceiptEmail(activity, ResponceprocessorReceiptsEmail()).getReceiptEmail(et_email.text.toString(), responce.orderId.toString(),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
                        popupemail.dismiss()

                    } else {
                        message.setText(cu.getString(cu.getColumnIndex("INVALIDEMAIL")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                    }
                })

//                startActivity(Intent(activity, Home::class.java))
            } else {
                message.setText(cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            }
            cu.close()
        }


    }

    inner class ResponceprocessorReceiptsEmail : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce.status.equals("success")) {
                startActivity(Intent(activity, Home::class.java))
            }
            /*else{
                Toast.makeText(activity,"xxx",Toast.LENGTH_SHORT).show()
            }*/
        }

    }

    fun isValidEmail(e_mail: String): Boolean {
        return e_mail.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}".toRegex()) && e_mail.length <= 320
    }

    fun set(cu: Cursor, context: Context, discount_aplayed: TextView, ttl_amount: TextView, i: Int) {

        databaseSplit = SQLiteHelper(context)
        val cu1 = databaseSplit!!.language
        cu1.moveToFirst()
        if (i == 1) {
            discount_aplayed.setText(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": " + cu.getString(cu.getColumnIndex("LABEL")))
            /*Toast.makeText(context, cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")) + " "
                    + cu.getString(cu.getColumnIndex("DISCOUNT")), Toast.LENGTH_SHORT).show()*/
            if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                var amount = ttl_amount.text.toString().replace(",", ".").toDouble() * cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble() / 100
                amount = ttl_amount.text.toString().replace(",", ".").toDouble() - amount
                ttl_amount.setText(String.format("%.2f", amount.toDouble()))
            } else {
                var amount = ttl_amount.text.toString().replace(",", ".").toDouble() - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble()
                ttl_amount.setText(String.format("%.2f", amount.toDouble()))
            }

            SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).TOTAL_AMOUNT, ttl_amount.text.toString())
        } else if (i == 0) {
            discount_aplayed.setText("")
            if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                var discount_amounts = ((ttl_amount.text.toString().replace(",", ".").toDouble() * 100 /
                        (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) -
                        ttl_amount.text.toString().replace(",", ".").toDouble()) + ttl_amount.text.toString().replace(",", ".").toDouble()
                ttl_amount.setText(String.format("%.2f", discount_amounts.toDouble()))
            } else {
                ttl_amount.setText(String.format("%.2f", ttl_amount.text.toString().replace(",", ".").toDouble() + cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble()))
            }

        }
        cu1.close()
    cu.close()
    }
}