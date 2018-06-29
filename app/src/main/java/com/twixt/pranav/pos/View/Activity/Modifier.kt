package com.twixt.pranav.pos.View.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragementModifier

class Modifier : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState == null) {

            val bundle = Bundle()
            bundle.putString("ITEMID", intent.getStringExtra("ITEMID"))
            bundle.putString("COUNT", intent.getStringExtra("COUNT"))

            val fragment = FragementModifier()
            fragment.setArguments(bundle)

            supportFragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragment, Modifier::class.java.name)
                    .commit()

        }

    }
}
