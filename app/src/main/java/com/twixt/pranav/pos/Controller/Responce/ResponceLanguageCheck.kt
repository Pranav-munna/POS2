package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import retrofit2.http.GET

/**
 * Created by Pranav on 12/19/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("defaultLanguage")
class ResponceLanguageCheck {

    @JsonProperty("defaultLanguage")
    @get:JsonProperty("defaultLanguage")
    var defaultLanguage: String? = null
}