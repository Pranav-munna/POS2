package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLaterMails

/**
 * Created by Pranav on 12/11/2017.
 */
class RequestPayLaterMails(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponcePayLaterMails>>) : AbstractRequest<Array<ResponcePayLaterMails>>(con, rhandler) {

    fun getPayLaterMails(shopId: String, page: Int) {
        val call = pOSInterphase.getPayLaterMail(shopId, page)
        call.enqueue(this)
    }
}