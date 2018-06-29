package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/18/2017.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "label",
        "discount",
        "discountType")
class ResponceDiscounts {

    @JsonProperty("label")
    @get:JsonProperty("label")
    var label: String? = null

    @JsonProperty("discount")
    @get:JsonProperty("discount")
    var discount: Int? = null

    @JsonProperty("discountType")
    @get:JsonProperty("discountType")
    var discountType: Int? = null
}