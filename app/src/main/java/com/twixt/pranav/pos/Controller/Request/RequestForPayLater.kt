package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponcePayLater

/**
 * Created by Pranav on 12/8/2017.
 */
class RequestForPayLater(con: Context, rhandler: ProcessResponcceInterphase<ResponcePayLater>) : AbstractRequest<ResponcePayLater>(con, rhandler) {

    fun isSuccess(shopid: String, userId: String, total: String, discount: String, discountAmount: String, payableAmount: String, discountType: String, paymentType: String, orderId: String) {
        val call = pOSInterphase.isPayed(shopid, userId, total, discount, discountAmount, payableAmount, discountType, paymentType, orderId)
        call.enqueue(this)
    }

}