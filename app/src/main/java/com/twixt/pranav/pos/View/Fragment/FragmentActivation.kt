package com.twixt.pranav.pos.View.Fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestActivate
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Login

/**
 * Created by Pranav on 11/24/2017.
 */
class FragmentActivation : Fragment() {

    lateinit var app_id: EditText
    lateinit var key: EditText
    lateinit var activate: Button
    lateinit var title: TextView
    private var database: SQLiteHelper? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_activation, container, false)
//        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = conMgr.activeNetworkInfo

        app_id = mView.findViewById(R.id.app_id)
        key = mView.findViewById(R.id.key)
        activate = mView.findViewById(R.id.activate)
        title = mView.findViewById(R.id.title)
        database = SQLiteHelper(activity)

        val cu = database!!.getLanguage()
        cu.moveToFirst()
        activate.setText(cu.getString(cu.getColumnIndex("ACTIVATE")).toString())
        title.setText(cu.getString(cu.getColumnIndex("ACTIVATION")).toString())
        app_id.setHint(cu.getString(cu.getColumnIndex("APP_ID")).toString())
        key.setHint(cu.getString(cu.getColumnIndex("KEY_")).toString())

        activate.setOnClickListener(View.OnClickListener {
            if (app_id.text.trim().length <= 0 || key.text.trim().length <= 0) {
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("ENTER_ALL_FIELDS")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                RequestActivate(activity, ResponceProcessorActivate()).getActivation(app_id.text.trim().toString(), key.text.trim().toString())
            }
        })
        cu.close()
        return mView
    }

    inner class ResponceProcessorActivate : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            val cu = database!!.getLanguage()
            cu.moveToFirst()
            if (responce == null)
                Toast.makeText(activity, cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            else {
                if (responce.status.toString().equals("true")) {
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).ACTIVATION_OK, "ok")
                    startActivity(Intent(activity, Login::class.java))
                    activity.finish()
                } else
                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("INVALID_ACTIVATION")).toString(), Toast.LENGTH_SHORT).show()
            }
            cu.close()
        }

    }
}