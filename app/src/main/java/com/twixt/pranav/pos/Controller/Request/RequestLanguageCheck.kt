package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceLanguageCheck

/**
 * Created by Pranav on 12/19/2017.
 */
class RequestLanguageCheck(con: Context, rhandler: ProcessResponcceInterphase<ResponceLanguageCheck>) : AbstractRequest<ResponceLanguageCheck>(con, rhandler) {

    fun checkLanguage(userId: String) {
        val call = pOSInterphase.checkLanguage(userId)
        call.enqueue(this)
    }

}