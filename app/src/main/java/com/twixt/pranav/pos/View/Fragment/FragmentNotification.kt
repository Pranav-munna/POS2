package com.twixt.pranav.pos.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Home

class FragmentNotification : Fragment() {

    lateinit var back: ImageButton
    lateinit var title: TextView
    lateinit var message: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_notification, container, false)

        back = mView.findViewById(R.id.back)
        title = mView.findViewById(R.id.title)
        message = mView.findViewById(R.id.message)

        title.setText(arguments.getString("TITLE"))
        message.setText(arguments.getString("MESSAGE"))


        back.setOnClickListener({ startActivity(Intent(activity, Home::class.java)) })

        return mView
    }

}
