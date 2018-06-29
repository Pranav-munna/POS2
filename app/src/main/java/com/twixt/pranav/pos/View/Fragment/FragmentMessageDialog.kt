package com.twixt.pranav.pos.View.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twixt.pranav.pos.R

/**
 * Created by Pranav on 1/30/2018.
 */
class FragmentMessageDialog : DialogFragment() {
    lateinit var message: TextView
    lateinit var mDialog: AlertDialog
    lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mView = inflater.inflate(R.layout.fragement_message_dialog, container, false)
        if (mDialog != null) mDialog.setView(mView)
        message = mView.findViewById(R.id.message)

        /* popup.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
                popup.getWindow().setLayout(650, 750);*/

        return mView
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        mDialog = AlertDialog.Builder(activity)
                .setCancelable(false)
                .setView(mView)
                .create()
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
        return mDialog
    }

}