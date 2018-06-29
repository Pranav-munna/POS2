package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/18/2017.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id",
        "name",
        "type")
class ResponcePaymentTypes {

    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: String? = null

    @JsonProperty("name")
    @get:JsonProperty("name")
    var name: String? = null

    @JsonProperty("type")
    @get:JsonProperty("type")
    var type: String? = null

}