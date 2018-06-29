package com.twixt.pranav.pos.View.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentCalculationSplit

class CalculationSplit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putString("TOTAL", intent.getStringExtra("TOTAL"))

            val fragmentCalculationSplit = FragmentCalculationSplit()
            fragmentCalculationSplit.setArguments(bundle)

            supportFragmentManager.beginTransaction()
                    .add(R.id.main_activity, fragmentCalculationSplit, "Calculation")
                    .commit()

        }

    }
}
