package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentViewPayLaterItems

class ViewPayLaterItems : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState == null) {
            var bundle = Bundle()
            bundle.putString("ORDERID", intent.getStringExtra("ORDERID"))


            val fragmennt = FragmentViewPayLaterItems()
            fragmennt.arguments = bundle

            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragmennt, ViewPayLaterItems::class.java.name)
                    .commit()
        }


    }
}
