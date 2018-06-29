package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceDiscounts
import com.twixt.pranav.pos.Controller.Responce.ResponcePaymentTypes

/**
 * Created by Pranav on 12/18/2017.
 */
class RequestDescount(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponceDiscounts>>) : AbstractRequest<Array<ResponceDiscounts>>(con, rhandler) {

    fun getDiscounts(id: String) {
        val call = pOSInterphase.getDescount(id)
        call.enqueue(this)
    }

}