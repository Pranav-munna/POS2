package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentPayLaterOrders

class PaylaterOrders : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState==null){
            val id = intent.getStringExtra("ID")

            val bundle = Bundle()
            bundle.putString("ID", id)
            val fragment = FragmentPayLaterOrders()
            fragment.setArguments(bundle)

            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragment, PaylaterOrders::class.java.name)
                    .commit()
        }


    }
}
