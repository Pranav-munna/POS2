package com.twixt.pranav.pos.View.Fragment.Tab

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Adapter.RvAdapterItemListHome


/**
 * Created by Pranav on 11/24/2017.
 */

class HomeTab : Fragment() {
    lateinit var recyclerview_list: RecyclerView
    var count = 1
    lateinit var message: TextView
    lateinit var ok: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.pv_tab_home, container, false)
        recyclerview_list = mView.findViewById(R.id.recyclerview)
        val adapterItemList_Rv = RvAdapterItemListHome(activity)
        recyclerview_list.adapter = adapterItemList_Rv
        adapterItemList_Rv.set1()
        recyclerview_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return mView
    }
}