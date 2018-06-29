package com.twixt.pranav.pos.Controller.Responce2Array

/**
 * Created by Pranav on 12/21/2017.
 */

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "shopId", "taxLabel", "taxValue", "taxType")
class Tax {

    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: Int? = null

    @JsonProperty("shopId")
    @get:JsonProperty("shopId")
    var shopId: Int? = null

    @JsonProperty("taxLabel")
    @get:JsonProperty("taxLabel")
    var taxLabel: String? = null

    @JsonProperty("taxValue")
    @get:JsonProperty("taxValue")
    var taxValue: Int? = null

    @JsonProperty("taxType")
    @get:JsonProperty("taxType")
    var taxType: Int? = null

}
