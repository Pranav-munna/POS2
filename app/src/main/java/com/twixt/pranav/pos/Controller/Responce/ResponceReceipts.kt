package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/26/2017.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("orderId",
        "amount",
        "paidAs",
        "payment_type",
        "refundStatus",
        "date",
        "time")
class ResponceReceipts {

    @JsonProperty("orderId")
    @get:JsonProperty("orderId")
    var orderId: Int? = null

    @JsonProperty("amount")
    @get:JsonProperty("amount")
    var amount: Double? = null

    @JsonProperty("paidAs")
    @get:JsonProperty("paidAs")
    var paidAs: String? = null

    @JsonProperty("payment_type")
    @get:JsonProperty("payment_type")
    var payment_type: String? = null

    @JsonProperty("refundStatus")
    @get:JsonProperty("refundStatus")
    var refundStatus: String? = null

    @JsonProperty("date")
    @get:JsonProperty("date")
    var date: String? = null

    @JsonProperty("time")
    @get:JsonProperty("time")
    var time: String? = null

}