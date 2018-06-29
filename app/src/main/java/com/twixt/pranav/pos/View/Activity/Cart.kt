package com.twixt.pranav.pos.View.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentCart

class Cart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)

        if (savedInstanceState==null){
            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, FragmentCart(), "Cart")
                    .commit()
        }


    }

    override fun onRestart() {
        super.onRestart()
        try {
            val intent: Intent
            intent = getIntent()
            finish()
            startActivity(intent)
        } catch (e: Exception) {
        }
    }
}
