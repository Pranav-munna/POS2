package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestForPayLater
import com.twixt.pranav.pos.Controller.Request.RequestReceiptEmail
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLater
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Home
import com.twixt.pranav.pos.View.Activity.PayLaterList
import com.twixt.pranav.pos.View.Activity.Print
import java.text.DecimalFormat

/*var amount: String = ""*/

class FragmentCalculationPayLater : Fragment() {

    var amount: String = ""
    lateinit var order_id: String
    var network = false
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
    lateinit var progress: ProgressDialog

    lateinit var message: TextView
    lateinit var ok: Button
    var print_receiptNo = ""
    lateinit var btn_send: Button
    lateinit var btn_cancel: Button
    lateinit var bn_pay_later: Button
    lateinit var et_email: AppCompatEditText
    lateinit var tv_email: AppCompatTextView
    lateinit var viewz: View
    lateinit var btn_new_sale: Button
    lateinit var btn_print: Button

    var print_items = ""
    var print_date = ""
    var print_discount = ""
    var print_discount_amount = ""
    var print_total = ""
    var formatter = DecimalFormat("#,###,##0.00")

    val handler = Handler()

    lateinit var database: SQLiteHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_calculation, container, false)
        database = SQLiteHelper(activity)
        val cu_language = database!!.language
        cu_language.moveToFirst()
        progress = ProgressDialog(activity)

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

        progress.setTitle(cu_language.getString(cu_language.getColumnIndex("LOADING")).toString())
        progress.setMessage(cu_language.getString(cu_language.getColumnIndex("WAIT")).toString() + "...")
        progress.setCancelable(false)

        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))


        /*).toDouble()*/
        amount = arguments.getString("TOTAL_AMOUNT").toString()

        order_id = arguments.getString("ORDERID")
        var discount_no = arguments.getString("DISCOUNT_NO")
        var discount_amount = arguments.getString("DISCOUNT_AMOUNT")
        var total = arguments.getString("TOTAL").toDouble()

        print_items = arguments.getString("PRINT_ITEMS").toString()
        print_date = arguments.getString("PRINT_DATE").toString()
        print_discount = arguments.getString("PRINT_DISCOUNT").toString()
        print_discount_amount = arguments.getString("PRINT_DISCOUNT_AMOUNT").toString()
        print_total = arguments.getString("PRINT_TOTAL").toString()

        tv_total.setText(formatter.format(amount.replace(",", ".").toDouble()))


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
                        et_change_amount.setText(formatter.format(et_cash.text.toString().replace(",", ".").toDouble() - amount.toDouble()))
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


        /*btn_twenty.setOnClickListener {
            if (et_cash.text.toString().equals("")) {
                et_cash.setText("20")
            } else et_cash.setText((et_cash.text.toString().toDouble() + 20).toString())
        }*/
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

        btn_checkout.setOnClickListener {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

            if (isConnected) {
                //region PAY
                progress.show()

                print_discount = discount_no
                print_discount_amount = discount_amount
                print_total = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim()


                progress.show()

                print_discount = discount_no
                print_discount_amount = discount_amount
                RequestForPayLater(activity, ResponceProcessorForPayLaterr()).isSuccess(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "1"),
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"),
                        total.toString(), discount_amount, discount_amount,
                        amount.toString().trim(),
                        discount_no, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"), order_id)

//                    Toast.makeText(activity,total_amount.text.toString().trim(),Toast.LENGTH_SHORT).show()
//                    wifi = false


                handler.postDelayed({

                    if (!network) {
                        progress.dismiss()

                        message.setText(cu_language.getString(cu_language.getColumnIndex("CHECK_NETWORK")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener {
                            popup.dismiss()
                        })

                        network = false
                    }

                }, 5000)
                //endregion

            }
        }

        back.setOnClickListener { activity.onBackPressed() }
        cu_language.close()
        return mView
    }

    inner class ResponceProcessorForPayLaterr : ProcessResponcceInterphase<ResponcePayLater> {
        override fun processResponce(responce: ResponcePayLater) {
            val cu = database!!.language
            cu.moveToFirst()
            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            popup.setCancelable(false)
            ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())

            if (responce.status.equals("success")) {
                network = true
                print_receiptNo = responce.orderId.toString()
                val popupemail = Dialog(activity)
                popupemail.setContentView(R.layout.popup_email_receipt)
                btn_cancel = popupemail.findViewById(R.id.btn_cancel)
                btn_send = popupemail.findViewById(R.id.btn_send)
                et_email = popupemail.findViewById(R.id.et_email)
                tv_email = popupemail.findViewById(R.id.tv_email)
                viewz = popupemail.findViewById(R.id.viewz)
                btn_print = popupemail.findViewById(R.id.btn_print)
                btn_new_sale = popupemail.findViewById(R.id.btn_new_sale)
                btn_new_sale.visibility = View.VISIBLE
                viewz.visibility = View.VISIBLE
                btn_print.visibility = View.VISIBLE

                btn_new_sale.setText(cu.getString(cu.getColumnIndex("NEWSALE")).toString())
                btn_print.setText(cu.getString(cu.getColumnIndex("PRINT")).toString())
                btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                tv_email.setText(cu.getString(cu.getColumnIndex("SENDRECIPTS")).toString() + " :")

//                tv_email.setText("Send Receipt to :")
                popupemail.setCancelable(false)
                popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                popupemail.show()
                btn_cancel.setOnClickListener(View.OnClickListener {
                    popupemail.dismiss()
                    startActivity(Intent(activity, Home::class.java))
                })
                btn_new_sale.setOnClickListener(View.OnClickListener {
                    popupemail.dismiss()
                    startActivity(Intent(activity, Home::class.java))
                })

//                Toast.makeText(activity, responce.print, Toast.LENGTH_SHORT).show()
                Log.e("helphelp", responce.print)
                btn_print.setOnClickListener(View.OnClickListener {
                    popupemail.dismiss()


                    val next_pg = Intent(activity, Print::class.java)
                    next_pg.putExtra("PRINT_ITEMS", responce.print)
                    next_pg.putExtra("PRINT_ITEMS_REFUND", "")
                    next_pg.putExtra("PRINT_DATE", print_date)
                    next_pg.putExtra("PRINT_DISCOUNT", print_discount)
                    next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", print_discount_amount)
                    next_pg.putExtra("PRINT_TOTAL", amount.toString())
                    next_pg.putExtra("PRINT_RECEIPT_NO", responce.orderId)
                    next_pg.putExtra("TAX", responce.tax)
                    next_pg.putExtra("REFUNDED_DISCOUNT", "")
                    startActivity(next_pg)

                })

                btn_send.setOnClickListener(View.OnClickListener {
                    progress.show()
                    if (isValidEmail(et_email.text.toString())) {
                        RequestReceiptEmail(activity, ResponceprocessorReceiptsEmal()).getReceiptEmail(et_email.text.toString(), order_id.toString(),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
                        popupemail.dismiss()

                    } else {
                        message.setText(cu.getString(cu.getColumnIndex("INVALIDEMAIL")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                    }
                })

            }
//            cu.close()
        }

        inner class ResponceprocessorReceiptsEmal : ProcessResponcceInterphase<ResponceActivateStatus> {
            override fun processResponce(responce: ResponceActivateStatus) {
                if (responce != null) {
                    progress.dismiss()
                    startActivity(Intent(activity, PayLaterList::class.java))
                }
            }

        }

    }

    /*inner class ResponceprocessorReceiptsEml : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce.status.equals("success")) {
                progress.dismiss()
                startActivity(Intent(activity, Home::class.java))
            }
        }

    }*/

    fun isValidEmail(e_mail: String): Boolean {
        return e_mail.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}".toRegex()) && e_mail.length <= 320
    }

    /*fun print_no_network() {
        val cu = database!!.getLanguage()
        cu.moveToFirst()
        val popupemail = Dialog(activity)
        popupemail.setContentView(R.layout.popup_email_receipt)
        btn_cancel = popupemail.findViewById(R.id.btn_cancel)
        btn_send = popupemail.findViewById(R.id.btn_send)
        et_email = popupemail.findViewById(R.id.et_email)
        tv_email = popupemail.findViewById(R.id.tv_email)
        viewz = popupemail.findViewById(R.id.viewz)
        btn_new_sale = popupemail.findViewById(R.id.btn_new_sale)
        btn_print = popupemail.findViewById(R.id.btn_print)
        btn_new_sale.visibility = View.VISIBLE
        btn_print.visibility = View.VISIBLE
        viewz.visibility = View.VISIBLE
        btn_send.visibility = View.INVISIBLE


        btn_print.setText(cu.getString(cu.getColumnIndex("PRINT")).toString())
        btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
        btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
        tv_email.setText(cu.getString(cu.getColumnIndex("SENDRECIPTS")).toString() + " :")

//        tv_email.setText("Send Receipt to :")
        popupemail.setCancelable(false)
        popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
        popupemail.show()
        btn_cancel.setOnClickListener(View.OnClickListener {
            popupemail.dismiss()
            startActivity(Intent(activity, Home::class.java))
        })
        btn_new_sale.setOnClickListener(View.OnClickListener {
            popupemail.dismiss()
            startActivity(Intent(activity, Home::class.java))
        })

        btn_print.setOnClickListener(View.OnClickListener {
            popupemail.dismiss()


            val next_pg = Intent(activity, Print::class.java)
            next_pg.putExtra("PRINT_ITEMS", print_items)
            next_pg.putExtra("PRINT_ITEMS_REFUND", "")
            next_pg.putExtra("PRINT_DATE", print_date)
            next_pg.putExtra("PRINT_DISCOUNT", print_discount)
            next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", print_discount_amount)
            next_pg.putExtra("PRINT_TOTAL", print_total)
            next_pg.putExtra("PRINT_RECEIPT_NO", "")
            next_pg.putExtra("TAX", "")
            startActivity(next_pg)

        })
    }*/

    override fun onDestroy() {
        super.onDestroy()
        try {
            progress.dismiss()
        } catch (e: Exception) {
        }
    }
}


