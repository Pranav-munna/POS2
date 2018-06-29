package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponcePaymentTypes

/**
 * Created by Pranav on 12/18/2017.
 */
class RequestPaymentTypes(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponcePaymentTypes>>) : AbstractRequest<Array<ResponcePaymentTypes>>(con, rhandler) {

    fun getPaymentTypes(id: String) {
        val call = pOSInterphase.getPaymentTypes(id)
        call.enqueue(this)
    }

}