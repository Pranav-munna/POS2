package com.twixt.pranav.pos.View.Fragment

import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.ErrorPage
import com.twixt.pranav.pos.View.Activity.Home


/**
 * Created by Pranav on 12/7/2017.
 */
class FragmentPinRestart : Fragment() {

    lateinit var cancel: TextView
    lateinit var title: TextView
    lateinit var enter_pin: TextView
    lateinit var ok: Button
    //    lateinit var pin: Pinview
    private lateinit var databaseforpinrestart: SQLiteHelper

    lateinit var first_no: TextView
    lateinit var second_no: TextView
    lateinit var third_no: TextView
    lateinit var fourth_no: TextView
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
    lateinit var delete: ImageButton

    lateinit var message: TextView
    lateinit var okk: Button

    var pin_entered = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_pin, container, false)

        cancel = mView.findViewById(R.id.cancel)
        title = mView.findViewById(R.id.title)
        enter_pin = mView.findViewById(R.id.enter_pin)
        ok = mView.findViewById(R.id.ok)
//        pin = mView.findViewById(R.id.pin)
        one = mView.findViewById(R.id.one)
        two = mView.findViewById(R.id.two)
        three = mView.findViewById(R.id.three)
        four = mView.findViewById(R.id.four)
        five = mView.findViewById(R.id.five)
        six = mView.findViewById(R.id.six)
        seven = mView.findViewById(R.id.seven)
        eight = mView.findViewById(R.id.eight)
        nine = mView.findViewById(R.id.nine)
        zero = mView.findViewById(R.id.zero)
        delete = mView.findViewById(R.id.delete)
        first_no = mView.findViewById(R.id.first_no)
        second_no = mView.findViewById(R.id.second_no)
        third_no = mView.findViewById(R.id.third_no)
        fourth_no = mView.findViewById(R.id.fourth_no)

        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                startActivity(Intent(activity, ErrorPage::class.java))
            }
        }

        databaseforpinrestart = SQLiteHelper(activity)
        val cu = databaseforpinrestart.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("PASSCODE")).toString())
        cancel.setText(cu.getString(cu.getColumnIndex("CANCEL")).toString())
        enter_pin.setText(cu.getString(cu.getColumnIndex("ENTERYOURPIN")).toString())
        ok.setText(cu.getString(cu.getColumnIndex("LOGIN")).toString())


        ok.setOnClickListener(View.OnClickListener {
            val cu1 = databaseforpinrestart.language
            cu1.moveToFirst()
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PIN, "").equals(pin_entered)) {
                activity.finish()
                startActivity(Intent(activity, Home::class.java))
            } else {
                first_no.setText("")
                second_no.setText("")
                third_no.setText("")
                fourth_no.setText("")
                pin_entered = ""

                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                okk = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                message.setText(cu1.getString(cu1.getColumnIndex("INVALID_PIN")))
                popup.show()
                okk.setOnClickListener(View.OnClickListener { popup.dismiss() })

                /*var tr = Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("INVALID_PIN")), Toast.LENGTH_SHORT)
                tr.setGravity(Gravity.CENTER, 0, 0)
                tr.show()*/
            }
            cu1.close()
        })
        one.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "1"
                when (i + 1) {
                    1 -> first_no.setText("1")
                    2 -> second_no.setText("1")
                    3 -> third_no.setText("1")
                    4 -> fourth_no.setText("1")
                }
            }
        })
        two.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "2"
                when (i + 1) {
                    1 -> first_no.setText("2")
                    2 -> second_no.setText("2")
                    3 -> third_no.setText("2")
                    4 -> fourth_no.setText("2")
                }
            }
        })
        three.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "3"
                when (i + 1) {
                    1 -> first_no.setText("3")
                    2 -> second_no.setText("3")
                    3 -> third_no.setText("3")
                    4 -> fourth_no.setText("3")
                }
            }
        })
        four.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "4"
                when (i + 1) {
                    1 -> first_no.setText("4")
                    2 -> second_no.setText("4")
                    3 -> third_no.setText("4")
                    4 -> fourth_no.setText("4")
                }
            }
        })
        five.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "5"
                when (i + 1) {
                    1 -> first_no.setText("5")
                    2 -> second_no.setText("5")
                    3 -> third_no.setText("5")
                    4 -> fourth_no.setText("5")
                }
            }
        })
        six.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "6"
                when (i + 1) {
                    1 -> first_no.setText("6")
                    2 -> second_no.setText("6")
                    3 -> third_no.setText("6")
                    4 -> fourth_no.setText("6")
                }
            }
        })
        seven.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "7"

                when (i + 1) {
                    1 -> first_no.setText("7")
                    2 -> second_no.setText("7")
                    3 -> third_no.setText("7")
                    4 -> fourth_no.setText("7")
                }
            }
        })
        eight.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "8"

                when (i + 1) {
                    1 -> first_no.setText("8")
                    2 -> second_no.setText("8")
                    3 -> third_no.setText("8")
                    4 -> fourth_no.setText("8")
                }
            }
        })
        nine.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "9"
                when (i + 1) {
                    1 -> first_no.setText("9")
                    2 -> second_no.setText("9")
                    3 -> third_no.setText("9")
                    4 -> fourth_no.setText("9")
                }
            }
        })
        zero.setOnClickListener(View.OnClickListener {
            var i = pin_entered.length
            if (i < 4) {
                pin_entered = pin_entered + "0"
                when (i + 1) {
                    1 -> first_no.setText("0")
                    2 -> second_no.setText("0")
                    3 -> third_no.setText("0")
                    4 -> fourth_no.setText("0")
                }
            }
        })

        /* delete.setOnClickListener(View.OnClickListener {
             var i = pin_entered.length
             if (i != 0) {
                 pin_entered = pin_entered.dropLast(i)
                 Toast.makeText(activity, pin_entered, Toast.LENGTH_SHORT).show()
                 when (i) {
                     1 -> first_no.setText("")
                     2 -> second_no.setText("")
                     3 -> third_no.setText("")
                     4 -> fourth_no.setText("")
                 }
             }
         })*/
        cancel.visibility = View.INVISIBLE
        cu.close()
        return mView
    }
}