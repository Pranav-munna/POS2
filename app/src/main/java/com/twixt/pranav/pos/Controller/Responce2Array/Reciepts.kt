package com.twixt.pranav.pos.Controller.Responce2Array

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/26/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("amount",
        "paidAs",
        "payment_type",
        "time")
class Reciepts {

    @JsonProperty("amount")
    @get:JsonProperty("amount")
    var amount: Int? = null

    @JsonProperty("paidAs")
    @get:JsonProperty("paidAs")
    var paidAs: String? = null

    @JsonProperty("payment_type")
    @get:JsonProperty("payment_type")
    var payment_type: String? = null

    @JsonProperty("time")
    @get:JsonProperty("time")
    var time: String? = null

}