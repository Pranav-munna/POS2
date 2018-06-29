package com.twixt.pranav.pos.Controller.Request

import android.content.Context
import com.twixt.pranav.pos.Controller.AbstractRequest
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Responce.ResponcePay

/**
 * Created by Pranav on 12/26/2017.
 */
class RequestPaySplit(con: Context, rhandler: ProcessResponcceInterphase<ResponcePay>) : AbstractRequest<ResponcePay>(con, rhandler) {

    fun pay(shopId: String, userId: String, total: String, discount: String, discountAmount: String, payableAmount: String, discountType: String, paymentType: String, items: String, splitDetails: String) {
        val call = pOSInterphase.paysplit(shopId, userId, total, discount, discountAmount, payableAmount, discountType, paymentType, items, splitDetails)
        call.enqueue(this)
    }

}