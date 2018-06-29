package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLaterRecieptDetails
import com.twixt.pranav.pos.Controller.Responce.ResponceProducts

/**
 * Created by Pranav on 12/12/2017.
 */
class RequestpayLaterRecieptDetails(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponcePayLaterRecieptDetails>>) : AbstractRequest<Array<ResponcePayLaterRecieptDetails>>(con, rhandler) {

    fun getPayLaterRecieptDetails(shopId: String, id: String) {
        val call = pOSInterphase.getPayLaterRecieptDetails(shopId,id)
        call.enqueue(this)
    }
}