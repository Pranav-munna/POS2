package com.twixt.pranav.pos.View.Fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Print


/**
 * Created by Pranav on 1/3/2018.
 */

class FragmentSettings : Fragment() {
    private var mWebView: WebView? = null

    lateinit var printer: Button
    lateinit var back: ImageButton
    lateinit var title: TextView
    private lateinit var databaseforSettings: SQLiteHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_settings, container, false)
        printer = mView.findViewById(R.id.printer)
        back = mView.findViewById(R.id.back)
        title = mView.findViewById(R.id.title)

        databaseforSettings = SQLiteHelper(activity)
        val cu = databaseforSettings.language
        cu.moveToFirst()
        title.setText(cu.getString(cu.getColumnIndex("SETTINGS")).toString())
        printer.setText(cu.getString(cu.getColumnIndex("PRINTTEST")).toString())

        printer.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Print::class.java)) })

        back.setOnClickListener(View.OnClickListener { activity.onBackPressed() })
        cu.close()
        return mView
    }

}
