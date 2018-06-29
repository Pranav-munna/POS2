package com.twixt.pranav.pos.Controller.Responce2Array

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder


/**
 * Created by Pranav on 1/1/2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id",
        "orderId",
        "itemId",
        "quantity",
        "itemName",
        "price",
        "tax",
        "deleted_at",
        "created_at",
        "updated_at",
        "sellingType",
        "modifiers")

class RefundedItems {
    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: Int? = null

    @JsonProperty("orderId")
    @get:JsonProperty("orderId")
    var orderId: Int? = null

    @JsonProperty("itemId")
    @get:JsonProperty("itemId")
    var itemId: Int? = null

    @JsonProperty("quantity")
    @get:JsonProperty("quantity")
    var quantity: String? = null

    @JsonProperty("itemName")
    @get:JsonProperty("itemName")
    var itemName: String? = null

    @JsonProperty("price")
    @get:JsonProperty("price")
    var price: Double? = null

    @JsonProperty("tax")
    @get:JsonProperty("tax")
    var tax: Double? = null

    @JsonProperty("deleted_at")
    @get:JsonProperty("deleted_at")
    var deletedAt: Any? = null

    @JsonProperty("created_at")
    @get:JsonProperty("created_at")
    var createdAt: String? = null

    @JsonProperty("updated_at")
    @get:JsonProperty("updated_at")
    var updatedAt: String? = null

    @JsonProperty("sellingType")
    @get:JsonProperty("sellingType")
    var sellingType: String? = null

    @JsonProperty("modifiers")
    @get:JsonProperty("modifiers")
    var modifiers: List<modifiers>? = null

    var count: String? = null

    /*@JsonProperty("id")
    fun getId(): Int? {
        return id
    }

    @JsonProperty("orderId")
    fun getOrderId(): Int? {
        return orderId
    }

    @JsonProperty("itemId")
    fun getItemId(): Int? {
        return itemId
    }

    @JsonProperty("quantity")
    fun getQuantity(): Int? {
        return quantity
    }

    @JsonProperty("itemName")
    fun getItemName(): String? {
        return itemName
    }

    @JsonProperty("price")
    fun getPrice(): Int? {
        return price
    }

    @JsonProperty("tax")
    fun getTax(): Int? {
        return tax
    }

    @JsonProperty("deleted_at")
    fun getDeletedAt(): Any? {
        return deletedAt
    }

    @JsonProperty("created_at")
    fun getCreatedAt(): String? {
        return createdAt
    }

    @JsonProperty("updated_at")
    fun getUpdatedAt(): String? {
        return updatedAt
    }
*/
}