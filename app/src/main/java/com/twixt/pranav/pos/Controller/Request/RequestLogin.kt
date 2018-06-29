package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceLogin

/**
 * Created by Pranav on 12/11/2017.
 */
class RequestLogin(con: Context, rhandler: ProcessResponcceInterphase<ResponceLogin>) : AbstractRequest<ResponceLogin>(con, rhandler) {
    fun getLogin(email: String, pass: String, token: String) {
        val call = pOSInterphase.getLogin(email, pass,token)
        call.enqueue(this)
    }
}