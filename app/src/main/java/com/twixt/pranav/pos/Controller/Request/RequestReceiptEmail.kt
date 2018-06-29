package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus

/**
 * Created by Pranav on 12/8/2017.
 */
class RequestReceiptEmail(con: Context, rhandler: ProcessResponcceInterphase<ResponceActivateStatus>) : AbstractRequest<ResponceActivateStatus>(con, rhandler) {

    fun getReceiptEmail(email: String, orderId: String, shopId: String) {
        val call = pOSInterphase.getReceiptsEmail(email, orderId, shopId)
        call.enqueue(this)
    }

}