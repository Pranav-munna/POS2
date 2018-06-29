package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceLanguage

/**
 * Created by Pranav on 12/19/2017.
 */
class RequestLanguage(con: Context, rhandler: ProcessResponcceInterphase<ResponceLanguage>) : AbstractRequest<ResponceLanguage>(con, rhandler) {

    fun getLanguage(userId: String) {
        val call = pOSInterphase.getLanguage(userId)
        call.enqueue(this)
    }

}