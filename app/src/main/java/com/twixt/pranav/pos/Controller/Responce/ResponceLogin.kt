package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/11/2017.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id",
        "name",
        "email",
        "shopId",
        "role",
        "profileImage",
        "pincode",
        "address",
        "phone",
        "status",
        "offlineAllowed",
        "businessName",
        "cvr",
        "header",
        "footer",
        "users"
)
class ResponceLogin {

    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: String? = null

    @JsonProperty("name")
    @get:JsonProperty("name")
    var name: String? = null

    @JsonProperty("email")
    @get:JsonProperty("email")
    var email: String? = null

    @JsonProperty("shopId")
    @get:JsonProperty("shopId")
    var shopId: String? = null

    @JsonProperty("role")
    @get:JsonProperty("role")
    var role: String? = null

    @JsonProperty("profileImage")
    @get:JsonProperty("profileImage")
    var profileImage: String? = null

    @JsonProperty("pincode")
    @get:JsonProperty("pincode")
    var pincode: String? = null

    @JsonProperty("address")
    @get:JsonProperty("address")
    var address: String? = null

    @JsonProperty("phone")
    @get:JsonProperty("phone")
    var phone: String? = null

    @JsonProperty("status")
    @get:JsonProperty("status")
    var status: String? = null

    @JsonProperty("offlineAllowed")
    @get:JsonProperty("offlineAllowed")
    var offlineAllowed: String? = null

    @JsonProperty("businessName")
    @get:JsonProperty("businessName")
    var businessName: String? = null

    @JsonProperty("cvr")
    @get:JsonProperty("cvr")
    var cvr: String? = null

    @JsonProperty("header")
    @get:JsonProperty("header")
    var header: String? = null

    @JsonProperty("footer")
    @get:JsonProperty("footer")
    var footer: String? = null

    @JsonProperty("users")
    @get:JsonProperty("users")
    var users: String? = null

}