package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceCategories

/**
 * Created by Pranav on 12/11/2017.
 */
class RequestCategories(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponceCategories>>) : AbstractRequest<Array<ResponceCategories>>(con, rhandler) {

    fun getCategories(page: Int, shopId: String) {
        val call = pOSInterphase.getCategories(shopId, page)
        call.enqueue(this)
    }
}