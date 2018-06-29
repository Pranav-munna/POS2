package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentRefund

class Refund : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState==null){
            val id = intent.getStringExtra("orderId")

            val bundle = Bundle()
            bundle.putString("orderId", id)
            val fragmentRefund = FragmentRefund()
            fragmentRefund.setArguments(bundle)

            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragmentRefund, "Refund")
                    .commit()
        }

    }
}
