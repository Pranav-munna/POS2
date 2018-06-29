package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/11/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("tax",
        "orderId",
        "paylaterId",
        "date",
        "cashier",
        "total")

class ResponcePayLaterRecieptDetails {

    @JsonProperty("tax")
    @get:JsonProperty("tax")
    var tax: String? = null

    @JsonProperty("orderId")
    @get:JsonProperty("orderId")
    var orderId: String? = null

    @JsonProperty("paylaterId")
    @get:JsonProperty("paylaterId")
    var paylaterId: String? = null

    @JsonProperty("date")
    @get:JsonProperty("date")
    var date: String? = null

    @JsonProperty("cashier")
    @get:JsonProperty("cashier")
    var cashier: String? = null

    @JsonProperty("total")
    @get:JsonProperty("total")
    var total: String? = null

}