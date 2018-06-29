package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/11/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("itemId",
        "modifier_id",
        "modifier_name",
        "option_id",
        "option_name",
        "modifier_price")

class ResponceModifier {

    @JsonProperty("itemId")
    @get:JsonProperty("itemId")
    var itemId: Int? = null

    @JsonProperty("modifier_id")
    @get:JsonProperty("modifier_id")
    var modifier_id: Int? = null

    @JsonProperty("modifier_name")
    @get:JsonProperty("modifier_name")
    var modifier_name: String? = null


    @JsonProperty("option_id")
    @get:JsonProperty("option_id")
    var option_id: Int? = null

    @JsonProperty("option_name")
    @get:JsonProperty("option_name")
    var option_name: String? = null

    @JsonProperty("modifier_price")
    @get:JsonProperty("modifier_price")
    var modifier_price: String? = null

}