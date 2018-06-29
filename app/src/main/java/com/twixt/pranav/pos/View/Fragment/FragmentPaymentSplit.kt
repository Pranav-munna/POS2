package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestPaySplit
import com.twixt.pranav.pos.Controller.Request.RequestReceiptEmail
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponcePay
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.CalculationSplit
import com.twixt.pranav.pos.View.Activity.ErrorPage
import com.twixt.pranav.pos.View.Activity.Home
import com.twixt.pranav.pos.View.Activity.Print
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Pranav on 11/28/2017.
 */
class FragmentPaymentSplit : Fragment() {
    //    var wifi = false
    var network = false
    val handler = Handler()

    lateinit var back: ImageButton
    lateinit var pay_a1: Button
    lateinit var pay_a2: Button
    lateinit var pay_a3: Button
    lateinit var pay_a4: Button
    lateinit var pay_a5: Button
    lateinit var pay_a6: Button
    lateinit var pay_a7: Button
    lateinit var pay_a8: Button
    lateinit var pay_a9: Button
    lateinit var pay_a10: Button

    lateinit var btn_send: Button
    lateinit var btn_cancel: Button
    lateinit var et_email: AppCompatEditText
    lateinit var tv_email: AppCompatTextView
    lateinit var viewz: View
    lateinit var btn_new_sale: Button
    lateinit var btn_print: Button

    lateinit var checkout: Button
    //    lateinit var rv_split: RecyclerView
    lateinit var paying_amounts_a1: EditText
    lateinit var paying_amounts_a2: EditText
    lateinit var paying_amounts_a3: EditText
    lateinit var paying_amounts_a4: EditText
    lateinit var paying_amounts_a5: EditText
    lateinit var paying_amounts_a6: EditText
    lateinit var paying_amounts_a7: EditText
    lateinit var paying_amounts_a8: EditText
    lateinit var paying_amounts_a9: EditText
    lateinit var paying_amounts_a10: EditText
    lateinit var remaning_amount: TextView
    //    lateinit var title: TextView
    lateinit var r_amount: TextView
    lateinit var count: TextView
    lateinit var relative_layout_a1: RelativeLayout
    lateinit var relative_layout_a2: RelativeLayout
    lateinit var relative_layout_a3: RelativeLayout
    lateinit var relative_layout_a4: RelativeLayout
    lateinit var relative_layout_a5: RelativeLayout
    lateinit var relative_layout_a6: RelativeLayout
    lateinit var relative_layout_a7: RelativeLayout
    lateinit var relative_layout_a8: RelativeLayout
    lateinit var relative_layout_a9: RelativeLayout
    lateinit var relative_layout_a10: RelativeLayout
    lateinit var card_type_a1: Spinner
    lateinit var card_type_a2: Spinner
    lateinit var card_type_a3: Spinner
    lateinit var card_type_a4: Spinner
    lateinit var card_type_a5: Spinner
    lateinit var card_type_a6: Spinner
    lateinit var card_type_a7: Spinner
    lateinit var card_type_a8: Spinner
    lateinit var card_type_a9: Spinner
    lateinit var card_type_a10: Spinner

    lateinit var message: TextView
    lateinit var ok: Button
    lateinit var progress: ProgressDialog

    private lateinit var databaseforpSplit: SQLiteHelper
    //    var cards = arrayOf("- - Select Payment- -", "Card", "Cash", "Net Banking", "Paytm")
    var i = 0
    var SplitDetails = ""

    var print_items = ""
    var print_discount = ""
    var print_discount_amount = ""
    var print_total = ""
    var print_date = ""

    var dot_comma_int = 1.1
    val decimal_format = DecimalFormat("#,###,##0.000")
    //    val d_reverse = DecimalFormat("0.000")
    val d_reverse = DecimalFormat("#,###,##0.000")

    var replace_String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_payment_split, container, false)

//        RequestcheckingNetwork(activity, ResponceprocesrWifi()).getStatus()

        try {
            if (decimal_format.format(dot_comma_int).toDouble() == 1.1) {
                replace_String = ","
            }
        } catch (e: Exception) {
            replace_String = "."
        }
        //region findViewById
        progress = ProgressDialog(activity)
//        val rvAdapterSplitPayment = RvAdapterSplitPayment(activity)
        back = mView.findViewById(R.id.back)
        checkout = mView.findViewById(R.id.checkout)
//        rv_split = mView.findViewById(R.id.rv_split)
//        split = mView.findViewById(R.id.split)
        remaning_amount = mView.findViewById(R.id.remaning_amount)
        card_type_a1 = mView.findViewById(R.id.card_type_a1)
        card_type_a10 = mView.findViewById(R.id.card_type_a10)
        card_type_a2 = mView.findViewById(R.id.card_type_a2)
        card_type_a3 = mView.findViewById(R.id.card_type_a3)
        card_type_a4 = mView.findViewById(R.id.card_type_a4)
        card_type_a5 = mView.findViewById(R.id.card_type_a5)
        card_type_a6 = mView.findViewById(R.id.card_type_a6)
        card_type_a7 = mView.findViewById(R.id.card_type_a7)
        card_type_a8 = mView.findViewById(R.id.card_type_a8)
        card_type_a9 = mView.findViewById(R.id.card_type_a9)
        count = mView.findViewById(R.id.count)
        pay_a1 = mView.findViewById(R.id.pay_a1)
        pay_a2 = mView.findViewById(R.id.pay_a2)
        pay_a3 = mView.findViewById(R.id.pay_a3)
        pay_a4 = mView.findViewById(R.id.pay_a4)
        pay_a5 = mView.findViewById(R.id.pay_a5)
        pay_a6 = mView.findViewById(R.id.pay_a6)
        pay_a7 = mView.findViewById(R.id.pay_a7)
        pay_a8 = mView.findViewById(R.id.pay_a8)
        pay_a9 = mView.findViewById(R.id.pay_a9)
        pay_a10 = mView.findViewById(R.id.pay_a10)
        relative_layout_a1 = mView.findViewById(R.id.relative_layout_a1)
        relative_layout_a2 = mView.findViewById(R.id.relative_layout_a2)
        relative_layout_a3 = mView.findViewById(R.id.relative_layout_a3)
        relative_layout_a4 = mView.findViewById(R.id.relative_layout_a4)
        relative_layout_a5 = mView.findViewById(R.id.relative_layout_a5)
        relative_layout_a6 = mView.findViewById(R.id.relative_layout_a6)
        relative_layout_a7 = mView.findViewById(R.id.relative_layout_a7)
        relative_layout_a8 = mView.findViewById(R.id.relative_layout_a8)
        relative_layout_a9 = mView.findViewById(R.id.relative_layout_a9)
        relative_layout_a10 = mView.findViewById(R.id.relative_layout_a10)
        paying_amounts_a1 = mView.findViewById(R.id.paying_amounts_a1)
        paying_amounts_a2 = mView.findViewById(R.id.paying_amounts_a2)
        paying_amounts_a3 = mView.findViewById(R.id.paying_amounts_a3)
        paying_amounts_a4 = mView.findViewById(R.id.paying_amounts_a4)
        paying_amounts_a5 = mView.findViewById(R.id.paying_amounts_a5)
        paying_amounts_a6 = mView.findViewById(R.id.paying_amounts_a6)
        paying_amounts_a7 = mView.findViewById(R.id.paying_amounts_a7)
        paying_amounts_a8 = mView.findViewById(R.id.paying_amounts_a8)
        paying_amounts_a9 = mView.findViewById(R.id.paying_amounts_a9)
        paying_amounts_a10 = mView.findViewById(R.id.paying_amounts_a10)
//        title = mView.findViewById(R.id.title)
        r_amount = mView.findViewById(R.id.r_amount)
        databaseforpSplit = SQLiteHelper(activity)

        //endregion

        val cu = databaseforpSplit.language
        cu.moveToFirst()
        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))

        val cashType = arrayOfNulls<String>(databaseforpSplit.paymentTypes.count + 1)
        cashType[0] = cu.getString(cu.getColumnIndex("SELECT_PAYMENT_METHOD")).toString()
        var j = 1
        while (j < databaseforpSplit.paymentTypes.count + 1) {
            val cu_payment = databaseforpSplit.paymentTypedatas(j - 1)
            cu_payment.moveToFirst()
            cashType[j] = cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_NAME")).toString()
            j++
            cu_payment.close()
        }


//        title.setText(cu.getString(cu.getColumnIndex("PAYMENT")).toString())
//        pay_a1.setText(cu.getString(cu.getColumnIndex("PAY")).toString())
        r_amount.setText(cu.getString(cu.getColumnIndex("REMANINGAMOUNT")).toString())
        checkout.setText(cu.getString(cu.getColumnIndex("CHARGE")).toString())

        val width_px = Resources.getSystem().displayMetrics.widthPixels
        val height_px = Resources.getSystem().displayMetrics.heightPixels
        val pixeldpi = Resources.getSystem().displayMetrics.densityDpi

        val width_dp = width_px / pixeldpi * 220
        val height_dp = height_px / pixeldpi * 220
        if (width_dp > 700 && height_dp > 700)
            activity.window.setLayout(width_dp.toInt(), height_dp.toInt())

        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                startActivity(Intent(activity, ErrorPage::class.java))
            }
        }

        var amount = /*String.format("%.2f",*/ arguments.getString("TOTAL_AMOUNT").toDouble()/*).toDouble()*/
        var s = arguments.getString("ITEMS")
        var discount_no = arguments.getString("DISCOUNT_NO")
        var discount_amount = arguments.getString("DISCOUNT_AMOUNT")



        print_items = arguments.getString("PRINT_ITEMS")
        print_discount = arguments.getString("PRINT_DISCOUNT")
        print_discount_amount = arguments.getString("PRINT_DISCOUNT_AMOUNT")
        print_total = arguments.getString("PRINT_TOTAL")

//        Toast.makeText(activity, print_total, Toast.LENGTH_SHORT).show()

        val adapterCardName = ArrayAdapter<String>(activity, R.layout.support_simple_spinner_dropdown_item, cashType)

        card_type_a1.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a2.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a3.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a4.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a5.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a6.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a7.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a8.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a9.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()
        card_type_a10.adapter = adapterCardName
        adapterCardName.notifyDataSetChanged()

        paying_amounts_a1.setText(d_reverse.format(amount / 2).toString())
        paying_amounts_a2.setText(d_reverse.format(amount / 2).toString())
        remaning_amount.setText(decimal_format.format(amount).toString())

        var c = Calendar.getInstance().time
        var df = SimpleDateFormat("dd-MMM-yyyy")

        print_date = df.format(c).toString()









        count.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.getAction() === MotionEvent.ACTION_UP) {
                    val textLocation = IntArray(2)
                    count.getLocationOnScreen(textLocation)

                    if (event.getRawX() <= textLocation[0] + count.getTotalPaddingLeft()) {
                        if (!count.text.toString().equals("2"))
                            count.setText((count.text.toString().toInt() - 1).toString())

                        if (count.text.toString().equals("2"))
                            relative_layout_a3.visibility = View.GONE
                        if (count.text.toString().equals("3"))
                            relative_layout_a4.visibility = View.GONE
                        if (count.text.toString().equals("4"))
                            relative_layout_a5.visibility = View.GONE
                        if (count.text.toString().equals("5"))
                            relative_layout_a6.visibility = View.GONE
                        if (count.text.toString().equals("6"))
                            relative_layout_a7.visibility = View.GONE
                        if (count.text.toString().equals("7"))
                            relative_layout_a8.visibility = View.GONE
                        if (count.text.toString().equals("8"))
                            relative_layout_a9.visibility = View.GONE
                        if (count.text.toString().equals("9"))
                            relative_layout_a10.visibility = View.GONE

                        return true
                    }


                    if (event.getRawX() >= textLocation[0] + count.getWidth() - count.getTotalPaddingRight()) {
                        if (!count.text.toString().equals("10"))
                            count.setText((count.text.toString().toInt() + 1).toString())


                        if (count.text.toString().equals("3"))
                            relative_layout_a3.visibility = View.VISIBLE
                        if (count.text.toString().equals("4"))
                            relative_layout_a4.visibility = View.VISIBLE
                        if (count.text.toString().equals("5"))
                            relative_layout_a5.visibility = View.VISIBLE
                        if (count.text.toString().equals("6"))
                            relative_layout_a6.visibility = View.VISIBLE
                        if (count.text.toString().equals("7"))
                            relative_layout_a7.visibility = View.VISIBLE
                        if (count.text.toString().equals("8"))
                            relative_layout_a8.visibility = View.VISIBLE
                        if (count.text.toString().equals("9"))
                            relative_layout_a9.visibility = View.VISIBLE
                        if (count.text.toString().equals("10"))
                            relative_layout_a10.visibility = View.VISIBLE

                        return true
                    }
                }
                return true
            }
        })

        /* rv_split.adapter = rvAdapterSplitPayment
         rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/




        pay_a10.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a10.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
            } else
                if (d_reverse.format((paying_amounts_a10.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a10.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a10.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a10.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a10.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a10.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a10.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a10.t ext.toString() + "#" + card_type_a10.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a10.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a10.isEnabled = false
                            paying_amounts_a10.isEnabled = false
                            card_type_a10.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a10.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a9.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a9.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if (d_reverse.format((paying_amounts_a9.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a9.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a9.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a9.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a9.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a9.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a9.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a9.text.toString() + "#" + card_type_a9.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a9.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a9.isEnabled = false
                            paying_amounts_a9.isEnabled = false
                            card_type_a9.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a9.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }

//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }

                    }
                }

        })

        pay_a8.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a8.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if (d_reverse.format((paying_amounts_a8.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a8.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a8.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a8.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a8.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a8.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a8.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a8.text.toString() + "#" + card_type_a8.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a8.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a8.isEnabled = false
                            paying_amounts_a8.isEnabled = false
                            card_type_a8.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a8.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a7.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a7.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if (d_reverse.format((paying_amounts_a7.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a7.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a7.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a7.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a7.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a7.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a7.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a7.text.toString() + "#" + card_type_a7.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a7.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a7.isEnabled = false
                            paying_amounts_a7.isEnabled = false
                            card_type_a7.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a7.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a6.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a6.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if (d_reverse.format((paying_amounts_a6.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a6.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a6.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a6.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a6.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a6.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a6.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a6.text.toString() + "#" + card_type_a6.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a6.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a6.isEnabled = false
                            paying_amounts_a6.isEnabled = false
                            card_type_a6.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a6.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }
                            cu_payment.close()
//                        }
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a5.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a5.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if (d_reverse.format((paying_amounts_a5.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a5.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a5.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a5.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a5.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a5.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a5.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a5.text.toString() + "#" + card_type_a5.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a5.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a5.isEnabled = false
                            paying_amounts_a5.isEnabled = false
                            card_type_a5.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a5.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a4.setOnClickListener(View.OnClickListener {
            if (paying_amounts_a4.text.toString().trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if (d_reverse.format((paying_amounts_a4.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a4.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= d_reverse.format((paying_amounts_a4.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a4.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + paying_amounts_a4.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + paying_amounts_a4.text.toString().replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= d_reverse.format((paying_amounts_a4.text.toString().replace(",", ".")).toDouble()).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a4.text.toString() + "#" + card_type_a4.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a4.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a4.isEnabled = false
                            paying_amounts_a4.isEnabled = false
                            card_type_a4.isEnabled = false
                            if (d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", paying_amounts_a4.text.toString().replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a3.setOnClickListener(View.OnClickListener {
            if ((paying_amounts_a3.text.toString().replace(replace_String, "")).trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

            } else
                if ((d_reverse.format(((paying_amounts_a3.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                } else {
                    if (card_type_a3.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= (d_reverse.format(((paying_amounts_a3.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a3.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + (paying_amounts_a3.text.toString().replace(replace_String, "")).replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + (paying_amounts_a3.text.toString().replace(replace_String, "")).replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= (d_reverse.format(((paying_amounts_a3.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a3.te xt.toString() + "#" + card_type_a3.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a3.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a3.isEnabled = false
                            paying_amounts_a3.isEnabled = false
                            card_type_a3.isEnabled = false
                            if ((d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", (paying_amounts_a3.text.toString().replace(replace_String, "")).replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a2.setOnClickListener(View.OnClickListener {
            if ((paying_amounts_a2.text.toString().replace(replace_String, "")).trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
            } else
                if ((d_reverse.format(((paying_amounts_a2.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a2.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= (d_reverse.format(((paying_amounts_a2.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a2.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + (paying_amounts_a2.text.toString().replace(replace_String, "")).replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + (paying_amounts_a2.text.toString().replace(replace_String, "")).replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= (d_reverse.format(((paying_amounts_a2.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a2.t ext.toString() + "#" + card_type_a2.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*paying_amounts_a2.setText("0.0")
                            if (d_reverse.format(remaning_amount.text.toString().toDouble()).toDouble() == 0.0) {*/
                            pay_a2.isEnabled = false
                            paying_amounts_a2.isEnabled = false
                            card_type_a2.isEnabled = false
                            if ((d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", (paying_amounts_a2.text.toString().replace(replace_String, "")).replace(",", "."))
                                startActivity(nextpage)
                            }
//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })

        pay_a1.setOnClickListener(View.OnClickListener {
            if ((paying_amounts_a1.text.toString().replace(replace_String, "")).trim().length == 0) {
                message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
            } else
                if ((d_reverse.format(((paying_amounts_a1.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).toString().replace(",", ".").toDouble() <= 0) {
                    message.setText(cu.getString(cu.getColumnIndex("ENTEER_AMOUNT")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                } else {
                    if (card_type_a1.getSelectedItemPosition() == 0) {

                        message.setText(cu.getString(cu.getColumnIndex("SELECT_PAYMENT_TOAST")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

                    } else {
                        if (amount >= (d_reverse.format(((paying_amounts_a1.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble()) {
                            val cu_payment = databaseforpSplit.paymentTypedatas(card_type_a1.getSelectedItemPosition() - 1)
                            cu_payment.moveToFirst()

                            if (SplitDetails.length == 0) {
                                SplitDetails = """{"amount":"""" + (paying_amounts_a1.text.toString().replace(replace_String, "")).replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            } else {
                                SplitDetails = SplitDetails + """,{"amount":"""" + (paying_amounts_a1.text.toString().replace(replace_String, "")).replace(",", ".") + """","paymentType":"""" + cu_payment.getString(cu_payment.getColumnIndex("PAYMENT_ID")).toString() + """"}"""
                            }
                            amount -= (d_reverse.format(((paying_amounts_a1.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble()
                            /*rvAdapterSplitPayment.set((paying_amounts_a1.t ext.toString() + "#" + card_type_a1.selectedItem.toString()), i++)
                            rv_split.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/
                            remaning_amount.setText(decimal_format.format(amount).toString())
                            /*.toDouble()*/
                            pay_a1.isEnabled = false
                            paying_amounts_a1.isEnabled = false
                            card_type_a1.isEnabled = false
                            if ((d_reverse.format(((remaning_amount.text.toString().replace(replace_String, "")).replace(",", ".")).toDouble()).replace(replace_String, "")).replace(",", ".").toDouble() <= 0) {
                                checkout.visibility = View.VISIBLE
                                count.visibility = View.INVISIBLE
                                if (pay_a1.isEnabled != false) {
                                    relative_layout_a1.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a2.isEnabled != false) {
                                    relative_layout_a2.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a3.isEnabled != false) {
                                    relative_layout_a3.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a4.isEnabled != false) {
                                    relative_layout_a4.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a5.isEnabled != false) {
                                    relative_layout_a5.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a6.isEnabled != false) {
                                    relative_layout_a6.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a7.isEnabled != false) {
                                    relative_layout_a7.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a8.isEnabled != false) {
                                    relative_layout_a8.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a9.isEnabled != false) {
                                    relative_layout_a9.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }
                                if (pay_a10.isEnabled != false) {
                                    relative_layout_a10.visibility = View.GONE
                                    count.setText((count.text.toString().toInt() - 1).toString())
                                }

                            }
                            if (cu_payment.getString(cu_payment.getColumnIndex("PAYMENTTYPE")).toString().equals("Cash")) {
                                val nextpage = Intent(activity, CalculationSplit::class.java)
                                nextpage.putExtra("TOTAL", (paying_amounts_a1.text.toString().replace(replace_String, "")).replace(",", "."))
                                startActivity(nextpage)
                            }

//                        }
                            cu_payment.close()
                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("AMOUNT_TOO_HEIGH")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    }
                }

        })


        checkout.setOnClickListener(View.OnClickListener {
            checkout.isClickable = false
            progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
            progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
            progress.show()
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting


//            RequestcheckingNetwork(activity, ResponceprocesrWifi()).getStatus()


            if (isConnected /*&& wifi*/) {

                RequestPaySplit(activity, ResponceProcessorSplitPay()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                        arguments.getString("TOTAL_AMOUNT").toString(),
                        discount_amount,
                        discount_amount,
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                        discount_no,
                        "",
                        "[" + s + "]",
                        "[" + SplitDetails + "]")

//                Toast.makeText(activity, databaseforpSplit.pendingPaySplitSize.count.toString() + SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0"), Toast.LENGTH_SHORT).show()
                if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0").equals("1")) {
                    var i = 0

                    while (i < databaseforpSplit.pendingPaySplitSize.count) {
                        val cu = databaseforpSplit.getPendingPaySplit(i)
                        cu.moveToFirst()
//                        Toast.makeText(activity, cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")), Toast.LENGTH_SHORT).show()

                        RequestPaySplit(activity, ResponceProcessorSplitPay()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                                cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                                cu.getString(cu.getColumnIndex("DISCOUNT")),
                                cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                                cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                                cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                                cu.getString(cu.getColumnIndex("PAYMENT_TYPE")),
                                cu.getString(cu.getColumnIndex("ITEMS")),
                                cu.getString(cu.getColumnIndex("SPLIT_DETAILS")))
                        i++
//                        cu.close()
                    }

                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0")
                    databaseforpSplit.deletePaySplit()
                }
//                startActivity(Intent(activity, Home::class.java))
//                wifi = false


                handler.postDelayed({

                    if (!network) {
                        progress.dismiss()

//                        Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()

                        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                            startActivity(Intent(activity, ErrorPage::class.java))
                        } else {
                            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "1")
                            databaseforpSplit.insertPaySplit(arguments.getString("TOTAL_AMOUNT").toString(),
                                    discount_amount,
                                    discount_amount,
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                                    discount_no,
                                    "",
                                    "[" + s + "]",
                                    "[" + SplitDetails + "]")

                            if (databaseforpSplit.deleteCart()) {
                                SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(activity, Home::class.java))
                                print_no_network()
                            }
                        }

                        network = false
                    }

                }, 5000)


            } else {
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "1")
                databaseforpSplit.insertPaySplit(arguments.getString("TOTAL_AMOUNT").toString(),
                        discount_amount,
                        discount_amount,
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0").trim(),
                        discount_no,
                        "",
                        "[" + s + "]",
                        "[" + SplitDetails + "]")

                if (databaseforpSplit.deleteCart()) {
                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(activity, Home::class.java))
                    print_no_network()
                }
            }
        })
        back.setOnClickListener(View.OnClickListener { activity.finish() })
//        cu.close()
        return mView
    }


    inner class ResponceprocesrWifi : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
//                wifi = true
            }
        }

    }

    inner class ResponceProcessorSplitPay : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
            val cu = databaseforpSplit.getLanguage()
            cu.moveToFirst()

            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            popup.setCancelable(false)
            ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())

            if (responce != null) {
//                Log.e("helphelp",responce.status)
                progress.dismiss()
                network = true
                progress.setTitle(cu.getString(cu.getColumnIndex("LOADING")).toString())
                progress.setMessage(cu.getString(cu.getColumnIndex("WAIT")).toString() + "...")
                progress.setCancelable(false)
                if (databaseforpSplit.deleteCart()) {
                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()


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

//                    tv_email.setText("Send Receipt to :")

                    btn_new_sale.setText(cu.getString(cu.getColumnIndex("NEWSALE")).toString())
                    btn_print.setText(cu.getString(cu.getColumnIndex("PRINT")).toString())
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
                        next_pg.putExtra("PRINT_RECEIPT_NO", responce.orderId)
                        next_pg.putExtra("TAX", responce.tax)
                        next_pg.putExtra("REFUNDED_DISCOUNT", "")
                        startActivity(next_pg)
                        activity.finish()
                    })
                    btn_send.setOnClickListener(View.OnClickListener {
                        if (isValidEmail(et_email.text.toString())) {
                            progress.show()
                            RequestReceiptEmail(activity, ResponceprocessorReceiptsMail()).getReceiptEmail(et_email.text.toString(), responce.orderId.toString(),
                                    SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
                            popupemail.dismiss()

                        } else {
                            message.setText(cu.getString(cu.getColumnIndex("INVALIDEMAIL")).toString())
                            popup.show()
                            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
                        }
                    })


                }
//                startActivity(Intent(activity, Home::class.java))
            } else {
                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                ok = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
                message.setText(cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            }
//            cu.close()
        }

        inner class ResponceprocessorReceiptsMail : ProcessResponcceInterphase<ResponceActivateStatus> {
            override fun processResponce(responce: ResponceActivateStatus) {
                if (responce.status.equals("success")) {
                    progress.dismiss()
                    startActivity(Intent(activity, Home::class.java))
                }

            }

        }

    }

    fun print_no_network() {
        val cu = databaseforpSplit.getLanguage()
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

//        tv_email.setText("Send Receipt to :")


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
            activity.finish()
        })
        /*btn_send.setOnClickListener(View.OnClickListener {
            progress.show()
            if (isValidEmail(et_email.text.toString())) {
                RequestReceiptEmail(activity, ResponceprocessorReceiptsMail()).getReceiptEmail(et_email.text.toString(), responce.orderId.toString(),
                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
                popupemail.dismiss()

            } else {
                message.setText(cu.getString(cu.getColumnIndex("INVALIDEMAIL")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
            }
        })*/
//        cu.close()
    }

    fun isValidEmail(e_mail: String): Boolean {
        return e_mail.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}".toRegex()) && e_mail.length <= 320
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            progress.dismiss()
        } catch (e: Exception) {
        }
    }
}

