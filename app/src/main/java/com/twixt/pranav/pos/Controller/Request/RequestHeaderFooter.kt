package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceHeaderFooter

/**
 * Created by Pranav on 12/8/2017.
 */
class RequestHeaderFooter(con: Context, rhandler: ProcessResponcceInterphase<ResponceHeaderFooter>) : AbstractRequest<ResponceHeaderFooter>(con, rhandler) {

    fun getHeaderFooter(shopId: String) {
        val call = pOSInterphase.getHeaderFooter(shopId)
        call.enqueue(this)
    }

}