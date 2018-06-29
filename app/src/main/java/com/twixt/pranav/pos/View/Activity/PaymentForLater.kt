package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentPayment
import com.twixt.pranav.pos.View.Fragment.FragmentPaymentForLater
class PaymentForLater : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState==null){
            var bundle=Bundle()
            bundle.putString("TOTAL",intent.getStringExtra("TOTAL"))
            bundle.putString("ORDER_ID",intent.getStringExtra("ORDER_ID"))
            bundle.putString("TAX",intent.getStringExtra("TAX"))

            val fragment = FragmentPaymentForLater()
            fragment.arguments = bundle

            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragment,"PaymentForLater")
                    .commit()
        }

    }
}
