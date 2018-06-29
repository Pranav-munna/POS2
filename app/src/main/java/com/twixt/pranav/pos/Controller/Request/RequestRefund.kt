package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceCategories
import com.twixt.pranav.pos.Controller.Responce.ResponceRefund

/**
 * Created by Pranav on 12/11/2017.
 */
class RequestRefund(con: Context, rhandler: ProcessResponcceInterphase<ResponceRefund>) : AbstractRequest<ResponceRefund>(con, rhandler) {

    fun getRefundID(orderId: String, userId: String) {
        val call = pOSInterphase.refund(orderId, userId)
        call.enqueue(this)
    }
}