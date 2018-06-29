package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentRefundItems

class RefundItems : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState==null){
            val id = intent.getStringExtra("orderId")

            val bundle = Bundle()
            bundle.putString("orderId", id)
            val fragmentRefundItems = FragmentRefundItems()
            fragmentRefundItems.setArguments(bundle)


            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragmentRefundItems, "RefundItems")
                    .commit()
        }

    }
}
