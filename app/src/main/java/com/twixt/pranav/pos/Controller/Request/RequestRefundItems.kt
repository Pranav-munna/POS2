package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus

/**
 * Created by Pranav on 12/8/2017.
 */
class RequestRefundItems(con: Context, rhandler: ProcessResponcceInterphase<ResponceActivateStatus>) : AbstractRequest<ResponceActivateStatus>(con, rhandler) {

    fun refunditems(orderId: String, userId: String, products: String, shopId: String, payableAmount: String, paymentType: String) {
        val call = pOSInterphase.refunditems(orderId, userId,products,shopId,payableAmount,paymentType)
        call.enqueue(this)
    }

}