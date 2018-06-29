package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentPayLaterList

class PayLaterList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState==null){
            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, FragmentPayLaterList(), PayLaterList::class.java.name)
                    .commit()
        }


    }
}
