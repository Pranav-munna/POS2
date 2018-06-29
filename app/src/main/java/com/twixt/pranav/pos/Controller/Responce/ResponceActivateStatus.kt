package com.twixt.pranav.pos.Controller.Responce

/**
 * Created by Pranav on 12/8/2017.
 */

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "status"
)
class ResponceActivateStatus {

    @JsonProperty("status")
    @get:JsonProperty("status")
    var status: String? = null
}
