package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/11/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id",
        "paylaterId",
        "orderId",
        "total")

class ResponcePayLaterMails {

    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: Int? = null

    @JsonProperty("paylaterId")
    @get:JsonProperty("paylaterId")
    var paylaterId: String? = null

    @JsonProperty("orderId")
    @get:JsonProperty("orderId")
    var orderId: Int? = null

    @JsonProperty("total")
    @get:JsonProperty("total")
    var total: String? = null

}