package com.twixt.pranav.pos.Controller.Responce

/**
 * Created by Pranav on 12/6/2017.
 */


class ResponcePaymentSplit {
    var i = 0
    var s = "select"

    fun set(value: String, pos: Int) {
        s = value
        i = pos
    }

}


/*import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("company_id", "company", "area", "phone", "fax", "pobox")
class ListResponce {

    @JsonProperty("company_id")
    @get:JsonProperty("company_id")
    val companyId: String? = null
    @JsonProperty("company")
    @get:JsonProperty("company")
    val company: String? = null
    @JsonProperty("area")
    @get:JsonProperty("area")
    val area: String? = null
    @JsonProperty("phone")
    @get:JsonProperty("phone")
    val phone: String? = null
    @JsonProperty("fax")
    @get:JsonProperty("fax")
    val fax: String? = null
    @JsonProperty("pobox")
    @get:JsonProperty("pobox")
    val pobox: String? = null


}*/