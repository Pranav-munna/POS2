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
import android.webkit.WebView
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
import com.twixt.pranav.pos.View.Activity.*
import com.twixt.pranav.pos.View.Adapter.RvAdapterDiscount
import com.twixt.pranav.pos.View.Adapter.RvAdapterPayment
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Pranav on 11/27/2017.
 */
//private var database: SQLiteHelper? = null
//var total_amount_formated: TextView? = null

var adapterPos = -1
var tax_display = 0.0
class FragmentPayment : Fragment() {

    private lateinit var databaseForPayments: SQLiteHelper
    lateinit var total_amount_formated: TextView
    var network = false

    val handler = Handler()
    private var mWebView: WebView? = null

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

    var print_items = ""
    var print_discount = ""
    var print_discount_amount = ""
    var print_total = ""
    var print_receiptNo = ""

    var print_date = ""
    var s: String = ""
    var total = 0.0

    var formatter = DecimalFormat("#,###,##0.00")


    //    var singleItem = 0.0
    var tax_cal = 0.0
    var tax_percent = 0.0
    //    var tax_discount = 0.0

    fun displayDiscount(price: String, tax: String, quantity: String) {

//        singleItem = (price.toDouble() * discount_cal) / 100
        tax_cal = tax.toDouble() / quantity.toDouble()
        tax_percent = (tax_cal * 100) / price.toDouble()
//        tax_discount = singleItem * (tax_percent / 100)
        tax_display = tax_display + (tax_cal /*- tax_discount*/)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_payment, container, false)

        tax_display = 0.0
//        RequestcheckingNetwork(activity, ResponceprocesssorWifi()).getStatus()

        progress = ProgressDialog(activity)
        payment_rv = mView.findViewById(R.id.payment_rv)
        discount_rv = mView.findViewById(R.id.discount_rv)
        back = mView.findViewById(R.id.back)
        split = mView.findViewById(R.id.split)
        total_amount = mView.findViewById(R.id.total_amount)
        total_amount_formated = mView.findViewById(R.id.total_amount_formated)
        charge = mView.findViewById(R.id.charge)
        title = mView.findViewById(R.id.title)
        i_o_t = mView.findViewById(R.id.i_o_t)
        discount_applayed = mView.findViewById(R.id.discount_applayed)
        t_amount = mView.findViewById(R.id.t_amount)
        offer_applied_title = mView.findViewById(R.id.offer_applied_title)
        payment_type_title = mView.findViewById(R.id.payment_type_title)
        bn_pay_later = mView.findViewById(R.id.bn_pay_later)

        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")


        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))


        var c = Calendar.getInstance().time
        var df = SimpleDateFormat("dd-MMM-yyyy")

        print_date = df.format(c).toString()

        databaseForPayments = SQLiteHelper(activity)
        val cu = databaseForPayments.language
        cu.moveToFirst()

        progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
        progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
        progress.setCancelable(false)

        ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
        title.setText(cu.getString(cu.getColumnIndex("PAYMENT")).toString())
        bn_pay_later.setText(cu.getString(cu.getColumnIndex("PAYLATER")).toString())
        t_amount.setText(cu.getString(cu.getColumnIndex("TOTALAMOUNT")).toString())
        split.setText(cu.getString(cu.getColumnIndex("SPLIT")).toString())
        charge.setText(cu.getString(cu.getColumnIndex("CHARGE")).toString())
        offer_applied_title.setText(cu.getString(cu.getColumnIndex("OFFERS_APPLIED")).toString())
        payment_type_title.setText(cu.getString(cu.getColumnIndex("PAYMENT_TYPE")).toString())


        val rvAdapterPayment = RvAdapterPayment(activity)
        payment_rv.adapter = rvAdapterPayment
        rvAdapterPayment.set(total_amount_formated, databaseForPayments)
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

        if (databaseForPayments != null && (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) != 0)) {

            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                val cu = databaseForPayments.cartdatas(i)
                cu.moveToFirst()


                val cu_item = databaseForPayments.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                cu_item.moveToFirst()
                var t_amount = 0.0
                var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                var j = 0
                while (j < (mainstring.size - 1)) {
                    var sub_strig = mainstring[j].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    t_amount = t_amount + sub_strig[1].toDouble()
                    j++
                }
                total = total + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())


                if (i == 0/*(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)*/) {
                    if (!cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {


                        val cu_item = databaseForPayments.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                        cu_item.moveToFirst()
                        var modifier_string = ""
                        var modifier_amount = 0.0
                        var modifier_print = " "
                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        var j = 0
                        while (j < (mainstring.size - 1)) {
                            var sub_strig = mainstring[j].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                            if (j == (mainstring.size - 2)) {
                                modifier_print = modifier_print + sub_strig[0]
                                modifier_amount = modifier_amount + sub_strig[1].toDouble()
                                modifier_string = modifier_string + """{"modifier_id":"""" + sub_strig[2] +
                                        """","modifier_name":"""" + sub_strig[0] +
                                        """","amount":"""" + sub_strig[1] + """"}"""
                            } else {
                                modifier_amount = modifier_amount + sub_strig[1].toDouble()
                                modifier_print = modifier_print + sub_strig[0] + ","
                                modifier_string = modifier_string + """{"modifier_id":"""" + sub_strig[2] +
                                        """","modifier_name":"""" + sub_strig[0] +
                                        """","amount":"""" + sub_strig[1] + """"},"""
                            }


                            j++
                        }

                        Log.e("helphelp", (cu.getString(cu.getColumnIndex("TAX"))/*.toDouble() * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble()).toString(*/))

                        s = s + """{"itemId":"""" + cu.getString(cu.getColumnIndex("ITEM_ID")) +
                                """","quantity":"""" + cu.getString(cu.getColumnIndex("QUANTITY")) +
                                """","itemName":"""" + cu.getString(cu.getColumnIndex("ITEM_NAME")) +
                                """","price":"""" + (cu.getString(cu.getColumnIndex("PRICE")).toDouble() + modifier_amount).toString() +
                                """","modifiers":""" + "[" + modifier_string + "]" + ""","tax":"""" +
                                (cu.getString(cu.getColumnIndex("TAX")).toDouble() * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble()).toString() + """"}"""

                        displayDiscount(cu.getString(cu.getColumnIndex("PRICE")).toString(), cu.getString(cu.getColumnIndex("TAX")).toString(), cu.getString(cu.getColumnIndex("QUANTITY")).toString())

                        s = s.replace("[]", "null")


                        print_items = print_items + cu.getString(cu.getColumnIndex("ITEM_NAME")) + " ## " +
                                cu.getString(cu.getColumnIndex("QUANTITY")) + " X " + formatter.format(cu.getString(cu.getColumnIndex("PRICE")).replace(",", "").toDouble()) + " ## " +
                                String.format("%.2f", cu.getString(cu.getColumnIndex("QUANTITY")).toDouble() * cu.getString(cu.getColumnIndex("PRICE")).toDouble()) +
                                "##" + modifier_print
                        cu_item.close()
                    }
                } else {
                    if (!cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                        try {
                            val cu_item = databaseForPayments.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                            cu_item.moveToFirst()
                            var modifier_string = ""
                            var modifier_print = " "
                            var modifier_amount = 0.0
                            var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                            var j = 0
                            while (j < (mainstring.size - 1)) {
                                var sub_strig = mainstring[j].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                if (j == (mainstring.size - 2)) {
                                    modifier_print = modifier_print + sub_strig[0]
                                    modifier_amount = modifier_amount + sub_strig[1].toDouble()
                                    modifier_string = modifier_string + """{"modifier_id":"""" + sub_strig[2] +
                                            """","modifier_name":"""" + sub_strig[0] +
                                            """","amount":"""" + sub_strig[1] + """"}"""
                                } else {
                                    modifier_amount = modifier_amount + sub_strig[1].toDouble()
                                    modifier_print = modifier_print + sub_strig[0] + ","
                                    modifier_string = modifier_string + """{"modifier_id":"""" + sub_strig[2] +
                                            """","modifier_name":"""" + sub_strig[0] +
                                            """","amount":"""" + sub_strig[1] + """"},"""
                                }


                                j++
                            }
                            s = s + """,{"itemId":"""" + cu.getString(cu.getColumnIndex("ITEM_ID")) +
                                    """","quantity":"""" + cu.getString(cu.getColumnIndex("QUANTITY")) +
                                    """","itemName":"""" + cu.getString(cu.getColumnIndex("ITEM_NAME")) +
                                    """","price":"""" + (cu.getString(cu.getColumnIndex("PRICE")).toDouble() + modifier_amount).toString() +
                                    """","modifiers":""" + "[" + modifier_string + "]" + ""","tax":"""" +
                                    (cu.getString(cu.getColumnIndex("TAX")).toDouble() * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble()).toString() + """"}"""

                            displayDiscount(cu.getString(cu.getColumnIndex("PRICE")).toString(), cu.getString(cu.getColumnIndex("TAX")).toString(), cu.getString(cu.getColumnIndex("QUANTITY")).toString())

                            s = s.replace("[]", "null")

                            print_items = print_items + " !& " + cu.getString(cu.getColumnIndex("ITEM_NAME")) + " ## " +
                                    cu.getString(cu.getColumnIndex("QUANTITY")) + " X " + formatter.format(cu.getString(cu.getColumnIndex("PRICE")).replace(",", ".").toDouble()) + " ## " +
                                    String.format("%.2f", cu.getString(cu.getColumnIndex("QUANTITY")).toDouble() * cu.getString(cu.getColumnIndex("PRICE")).toDouble()) +
                                    "##" + modifier_print
                            cu_item.close()
                        } catch (e: Exception) {
                            /*activity.finish();
                            startActivity(activity.getIntent())*/
                        }


                    }
                }
                cu.close()
                cu_item.close()
            }

            i_o_t.setText(cu.getString(cu.getColumnIndex("INCLUSIVEOFTAXES")).toString() + "(" + formatter.format(tax_display) + ")")

            try {
                if (s.substring(0, 1).equals(",")) {
                    s = s.substring(1, s.length)
                }
            } catch (e: Exception) {
            }

        }


        total_amount.setText(String.format("%.2f", total))
        total_amount_formated.setText(formatter.format(total))

        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, total_amount.text.toString().replace(",", "."))


        charge.setOnClickListener(View.OnClickListener {
            charge.isClickable = false
            //            Toast.makeText(activity, s, Toast.LENGTH_LONG).toString()
            Log.e("helphelp", s)
            Log.e("print r1 help", print_items)
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0").equals("0")) {
                charge.isClickable = true
                message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString(), Toast.LENGTH_SHORT).show()
            } else
                if (databaseForPayments != null && (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) != 0) && (!total_amount.text.toString().replace(",", ".").equals("0.00"))) {

                    val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = cm.activeNetworkInfo
                    val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
                    var discount_no = ""
                    var discount_amount = ""
                    if (discount_applayed.text.trim().length > 0) {
                        val cu2 = databaseForPayments.getLanguage()
                        cu2.moveToFirst()

                        val cu = databaseForPayments.discountTypesString(discount_applayed.text.toString().replace(cu2.getString(cu2.getColumnIndex("APPLIED")) + ": ", ""))
                        cu.moveToFirst()
                        discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                        if (discount_no == "0")
                            discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                        else {
                            discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
//                            Toast.makeText(activity, discount_amount, Toast.LENGTH_SHORT).show()
                        }
//                        Toast.makeText(activity, cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")) + "\n" + cu.getString(cu.getColumnIndex("DISCOUNT")), Toast.LENGTH_SHORT).show()
                        cu2.close()
                        cu.close()
                    }

//                    RequestcheckingNetwork(activity, ResponceprocesssorWifi()).getStatus()

                    if (isConnected /*&& wifi*/) {

//                        Toast.makeText(activity, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CASH, "0"), Toast.LENGTH_SHORT).show()

                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CASH, "0").equals("Cash")) {
                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).CASH, "0")

                            val cu1 = databaseForPayments.language
                            cu1.moveToFirst()

                            var discount_no = ""
                            var discount_amount = ""
                            if (discount_applayed.text.trim().length > 0) {
                                val cu = databaseForPayments.discountTypesString(discount_applayed.text.toString().replace(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": ", ""))
                                cu.moveToFirst()
                                discount_no = cu.getString(cu.getColumnIndex("DISCOUNT_TYPE"))
                                if (discount_no == "0")
                                    discount_amount = cu.getString(cu.getColumnIndex("DISCOUNT"))
                                else {
                                    discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
                                }
                                cu.close()
                            }

                            val next_pg = Intent(activity, Calculation::class.java)
                            next_pg.putExtra("TOTAL_AMOUNT", total_amount.text.toString().replace(",", "."))
                            next_pg.putExtra("ITEMS", s)
                            next_pg.putExtra("DISCOUNT_NO", discount_no)
                            next_pg.putExtra("DISCOUNT_AMOUNT", discount_amount)
                            next_pg.putExtra("TOTAL", total.toString())


                            next_pg.putExtra("PRINT_ITEMS", print_items)
                            next_pg.putExtra("PRINT_ITEMS_REFUND", "")
                            next_pg.putExtra("PRINT_DATE", print_date)
                            next_pg.putExtra("PRINT_DISCOUNT", print_discount)
                            next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", discount_amount)
                            next_pg.putExtra("PRINT_TOTAL", SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim())

//            Toast.makeText(activity, discount_amount, Toast.LENGTH_SHORT).show()
                            Log.e("print help", print_items)
                            startActivity(next_pg)
                            activity.finish()
                            cu1.close()
                        } else {

                            //region PAY
                            progress.show()

                            print_discount = discount_no
                            print_discount_amount = discount_amount
                            print_total = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim()



                            RequestPay(activity, ResponceprocessorPay()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                    ((total * 100.0) / 100.0).toString(),
                                    discount_amount,
                                    discount_amount,
                                    total_amount.text.toString().replace(",", "."),
                                    discount_no,
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                    "[" + s + "]")
                            Log.e("taxtax", s)

                            handler.postDelayed({

                                if (!network) {
                                    progress.dismiss()


                                    if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                                        startActivity(Intent(activity, ErrorPage::class.java))
                                    } else {
                                        print_discount = discount_no
                                        print_discount_amount = discount_amount
                                        print_total = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim()
                                        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_PAY, "1")
                                        databaseForPayments.insertPay(((total * 100.0) / 100.0).toString(),
                                                discount_amount,
                                                discount_amount,
                                                total_amount.text.toString().replace(",", "."),
                                                discount_no,
                                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                                "[" + s + "]")

                                        if (databaseForPayments.deleteCart()) {
                                            SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                                Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                                        }
                                        print_no_network()
//                            startActivity(Intent(activity, Home::class.java))
                                    }

                                    /*message.setText(cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString())
                                    popup.show()
                                    ok.setOnClickListener(View.OnClickListener {
                                        popup.dismiss()
                                    })*/

                                    network = false
                                }

                            }, 5000)


                            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_PAY, "0").equals("1")) {
                                var i = 0
                                while (i < databaseForPayments.pendingPaySize.count) {
                                    val cu = databaseForPayments.getPendingPay(i)
                                    cu.moveToFirst()
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
                                databaseForPayments.deletePay()
                            }

                            //endregion

                        }

//                        wifi = false
                    } else {

                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                            startActivity(Intent(activity, ErrorPage::class.java))
                        } else {

                            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CASH, "0").equals("Cash")) {
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).CASH, "0")

                                val next_pg = Intent(activity, Calculation::class.java)
                                next_pg.putExtra("TOTAL_AMOUNT", total_amount.text.toString().replace(",", "."))
                                next_pg.putExtra("ITEMS", s)
                                next_pg.putExtra("DISCOUNT_NO", discount_no)
                                next_pg.putExtra("DISCOUNT_AMOUNT", discount_amount)
                                next_pg.putExtra("TOTAL", total.toString())


                                next_pg.putExtra("PRINT_ITEMS", print_items)
                                next_pg.putExtra("PRINT_ITEMS_REFUND", "")
                                next_pg.putExtra("PRINT_DATE", print_date)
                                next_pg.putExtra("PRINT_DISCOUNT", print_discount)
                                next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", discount_amount)
                                next_pg.putExtra("PRINT_TOTAL", SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim())

//            Toast.makeText(activity, discount_amount, Toast.LENGTH_SHORT).show()

                                startActivity(next_pg)
                                activity.finish()

                            } else {
                                print_discount = discount_no
                                print_discount_amount = discount_amount
                                print_total = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim()
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_PAY, "1")
                                databaseForPayments.insertPay(((total * 100.0) / 100.0).toString(),
                                        discount_amount,
                                        discount_amount,
                                        total_amount.text.toString().replace(",", "."),
                                        discount_no,
                                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                        "[" + s + "]")

                                if (databaseForPayments.deleteCart()) {
                                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                                Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                                }
                                print_no_network()
//                            startActivity(Intent(activity, Home::class.java))

                            }

                        }


                    }


                } else {
                    val cuLan = databaseForPayments.language
                    cuLan.moveToFirst()
                    charge.isClickable = true
                    message.setText(cuLan.getString(cuLan.getColumnIndex("NO_ITEMS")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                    Toast.makeText(activity, cuLan.getString(cuLan.getColumnIndex("NO_ITEMS")).toString(), Toast.LENGTH_SHORT).show()
                    cuLan.close()
                }
        })

        split.setOnClickListener(View.OnClickListener {

            val cu1 = databaseForPayments.language
            cu1.moveToFirst()

            var discount_no = ""
            var discount_amount = ""
            if (discount_applayed.text.trim().length > 0) {
                val cu = databaseForPayments.discountTypesString(discount_applayed.text.toString().replace(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": ", ""))
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
            next_pg.putExtra("TOTAL_AMOUNT", /*(Math.round(total * 100.0) / 100.0).toString()*/total_amount.text.toString().replace(",", "."))
            next_pg.putExtra("ITEMS", s)
            next_pg.putExtra("DISCOUNT_NO", discount_no)
            next_pg.putExtra("DISCOUNT_AMOUNT", discount_amount)


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
        })

        bn_pay_later.setOnClickListener(View.OnClickListener {
            val cu = databaseForPayments.language
            cu.moveToFirst()

            progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
            progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
            progress.setCancelable(false) // disable dismiss by tapping outside of the dialog


            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")


            if (databaseForPayments != null && (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) != 0)) {

                val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
                var discount_no = ""
                var discount_amount = ""
                if (discount_applayed.text.trim().length > 0) {
                    val cu2 = databaseForPayments.getLanguage()
                    cu2.moveToFirst()

                    val cupay = databaseForPayments.discountTypesString(discount_applayed.text.toString().replace(cu2.getString(cu2.getColumnIndex("APPLIED")) + ": ", ""))
                    cupay.moveToFirst()
                    discount_no = cupay.getString(cupay.getColumnIndex("DISCOUNT_TYPE"))
                    if (discount_no == "0")
                        discount_amount = cupay.getString(cupay.getColumnIndex("DISCOUNT"))
                    else {
                        discount_amount = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 / (100 - cupay.getString(cupay.getColumnIndex("DISCOUNT")).toDouble())) - total_amount.text.toString().replace(",", ".").toDouble()).toString()
                    }
                    cu2.close()
//                    cupay.close()
                }

//                RequestcheckingNetwork(activity, ResponceprocesssorWifi()).getStatus()


                if (isConnected /*&& wifi*/) {
                    val cu = databaseForPayments.getLanguage()
                    cu.moveToFirst()
                    val popupemail = Dialog(activity)
                    popupemail.setContentView(R.layout.popup_email_receipt)

                    btn_cancel = popupemail.findViewById(R.id.btn_cancel)
                    btn_send = popupemail.findViewById(R.id.btn_send)
                    et_email = popupemail.findViewById(R.id.et_email)
                    tv_email = popupemail.findViewById(R.id.tv_email)
//                    tv_email.setText("Pay Later Id:")
                    btn_cancel.visibility = View.INVISIBLE
                    popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))


                    btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                    btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                    tv_email.setText(cu.getString(cu.getColumnIndex("PAYLATERID")).toString() + " :")

                    popupemail.show()
                    btn_send.setOnClickListener(View.OnClickListener {
                        if (et_email.text.toString().trim().length > 0) {


                            if (adapterPos != -1) {
                                val cu = databaseForPayments.discountTypes(adapterPos)
                                cu.moveToFirst()
                                if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                                    var discount_amounts = ((total_amount.text.toString().replace(",", ".").toDouble() * 100 /
                                            (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) -
                                            total_amount.text.toString().replace(",", ".").toDouble()) + total_amount.text.toString().replace(",", ".").toDouble()
                                    total_amount.setText(String.format("%.2f", discount_amounts.toDouble()))
                                    total_amount_formated.setText(formatter.format(discount_amounts.toDouble()))
                                } else {
                                    total_amount.setText(String.format("%.2f", (total_amount.text.toString().replace(",", ".").toDouble() + cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())))
                                    total_amount_formated.setText(formatter.format(total_amount.text.toString().replace(",", ".").toDouble()))
                                }
                                adapterPos = -1
                                cu.close()
                            }


                            RequestPayLater(activity, ResponceprocessorPayLater()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                    total_amount.text.toString().replace(",", "."),
                                    "0",
                                    "0",
                                    total_amount.text.toString().replace(",", "."),
                                    "0",
                                    "0",
                                    "[" + s + "]", et_email.text.toString(), print_items)
                            progress.show()
//                        Log.e("helphelp", s.substring(0, 1))
                            popupemail.dismiss()

                            /*} else {
                                message.setText(cu.getString(cu.getColumnIndex("INVALID_EMAIL")).toString())
                                popup.show()
                                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                            }*/

                            handler.postDelayed({

                                if (!network) {
                                    progress.dismiss()

                                    if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                                        startActivity(Intent(activity, ErrorPage::class.java))
                                    } else {
//                            if (isValidEmail(et_email.text.toString())) {
                                        //region pay later offline
                                        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAY_LATER, "1")
                                        databaseForPayments.insertPayLater(total_amount.text.toString().replace(",", "."),
                                                discount_amount,
                                                discount_amount,
                                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                                discount_no,
                                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                                "[" + s + "]", et_email.text.toString(), print_items)

                                        if (databaseForPayments.deleteCart()) {
                                            SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
                                        }

                                        startActivity(Intent(activity, Home::class.java))

                                        //endregion
                                        popupemail.dismiss()

                                        /* } else {
                                             message.setText(cu.getString(cu.getColumnIndex("INVALID_EMAIL")).toString())
                                             popup.show()
                                             ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                                         }*/
                                    }

                                    network = false
                                }

                            }, 5000)
                        } else {
                            Toast.makeText(activity, cu.getString(cu.getColumnIndex("ENTER_ALL_FIELDS")).toString(), Toast.LENGTH_SHORT).show()
                        }


                    })
//                    wifi = false
//                    cu.close()

                } else {
                    val cu = databaseForPayments.getLanguage()
                    cu.moveToFirst()
                    val popupemail = Dialog(activity)
                    popupemail.setContentView(R.layout.popup_email_receipt)
                    btn_cancel = popupemail.findViewById(R.id.btn_cancel)
                    btn_send = popupemail.findViewById(R.id.btn_send)
                    et_email = popupemail.findViewById(R.id.et_email)
                    tv_email = popupemail.findViewById(R.id.tv_email)
//                    tv_email.setText("Customer email:")
                    btn_cancel.visibility = View.INVISIBLE
                    popupemail.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))

                    btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                    btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                    tv_email.setText(cu.getString(cu.getColumnIndex("SENDRECIPTS")).toString() + " :")

                    popupemail.show()

                    btn_send.setOnClickListener(View.OnClickListener {
                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                            startActivity(Intent(activity, ErrorPage::class.java))
                        } else {
//                            if (isValidEmail(et_email.text.toString())) {
                            //region pay later offline
                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAY_LATER, "1")
                            databaseForPayments.insertPayLater(total_amount.text.toString().replace(",", "."),
                                    discount_amount,
                                    discount_amount,
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                    discount_no,
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0"),
                                    "[" + s + "]", et_email.text.toString(), print_items)

                            if (databaseForPayments.deleteCart()) {
                                SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
                            }

                            startActivity(Intent(activity, Home::class.java))

                            //endregion
                            popupemail.dismiss()

                            /* } else {
                                 message.setText(cu.getString(cu.getColumnIndex("INVALID_EMAIL")).toString())
                                 popup.show()
                                 ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                             }*/
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
//                    cu.close()
                }

            } else {
                message.setText(cu.getString(cu.getColumnIndex("NO_ITEMS")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("NO_ITEMS")).toString(), Toast.LENGTH_SHORT).show()

            }


        })

        back.setOnClickListener(View.OnClickListener { activity.finish() })
//        cu.close()
        return mView
    }

    /*inner class ResponceprocesssorWifi : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
            }
//                wifi = true
        }

    }*/

    inner class ResponceprocessorPayLater : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
            if (responce.status.equals("success")) {
                network = true
                progress.dismiss()
                if (databaseForPayments.deleteCart()) {
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
            val cu = databaseForPayments.getLanguage()
            cu.moveToFirst()
            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            popup.setCancelable(false)

            ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())



            if (responce.status.equals("success")) {
                network = true
                print_receiptNo = responce.orderId.toString()
                if (databaseForPayments.deleteCart()) {
                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                }
                val cu = databaseForPayments.getLanguage()
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

//                ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
                btn_new_sale.setText(cu.getString(cu.getColumnIndex("NEWSALE")).toString())
                btn_print.setText(cu.getString(cu.getColumnIndex("PRINT")).toString())
                btn_send.setText(cu.getString(cu.getColumnIndex("SEND")).toString())
                btn_cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
                tv_email.setText(cu.getString(cu.getColumnIndex("SENDRECIPTS")).toString() + " :")


                btn_new_sale.visibility = View.VISIBLE
                btn_print.visibility = View.VISIBLE
                viewz.visibility = View.VISIBLE

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

                btn_print.setOnClickListener(View.OnClickListener {
                    popupemail.dismiss()


                    val next_pg = Intent(activity, Print::class.java)
                    next_pg.putExtra("PRINT_ITEMS", print_items)
                    next_pg.putExtra("PRINT_ITEMS_REFUND", "")
                    next_pg.putExtra("PRINT_DATE", print_date)
                    next_pg.putExtra("PRINT_DISCOUNT", print_discount)
                    next_pg.putExtra("PRINT_DISCOUNT_AMOUNT", print_discount_amount)
                    next_pg.putExtra("PRINT_TOTAL", print_total)
                    next_pg.putExtra("TAX", responce.tax)
                    next_pg.putExtra("REFUNDED_DISCOUNT", "")

                    next_pg.putExtra("PRINT_RECEIPT_NO", print_receiptNo)
                    startActivity(next_pg)

                })
                btn_send.setOnClickListener(View.OnClickListener {
                    progress.show()
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
//                cu.close()
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
                progress.dismiss()
                startActivity(Intent(activity, Home::class.java))
            }
            /*else{
                Toast.makeText(activity,"xxx",Toast.LENGTH_SHORT).show()
            }*/
        }

    }

    override fun onResume() {
        super.onResume()
        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
    }

    fun print_no_network() {
        val cu = databaseForPayments.language
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
            next_pg.putExtra("REFUNDED_DISCOUNT", "")
            startActivity(next_pg)

        })
        /*btn_send.setOnClickListener(View.OnClickListener {
            progress.show()
            if (isValidEmail(et_email.text.toString())) {
                RequestReceiptEmail(activity, ResponceprocessorReceiptsMail()).getReceiptEmail(et_email.text.toString(), responce.orderId.toString(),
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
                popupemail.dismiss()

            } else {
                message.setText(cu.getString(cu.getColumnIndex("INVALID_EMAIL")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
            }
        })*/
//        cu.close()
    }

    fun isValidEmail(e_mail: String): Boolean {
        return e_mail.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}".toRegex()) && e_mail.length <= 320
    }

    fun set(cu: Cursor, context: Context, discount_aplayed: TextView, ttl_amount: TextView, i: Int, pos: Int, total_amount_formateds: TextView, iOT: TextView) {

        var tax_display = tax_display
        databaseForPayments = SQLiteHelper(context)

        val cu1 = databaseForPayments.getLanguage()
        cu1.moveToFirst()

        if (i == 1) {
            adapterPos = pos

            var percen = tax_display * 100 / ttl_amount.text.toString().replace(",", ".").toDouble()

            /*Toast.makeText(context, cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")) + " "
                    + cu.getString(cu.getColumnIndex("DISCOUNT")), Toast.LENGTH_SHORT).show()*/
            if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                var amount = ttl_amount.text.toString().replace(",", ".").toDouble() * cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble() / 100
                amount = ttl_amount.text.toString().replace(",", ".").toDouble() - amount
                ttl_amount.setText(String.format("%.2f", amount.toDouble()))
                total_amount_formateds.setText(formatter.format(amount.toDouble()))
            } else {
                var amount = ttl_amount.text.toString().replace(",", ".").toDouble() - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble()
                ttl_amount.setText(String.format("%.2f", amount.toDouble()))
                total_amount_formateds.setText(formatter.format(amount.toDouble()))
            }
            discount_aplayed.setText(cu1.getString(cu1.getColumnIndex("APPLIED")) + ": " + cu.getString(cu.getColumnIndex("LABEL")))

            iOT.setText(cu1.getString(cu1.getColumnIndex("INCLUSIVEOFTAXES")).toString() + "(" +formatter.format(ttl_amount.text.toString().replace(",", ".").toDouble() * percen / 100) + ")")

            SharedPreferenceHelper(context).putString(SharedPreferenceHelper(context).TOTAL_AMOUNT, ttl_amount.text.toString().replace(",", "."))
        } else if (i == 0) {
            adapterPos = -1
            discount_aplayed.setText("")
            iOT.setText(cu1.getString(cu1.getColumnIndex("INCLUSIVEOFTAXES")).toString() + "(" +formatter.format( tax_display) + ")")
            if (cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")).equals("1")) {
                var discount_amounts = ((ttl_amount.text.toString().replace(",", ".").toDouble() * 100 /
                        (100 - cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble())) -
                        ttl_amount.text.toString().replace(",", ".").toDouble()) + ttl_amount.text.toString().replace(",", ".").toDouble()
                ttl_amount.setText(String.format("%.2f", discount_amounts.toDouble()))
                total_amount_formateds.setText(formatter.format(discount_amounts.toDouble()))
            } else {
                ttl_amount.setText(String.format("%.2f", ttl_amount.text.toString().replace(",", ".").toDouble() + cu.getString(cu.getColumnIndex("DISCOUNT")).toDouble()))
                total_amount_formateds.setText(formatter.format(ttl_amount.text.toString().replace(",", ".").toDouble()))
            }

        }
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