package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.twixt.pranav.pos.Controller.Responce2Array.OrderItem
import com.twixt.pranav.pos.Controller.Responce2Array.Payments
import com.twixt.pranav.pos.Controller.Responce2Array.RefundedItems


/**
 * Created by Pranav on 1/1/2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id",
        "shopId",
        "userId",
        "total",
        "discountType",
        "discount",
        "discountAmount",
        "paymentType",
        "payableAmount",
        "paidAs",
        "deleted_at",
        "created_at",
        "updated_at",
        "payments",
        "totalTax",
        "refunded_discount",
        "order_items",
        "refunded_items"
        )
class ResponceRefund {

    @JsonProperty("id")
    @get:JsonProperty("id")
    var id: Int? = null

    @JsonProperty("shopId")
    @get:JsonProperty("shopId")
    var shopId: Int? = null

    @JsonProperty("userId")
    @get:JsonProperty("userId")
    var userId: Int? = null

    @JsonProperty("total")
    @get:JsonProperty("total")
    var total: Double? = null

    @JsonProperty("discountType")
    @get:JsonProperty("discountType")
    var discountType: String? = null

    @JsonProperty("discount")
    @get:JsonProperty("discount")
    var discount: Double? = null

    @JsonProperty("discountAmount")
    @get:JsonProperty("discountAmount")
    var discountAmount: Double? = null

    @JsonProperty("paymentType")
    @get:JsonProperty("paymentType")
    var paymentType: Int? = null

    @JsonProperty("payableAmount")
    @get:JsonProperty("payableAmount")
    var payableAmount: Double? = null

    @JsonProperty("paidAs")
    @get:JsonProperty("paidAs")
    var paidAs: String? = null

    @JsonProperty("deleted_at")
    @get:JsonProperty("deleted_at")
    var deletedAt: String? = null

    @JsonProperty("created_at")
    @get:JsonProperty("created_at")
    var createdAt: String? = null

    @JsonProperty("updated_at")
    @get:JsonProperty("updated_at")
    var updatedAt: String? = null

    @JsonProperty("payments")
    @get:JsonProperty("payments")
    var payments: List<Payments>? = null

    @JsonProperty("totalTax")
    @get:JsonProperty("totalTax")
    var totalTax: String? = null

    @JsonProperty("refunded_discount")
    @get:JsonProperty("refunded_discount")
    var refunded_discount: String? = null

    @JsonProperty("order_items")
    @get:JsonProperty("order_items")
    var orderItems: List<OrderItem>? = null

    @JsonProperty("refunded_items")
    @get:JsonProperty("refunded_items")
    var refunded_items: List<RefundedItems>? = null



    /* @JsonProperty("order_items")
     fun getOrderItems(): List<OrderItem>? {
         return orderItems
     }

     @JsonProperty("shopId")
     fun getShopId(): Int? {
         return shopId
     }

     @JsonProperty("userId")
     fun getUserId(): Int? {
         return userId
     }

     @JsonProperty("total")
     fun getTotal(): Int? {
         return total
     }

     @JsonProperty("discountType")
     fun getDiscountType(): String? {
         return discountType
     }

     @JsonProperty("discount")
     fun getDiscount(): Int? {
         return discount
     }

     @JsonProperty("discountAmount")
     fun getDiscountAmount(): Int? {
         return discountAmount
     }

     @JsonProperty("paymentType")
     fun getPaymentType(): Int? {
         return paymentType
     }

     @JsonProperty("payableAmount")
     fun getPayableAmount(): Int? {
         return payableAmount
     }

     @JsonProperty("paidAs")
     fun getPaidAs(): String? {
         return paidAs
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

     @JsonProperty("payments")
     fun getPayments(): List<Payment>? {
         return payments
     }*/


}