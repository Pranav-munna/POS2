package com.twixt.pranav.pos.View.Activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentPaymentSplit

class PaymentSplit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putString("TOTAL_AMOUNT", intent.getStringExtra("TOTAL_AMOUNT"))
            bundle.putString("ITEMS", intent.getStringExtra("ITEMS"))
            bundle.putString("DISCOUNT_NO", intent.getStringExtra("DISCOUNT_NO"))
            bundle.putString("DISCOUNT_AMOUNT", intent.getStringExtra("DISCOUNT_AMOUNT"))

            bundle.putString("PRINT_ITEMS", intent.getStringExtra("PRINT_ITEMS"))
            bundle.putString("PRINT_ITEMS_REFUND", intent.getStringExtra("PRINT_ITEMS_REFUND"))
            bundle.putString("PRINT_DATE", intent.getStringExtra("PRINT_DATE"))
            bundle.putString("PRINT_DISCOUNT", intent.getStringExtra("PRINT_DISCOUNT"))
            bundle.putString("PRINT_DISCOUNT_AMOUNT", intent.getStringExtra("PRINT_DISCOUNT_AMOUNT"))
            bundle.putString("PRINT_TOTAL", intent.getStringExtra("PRINT_TOTAL"))



            val fragmentPaymentSplit = FragmentPaymentSplit()
            fragmentPaymentSplit.setArguments(bundle)

            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragmentPaymentSplit, "PaymentSplit")
                    .commit()
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom)
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
