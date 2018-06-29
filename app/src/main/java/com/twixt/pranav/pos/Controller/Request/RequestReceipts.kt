package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceReceipts

/**
 * Created by Pranav on 12/26/2017.
 */
class RequestReceipts(con: Context, rhandler: ProcessResponcceInterphase<Array<ResponceReceipts>>) : AbstractRequest<Array<ResponceReceipts>>(con, rhandler) {

    fun getReceipts(shopId: String, page: Int) {
        val call = pOSInterphase.getReceipts(shopId, page)
        call.enqueue(this)
    }

    fun getReceiptSearch(shopId: String, orderId: String) {
        val call = pOSInterphase.getReceiptSearch(shopId, orderId)
        call.enqueue(this)
    }

    fun getReceiptSearchDate(shopId: String, date: String,count :Int) {
        val call = pOSInterphase.getReceiptSearchDate(shopId, date,count)
        call.enqueue(this)
    }

}