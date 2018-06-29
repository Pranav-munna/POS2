package com.twixt.pranav.pos.View.Activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentPrint

class Print : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.requestedOrientation = this.getResources().getConfiguration().orientation

        if (savedInstanceState == null) {

            val bundle = Bundle()
            bundle.putString("PRINT_ITEMS", intent.getStringExtra("PRINT_ITEMS"))
            bundle.putString("PRINT_ITEMS_REFUND", intent.getStringExtra("PRINT_ITEMS_REFUND"))
            bundle.putString("PRINT_DATE", intent.getStringExtra("PRINT_DATE"))
            bundle.putString("PRINT_DISCOUNT", intent.getStringExtra("PRINT_DISCOUNT"))
            bundle.putString("PRINT_DISCOUNT_AMOUNT" , intent.getStringExtra("PRINT_DISCOUNT_AMOUNT"))
            bundle.putString("PRINT_TOTAL", intent.getStringExtra("PRINT_TOTAL"))
            bundle.putString("PRINT_RECEIPT_NO", intent.getStringExtra("PRINT_RECEIPT_NO"))
            bundle.putString("TAX", intent.getStringExtra("TAX"))
            bundle.putString("REFUNDED_DISCOUNT", intent.getStringExtra("REFUNDED_DISCOUNT"))

            val fragmentSplit = FragmentPrint()
            fragmentSplit.setArguments(bundle)

            supportFragmentManager.beginTransaction()
                    .add(R.id.main, fragmentSplit, Print::class.java.name)
                    .commit()
        }
    }
}
