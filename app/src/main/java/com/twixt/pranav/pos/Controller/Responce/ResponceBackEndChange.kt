package com.twixt.pranav.pos.Controller.Responce

/**
 * Created by Pranav on 12/8/2017.
 */

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "backend_change"
)
class ResponceBackEndChange {

    @JsonProperty("backend_change")
    @get:JsonProperty("backend_change")
    var backend_change: String? = null
}
