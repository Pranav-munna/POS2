package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/19/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("status", "orderId", "tax")
class ResponcePay {

    @JsonProperty("status")
    @get:JsonProperty("status")
    var status: String? = null

    @JsonProperty("orderId")
    @get:JsonProperty("orderId")
    var orderId: String? = null

    @JsonProperty("tax")
    @get:JsonProperty("tax")
    var tax: String? = null


}