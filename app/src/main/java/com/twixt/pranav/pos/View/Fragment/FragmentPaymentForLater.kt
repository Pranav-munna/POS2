package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.twixt.pranav.pos.View.Activity.*
import com.twixt.pranav.pos.View.Adapter.RvAdapterDiscountForPayLater
import com.twixt.pranav.pos.View.Adapter.RvAdapterPayment
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Pranav on 11/27/2017.
 */
//private var database: SQLiteHelper? = null
//var total_amount_formatedd: TextView? = null

class FragmentPaymentForLater : Fragment() {
    var tax_display = 0.0

    //    var wifi = false
    private lateinit var databasePaymentLater: SQLiteHelper
    var network = false

    val handler = Handler()
    lateinit var total_amount_formatedd: TextView
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
    lateinit var charge: Button
    lateinit var btn_send: Button
    lateinit var btn_cancel: Button
    lateinit var bn_pay_later: Button
    lateinit var et_email: AppCompatEditText
    lateinit var tv_email: AppCompatTextView
    lateinit var viewz: View
    lateinit var btn_new_sale: Button
    lateinit var btn_print: Button
    lateinit var progress: ProgressDialog
    lateinit var message: TextView
    lateinit var ok: Button

    var s: String = ""
    var total = 0.0
    var t_total = ""
    var order_id = ""

    var print_items = "Testitem 1 ## 1х 10.00 ## 10.00 !& Testitem 2 ## 2 х 10.00 ## 20.00"
    var print_discount = ""
    var print_discount_amount = ""
    var print_total = ""
    var print_receiptNo = ""

    var print_date = ""
    var formatter = DecimalFormat("#,###,##0.00")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_payment, container, false)

        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")

        progress = ProgressDialog(activity)
        payment_rv = mView.findViewById(R.id.payment_rv)
        discount_rv = mView.findViewById(R.id.discount_rv)
        back = mView.findViewById(R.id.back)
        split = mView.findViewById(R.id.split)
        total_amount = mView.findViewById(R.id.total_amount)
        charge = mView.findViewById(R.id.charge)
        title = mView.findViewById(R.id.title)
        i_o_t = mView.findViewById(R.id.i_o_t)
        discount_applayed = mView.findViewById(R.id.discount_applayed)
        t_amount = mView.findViewById(R.id.t_amount)
        offer_applied_title = mView.findViewById(R.id.offer_applied_title)
        payment_type_title = mView.findViewById(R.id.payment_type_title)
        total_amount_formatedd = mView.findViewById(R.id.total_amount_formated)
        bn_pay_later = mView.findViewById(R.id.bn_pay_later)
        bn_pay_later.visibility = View.GONE
        split.visibility = View.GONE

        var c = Calendar.getInstance().time
        var df = SimpleDateFormat("dd-MMM-yyyy")
        print_date = df.format(c).toString()




        t_total = arguments.getString("TOTAL")
        order_id = arguments.getString("ORDER_ID")
        tax_display = arguments.getString("TAX").toDouble()
        print_receiptNo = order_id
//        total_amount.setText(t_total + "jk")
//        Toast.makeText(activity, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(), Toast.LENGTH_SHORT).show()
//        Toast.makeText(activity, t_total, Toast.LENGTH_SHORT).show()
        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))



        databasePaymentLater = SQLiteHelper(activity)
        val cu = databasePaymentLater.getLanguage()
        cu.moveToFirst()

        ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
        progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
        progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
        progress.setCancelable(false)

        title.setText(cu.getString(cu.getColumnIndex("PAYMENT")).toString())
        t_amount.setText(cu.getString(cu.getColumnIndex("TOTALAMOUNT")).toString())
        i_o_t.setText(cu.getString(cu.getColumnIndex("INCLUSIVEOFTAXES")).toString() + "(" + formatter.format(tax_display )+ ")")
        split.setText(cu.getString(cu.getColumnIndex("SPLIT")).toString())
        charge.setText(cu.getString(cu.getColumnIndex("CHARGE")).toString())
        offer_applied_title.setText(cu.getString(cu.getColumnIndex("OFFERS_APPLIED")).toString())
        payment_type_title.setText(cu.getString(cu.getColumnIndex("PAYMENT_TYPE")).toString())


        val rvAdapterPayment = RvAdapterPayment(activity)
        payment_rv.adapter = rvAdapterPayment
        rvAdapterPayment.set(total_amount_formatedd, databasePaymentLater)
        payment_rv.getRecycledViewPool().setMaxRecycledViews(0, 0)
        payment_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val rvAdapterDiscount = RvAdapterDiscountForPayLater(activity, FragmentPaymentForLater())
        discount_rv.adapter = rvAdapterDiscount
        discount_rv.getRecycledViewPool().setMaxRecycledViews(0, 0)
        rvAdapterDiscount.set(discount_applayed, total_amount, total_amount_formatedd, databasePaymentLater, tax_display, i_o_t)
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

//        total_amount.setText((Math.round(t_total.toDouble() * 100.0) / 100.0).toString())
        total_amount.setText(String.format("%.2f", t_total.toDouble()))
        total_amount_formatedd.setText(formatter.format(t_total.toDouble()))



        charge.setOnClickListener(View.OnClickListener {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0").equals("0")) {
                message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString(), Toast.LENGTH_SHORT).show()
            } else {


                val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
                var discount_no = ""
                var discount_amount = ""
                if (discount_applayed.text.trim().length > 0) {
                    val cu2 = databasePaymentLater.getLanguage()
                    cu2.moveToFirst()

                    val cu = databasePaymentLater.discountTypesString(discount_applayed.text.toString().replace(cu2.getString(cu2.getColumnIndex("APPLIED")) + ": ", ""))
                    cu.moveToFirst()
                    discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                    if (discount_no == "0")
                        discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                    else {
                        discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
                    }
                    cu2.close()
//                    cu.close()
                }

                /*if (isConnected) {
                    RequestcheckingNetwork(activity, ResponceprocerWifi()).getStatus()
                }*/

                if (isConnected /*&& wifi*/) {

                    if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CASH, "0").equals("Cash")) {
                        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).CASH, "0")

                        val cu1 = databasePaymentLater.getLanguage()
                        cu1.moveToFirst()

                        var discount_no = ""
                        var discount_amount = ""
                        if (discount_applayed.text.trim().length > 0) {
                            val cu = databasePaymentLater.discountTypesString(discount_applayed.text.toString().replace(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": ", ""))
                            cu.moveToFirst()
                            discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                            if (discount_no == "0")
                                discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                            else {
                                discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
                            }
                            cu.close()
                        }

                        val next_pg = Intent(activity, CalculationPayLater::class.java)
                        next_pg.putExtra("TOTAL_AMOUNT", total_amount.text.toString().replace(",", "."))
                        next_pg.putExtra("ORDERID", order_id)
                        next_pg.putExtra("DISCOUNT_NO", discount_no)
                        next_pg.putExtra("DISCOUNT_AMOUNT", discount_amount)
                        next_pg.putExtra("TOTAL", t_total.toString())


                        next_pg.putExtra("PRINT_ITEMS", print_items)
                        next_pg.putExtra("PRINT_ITEMS_REFUND", "")
                        next_pg.putExtra("PRINT_DATE", print_date)
                        next_pg.putExtra("PRINT_DISCOUNT", print_discount)
                        next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", discount_amount)
                        next_pg.putExtra("PRINT_TOTAL", SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim())

//            Toast.makeText(activity, discount_amount, Toast.LENGTH_SHORT).show()

                        startActivity(next_pg)
                        activity.finish()

                        cu1.close()
                    } else {


                        progress.show()

                        print_discount = discount_no
                        print_discount_amount = discount_amount
                        RequestForPayLater(activity, ResponceProcessorForPayLater()).isSuccess(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "1"),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"),
                                t_total, discount_amount, discount_amount,
                                total_amount.text.toString().replace(",", ".").trim(),
                                discount_no, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"), order_id)

//                    Toast.makeText(activity,total_amount.text.toString().trim(),Toast.LENGTH_SHORT).show()
//                    wifi = false


                        handler.postDelayed({

                            if (!network) {
                                progress.dismiss()

                                message.setText(cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString())
                                popup.show()
                                ok.setOnClickListener(View.OnClickListener {
                                    popup.dismiss()
                                })

                                network = false
                            }

                        }, 5000)

                    }


                }


            }

        })

        split.setOnClickListener(View.OnClickListener {
            var discount_no = ""
            var discount_amount = ""
            if (discount_applayed.text.trim().length > 0) {
                val cu = databasePaymentLater.discountTypesString(discount_applayed.text.toString().replace(" Applied: ", ""))
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

        back.setOnClickListener(View.OnClickListener { activity.finish() })
//        cu.close()
        return mView
    }

    /*inner class ResponceprocerWifi : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
//                wifi = true
            }
        }

    }
*/

    inner class ResponceProcessorForPayLater : ProcessResponcceInterphase<ResponcePayLater> {
        override fun processResponce(responce: ResponcePayLater) {
            val cu = databasePaymentLater.getLanguage()
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
                    next_pg.putExtra("PRINT_TOTAL", total_amount.text.toString())
                    next_pg.putExtra("PRINT_RECEIPT_NO", responce.orderId)
                    next_pg.putExtra("TAX", responce.tax)
                    next_pg.putExtra("TAREFUNDED_DISCOUNTX", "")
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

    fun isValidEmail(e_mail: String): Boolean {
        return e_mail.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}".toRegex()) && e_mail.length <= 320
    }

    fun set(cu: Cursor, context: Context, discount_aplayed: TextView, ttl_amount: TextView, i: Int, total_amount_formated: TextView, display_tax: Double, iOT: TextView) {

        var tax_display_ = display_tax
        //region set
        databasePaymentLater = SQLiteHelper(context)
        val cu1 = databasePaymentLater.language
        cu1.moveToFirst()

        var percen = tax_display_ * 100 / ttl_amount.text.toString().replace(",", ".").toDouble()

        if (i == 1) {
            discount_aplayed.setText(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": " + cu.getString(cu.getColumnIndex("LABEL")))
            /*Toast.makeText(context, cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")) + " "
                    + cu.getString(cu.getColumnIndex("DISCOUNT")), Toast.LENGTH_SHORT).show()*/
            if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                var amount = ttl_amount.text.toString().replace(",", ".").toDouble() * cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble() / 100
                amount = ttl_amount.text.toString().replace(",", ".").toDouble() - amount
                ttl_amount.setText(String.format("%.2f", amount.toDouble()))
                total_amount_formated.setText(formatter.format(amount.toDouble()))
            } else {
                var amount = ttl_amount.text.toString().replace(",", ".").toDouble() - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble()
                ttl_amount.setText(String.format("%.2f", amount.toDouble()))
                total_amount_formated.setText(formatter.format(amount.toDouble()))
            }

            iOT.setText(cu1.getString(cu1.getColumnIndex("INCLUSIVEOFTAXES")).toString() + "(" + formatter.format(ttl_amount.text.toString().replace(",", ".").toDouble() * percen / 100) + ")")

//            discount_aplayed.setText(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": " + cu.getString(cu.getColumnIndex("LABEL")) + "(" + ttl_amount.text.toString().replace(",", ".").toDouble() * percen / 100 + ")")


            SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).TOTAL_AMOUNT, ttl_amount.text.toString())
        } else if (i == 0) {
            discount_aplayed.setText("")
            iOT.setText(cu1.getString(cu1.getColumnIndex("INCLUSIVEOFTAXES")).toString() + "(" + formatter.format(display_tax) + ")")
            if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                var discount_amounts = ((ttl_amount.text.toString().replace(",", ".").toDouble() * 100 /
                        (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) -
                        ttl_amount.text.toString().replace(",", ".").toDouble()) + ttl_amount.text.toString().replace(",", ".").toDouble()
                ttl_amount.setText(String.format("%.2f", discount_amounts.toDouble()))
                total_amount_formated.setText(formatter.format(discount_amounts.toDouble()))
            } else {
                ttl_amount.setText(String.format("%.2f", ttl_amount.text.toString().replace(",", ".").toDouble() + cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble()))
                total_amount_formated.setText(formatter.format(ttl_amount.text.toString().replace(",", ".").toDouble()))
            }

        }

        //endregion
        cu1.close()
        cu.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            progress.dismiss()
        } catch (e: Exception) {
        }
    }
}