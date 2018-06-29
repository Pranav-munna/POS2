package com.twixt.pranav.pos.View.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentNotification

class Notification_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)


        val bundle = Bundle()
        bundle.putString("TITLE", intent.getStringExtra("TITLE"))
        bundle.putString("MESSAGE", intent.getStringExtra("MESSAGE"))


        val fragmentNotification = FragmentNotification()
        fragmentNotification.setArguments(bundle)

        supportFragmentManager.beginTransaction()
                .add(R.id.main_activity, fragmentNotification, "Notification_activity")
                .commit()
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Home::class.java))

    }*/
}
