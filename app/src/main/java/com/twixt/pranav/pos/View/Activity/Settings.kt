package com.twixt.pranav.pos.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentSettings

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)

        if (savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_activity, FragmentSettings(), "Settings")
                    .commit()
        }


    }
}
