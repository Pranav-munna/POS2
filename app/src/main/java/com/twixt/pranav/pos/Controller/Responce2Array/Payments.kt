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
        "shopId",
        "paymentTypeId",
        "amount",
        "returnStatus",
        "deleted_at",
        "created_at",
        "updated_at")
class Payments {
    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: Int? = null

    @JsonProperty("orderId")
    @get:JsonProperty("orderId")
    var orderId: Int? = null

    @JsonProperty("paymentTypeId")
    @get:JsonProperty("paymentTypeId")
    var paymentTypeId: Int? = null

    @JsonProperty("shopId")
    @get:JsonProperty("shopId")
    var shopId: Int? = null

    @JsonProperty("amount")
    @get:JsonProperty("amount")
    var amount: Double? = null

    @JsonProperty("returnStatus")
    @get:JsonProperty("returnStatus")
    var returnStatus: Int? = null

    @JsonProperty("deleted_at")
    @get:JsonProperty("deleted_at")
    var deletedAt: String? = null

    @JsonProperty("created_at")
    @get:JsonProperty("created_at")
    var createdAt: String? = null

    @JsonProperty("updated_at")
    @get:JsonProperty("updated_at")
    var updatedAt: String? = null


/* @JsonProperty("id")
 fun getId(): Int? {
     return id
 }

 @JsonProperty("orderId")
 fun getOrderId(): Int? {
     return orderId
 }

 @JsonProperty("paymentTypeId")
 fun getPaymentTypeId(): Int? {
     return paymentTypeId
 }

 @JsonProperty("amount")
 fun getAmount(): Int? {
     return amount
 }

 @JsonProperty("returnStatus")
 fun getReturnStatus(): Int? {
     return returnStatus
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