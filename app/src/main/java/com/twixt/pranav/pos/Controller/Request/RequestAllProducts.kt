package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceProducts

/**
 * Created by Pranav on 12/12/2017.
 */
class RequestAllProducts(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponceProducts>>) : AbstractRequest<Array<ResponceProducts>>(con, rhandler) {

    fun getProducts(id: String, page: Int) {
        val call = pOSInterphase.getAllProducts(id,page)
        call.enqueue(this)
    }
}