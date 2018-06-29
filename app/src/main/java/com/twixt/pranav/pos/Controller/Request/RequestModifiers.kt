package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceCategories
import com.twixt.pranav.pos.Controller.Responce.ResponceModifier

/**
 * Created by Pranav on 12/11/2017.
 */
class RequestModifiers(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponceModifier>>) : AbstractRequest<Array<ResponceModifier>>(con, rhandler) {

    fun getModifiers(page: Int, shopId: String) {
        val call = pOSInterphase.getModifiers(shopId, page)
        call.enqueue(this)
    }
}