package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponceBackEndChange

/**
 * Created by Pranav on 12/8/2017.
 */
class RequestBackEndChange(con: Context, rhandler: ProcessResponcceInterphase<ResponceBackEndChange>) : AbstractRequest<ResponceBackEndChange>(con, rhandler) {

    fun getBackEnd(userId: String) {
        val call = pOSInterphase.getBackEnd(userId)
        call.enqueue(this)
    }

}