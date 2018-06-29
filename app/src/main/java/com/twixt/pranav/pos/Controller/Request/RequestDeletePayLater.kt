package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus

/**
 * Created by Pranav on 12/8/2017.
 */
class RequestDeletePayLater(con: Context, rhandler: ProcessResponcceInterphase<ResponceActivateStatus>) : AbstractRequest<ResponceActivateStatus>(con, rhandler) {

    fun getDeletePayLater(orderId: String, items: String) {
        val call = pOSInterphase.getDeletePayLater(orderId, items)
        call.enqueue(this)
    }

}