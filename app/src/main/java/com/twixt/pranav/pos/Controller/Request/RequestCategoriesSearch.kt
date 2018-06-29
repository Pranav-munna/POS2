package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceCategories

/**
 * Created by Pranav on 12/11/2017.
 */
class RequestCategoriesSearch(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponceCategories>>) : AbstractRequest<Array<ResponceCategories>>(con, rhandler) {

    fun getCategories(shopId: String, key: String) {
        val call = pOSInterphase.getCategoriesSearch(shopId, key)
        call.enqueue(this)
    }
}