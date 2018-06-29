package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/12/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id",
        "shopId",
        "categoryId",
        "sku",
        "name",
        "image",
        "color",
        "shape",
        "taxId",
        "price",
        "sellingType",
        "trackStock",
        "stock",
        "tax"
)

class ResponceProducts {

    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: Int? = null

    @JsonProperty("shopId")
    @get:JsonProperty("shopId")
    var shopId: Int? = null

    @JsonProperty("categoryId")
    @get:JsonProperty("categoryId")
    var categoryId: Int? = null


    @JsonProperty("sku")
    @get:JsonProperty("sku")
    var sku: String? = null

    @JsonProperty("name")
    @get:JsonProperty("name")
    var name: String? = null

    @JsonProperty("image")
    @get:JsonProperty("image")
    var image: String? = null

    @JsonProperty("color")
    @get:JsonProperty("color")
    var color: String? = null

    @JsonProperty("shape")
    @get:JsonProperty("shape")
    var shape: String? = null

    @JsonProperty("taxId")
    @get:JsonProperty("taxId")
    var taxId: Int? = null

    @JsonProperty("price")
    @get:JsonProperty("price")
    var price: Double? = null

    @JsonProperty("sellingType")
    @get:JsonProperty("sellingType")
    var sellingType: Int? = null

    @JsonProperty("trackStock")
    @get:JsonProperty("trackStock")
    var trackStock: Int? = null

    @JsonProperty("stock")
    @get:JsonProperty("stock")
    var stock: Int? = null

    @JsonProperty("tax")
    @get:JsonProperty("tax")
    var tax: String? = null

}