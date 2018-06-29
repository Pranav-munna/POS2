package com.twixt.pranav.pos.View.Fragment

import android.app.Fragment
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestcheckingNetwork
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R

/**
 * Created by Pranav on 1/11/2018.
 */
class FragmentErrorPage : Fragment() {
    var wifi = false
    private var database: SQLiteHelper? = null
    lateinit var text: TextView
    lateinit var refresh: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_error_page, container, false)

        RequestcheckingNetwork(activity, ResponceprocessorNetwork()).getStatus()

        text = mView.findViewById(R.id.text)
        refresh = mView.findViewById(R.id.refresh)
        database = SQLiteHelper(activity)
        val cu = database!!.language
        cu.moveToFirst()

        text.setText(cu.getString(cu.getColumnIndex("OFFLINE_ERROR")).toString())
        refresh.setOnClickListener(View.OnClickListener {
            RequestcheckingNetwork(activity, ResponceprocessorNetwork()).getStatus()
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (isConnected && wifi) {
                activity.onBackPressed()
            } else {
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("OFFLINE_ERROR")).toString(), Toast.LENGTH_SHORT).show()
            }
        })
        cu.close()
        return mView
    }


    inner class ResponceprocessorNetwork : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
                wifi = true
//                activity.onBackPressed()
            }
        }

    }
}