package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/11/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("header",
        "footer")

class ResponceHeaderFooter {

    @JsonProperty("header")
    @get:JsonProperty("header")
    var header: String? = null

    @JsonProperty("footer")
    @get:JsonProperty("footer")
    var footer: String? = null
}