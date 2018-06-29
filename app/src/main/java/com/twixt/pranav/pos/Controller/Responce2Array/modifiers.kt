package com.twixt.pranav.pos.Controller.Responce2Array

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/26/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("modifier_name",
        "amount")
class modifiers {

    @JsonProperty("modifier_name")
    @get:JsonProperty("modifier_name")
    var modifier_name: String? = null

    @JsonProperty("amount")
    @get:JsonProperty("amount")
    var amount: String? = null

}