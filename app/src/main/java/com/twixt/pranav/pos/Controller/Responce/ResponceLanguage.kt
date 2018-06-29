package com.twixt.pranav.pos.Controller.Responce

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Created by Pranav on 12/19/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("receipts",
        "refund",
        "sales",
        "settings",
        "cart",
        "payment",
        "categories",
        "favorites",
        "cashier",
        "total",
        "amount",
        "total_amount",
        "select_payment",
        "inclusive_of_taxes",
        "passcode",
        "cancel",
        "enter_your_pin",
        "quantity",
        "ok",
        "remaning_amount",
        "pay",
        "split",
        "charge",
        "rs",
        "check_network",
        "forgot_password",
        "enter_all_fields",
        "invalid_email",
        "password_error",
        "no_items",
        "offline_error",
        "email",
        "login",
        "appid",
        "key",
        "activate",
        "activation",
        "invalid_activation",
        "logout",
        "no_such_category",
        "payed",
        "select_payment_method",
        "enter_amount",
        "amount_too_heigh",
        "refund_unavailable",
        "selected",
        "added",
        "select_payment_toast",
        "applied",
        "invalidpin",
        "paymenttype",
        "offersapplied",
        "send",
        "paylater",
        "sendreciept",
        "orders",
        "items",
        "wait",
        "loading",
        "newsale",
        "allcategories",
        "all",
        "paylaterid",
        "print",
        "view",
        "printtest",
        "modifier",
        "delete_modifier",
        "discount",
        "invoice",
        "date",
        "item",
        "price",
        "refunded",
        "next",
        "noreciepts",
        "refundedReciept",
        "cash",
        "change",
        "deselectPayment",
        "userSelect",
        "refundedDiscount",
        "invalidEmail"
)

class ResponceLanguage {

    @JsonProperty("receipts")
    @get:JsonProperty("receipts")
    var receipts: String? = null

    @JsonProperty("refund")
    @get:JsonProperty("refund")
    var refund: String? = null

    @JsonProperty("sales")
    @get:JsonProperty("sales")
    var sales: String? = null

    @JsonProperty("settings")
    @get:JsonProperty("settings")
    var settings: String? = null

    @JsonProperty("cart")
    @get:JsonProperty("cart")
    var cart: String? = null

    @JsonProperty("payment")
    @get:JsonProperty("payment")
    var payment: String? = null

    @JsonProperty("categories")
    @get:JsonProperty("categories")
    var categories: String? = null


    @JsonProperty("favorites")
    @get:JsonProperty("favorites")
    var favorites: String? = null

    @JsonProperty("cashier")
    @get:JsonProperty("cashier")
    var cashier: String? = null

    @JsonProperty("total")
    @get:JsonProperty("total")
    var total: String? = null

    @JsonProperty("amount")
    @get:JsonProperty("amount")
    var amount: String? = null

    @JsonProperty("total_amount")
    @get:JsonProperty("total_amount")
    var total_amount: String? = null

    @JsonProperty("select_payment")
    @get:JsonProperty("select_payment")
    var select_payment: String? = null

    @JsonProperty("inclusive_of_taxes")
    @get:JsonProperty("inclusive_of_taxes")
    var inclusive_of_taxes: String? = null

    @JsonProperty("passcode")
    @get:JsonProperty("passcode")
    var passcode: String? = null

    @JsonProperty("cancel")
    @get:JsonProperty("cancel")
    var cancel: String? = null

    @JsonProperty("enter_your_pin")
    @get:JsonProperty("enter_your_pin")
    var enter_your_pin: String? = null

    @JsonProperty("quantity")
    @get:JsonProperty("quantity")
    var quantity: String? = null

    @JsonProperty("ok")
    @get:JsonProperty("ok")
    var ok: String? = null

    @JsonProperty("remaning_amount")
    @get:JsonProperty("remaning_amount")
    var remaning_amount: String? = null

    @JsonProperty("pay")
    @get:JsonProperty("pay")
    var pay: String? = null

    @JsonProperty("split")
    @get:JsonProperty("split")
    var split: String? = null

    @JsonProperty("charge")
    @get:JsonProperty("charge")
    var charge: String? = null


    @JsonProperty("rs")
    @get:JsonProperty("rs")
    var rs: String? = null

    @JsonProperty("check_network")
    @get:JsonProperty("check_network")
    var check_network: String? = null

    @JsonProperty("forgot_password")
    @get:JsonProperty("forgot_password")
    var forgot_password: String? = null

    @JsonProperty("enter_all_fields")
    @get:JsonProperty("enter_all_fields")
    var enter_all_fields: String? = null

    @JsonProperty("invalid_email")
    @get:JsonProperty("invalid_email")
    var invalid_email: String? = null

    @JsonProperty("password_error")
    @get:JsonProperty("password_error")
    var password_error: String? = null

    @JsonProperty("no_items")
    @get:JsonProperty("no_items")
    var no_items: String? = null

    @JsonProperty("offline_error")
    @get:JsonProperty("offline_error")
    var offline_error: String? = null

    @JsonProperty("email")
    @get:JsonProperty("email")
    var email: String? = null

    @JsonProperty("login")
    @get:JsonProperty("login")
    var login: String? = null

    @JsonProperty("appid")
    @get:JsonProperty("appid")
    var appid: String? = null

    @JsonProperty("key")
    @get:JsonProperty("key")
    var key: String? = null

    @JsonProperty("activate")
    @get:JsonProperty("activate")
    var activate: String? = null

    @JsonProperty("activation")
    @get:JsonProperty("activation")
    var activation: String? = null

    @JsonProperty("invalid_activation")
    @get:JsonProperty("invalid_activation")
    var invalid_activation: String? = null

    @JsonProperty("logout")
    @get:JsonProperty("logout")
    var logout: String? = null

    @JsonProperty("no_such_category")
    @get:JsonProperty("no_such_category")
    var no_such_category: String? = null

    @JsonProperty("payed")
    @get:JsonProperty("payed")
    var payed: String? = null

    @JsonProperty("select_payment_method")
    @get:JsonProperty("select_payment_method")
    var select_payment_method: String? = null

    @JsonProperty("enter_amount")
    @get:JsonProperty("enter_amount")
    var enter_amount: String? = null

    @JsonProperty("amount_too_heigh")
    @get:JsonProperty("amount_too_heigh")
    var amount_too_heigh: String? = null

    @JsonProperty("refund_unavailable")
    @get:JsonProperty("refund_unavailable")
    var refund_unavailable: String? = null

    @JsonProperty("selected")
    @get:JsonProperty("selected")
    var selected: String? = null

    @JsonProperty("added")
    @get:JsonProperty("added")
    var added: String? = null

    @JsonProperty("select_payment_toast")
    @get:JsonProperty("select_payment_toast")
    var select_payment_toast: String? = null

    @JsonProperty("applied")
    @get:JsonProperty("applied")
    var applied: String? = null

    @JsonProperty("invalidpin")
    @get:JsonProperty("invalidpin")
    var invalidpin: String? = null

    @JsonProperty("paymenttype")
    @get:JsonProperty("paymenttype")
    var paymenttype: String? = null

    @JsonProperty("offersapplied")
    @get:JsonProperty("offersapplied")
    var offersapplied: String? = null

    @JsonProperty("send")
    @get:JsonProperty("send")
    var send: String? = null

    @JsonProperty("paylater")
    @get:JsonProperty("paylater")
    var paylater: String? = null

    @JsonProperty("sendreciept")
    @get:JsonProperty("sendreciept")
    var sendreciept: String? = null

    @JsonProperty("orders")
    @get:JsonProperty("orders")
    var orders: String? = null

    @JsonProperty("items")
    @get:JsonProperty("items")
    var items: String? = null

    @JsonProperty("wait")
    @get:JsonProperty("wait")
    var wait: String? = null

    @JsonProperty("loading")
    @get:JsonProperty("loading")
    var loading: String? = null

    @JsonProperty("newsale")
    @get:JsonProperty("newsale")
    var newsale: String? = null

    @JsonProperty("allcategories")
    @get:JsonProperty("allcategories")
    var allcategories: String? = null

    @JsonProperty("all")
    @get:JsonProperty("all")
    var all: String? = null

    @JsonProperty("paylaterid")
    @get:JsonProperty("paylaterid")
    var paylaterid: String? = null

    @JsonProperty("print")
    @get:JsonProperty("print")
    var print: String? = null

    @JsonProperty("view")
    @get:JsonProperty("view")
    var viewz: String? = null

    @JsonProperty("printtest")
    @get:JsonProperty("printtest")
    var printtest: String? = null

    @JsonProperty("modifier")
    @get:JsonProperty("modifier")
    var modifier: String? = null

    @JsonProperty("delete_modifier")
    @get:JsonProperty("delete_modifier")
    var delete_modifier: String? = null

    @JsonProperty("discount")
    @get:JsonProperty("discount")
    var discount: String? = null

    @JsonProperty("invoice")
    @get:JsonProperty("invoice")
    var invoice: String? = null

    @JsonProperty("date")
    @get:JsonProperty("date")
    var date: String? = null

    @JsonProperty("item")
    @get:JsonProperty("item")
    var item: String? = null

    @JsonProperty("price")
    @get:JsonProperty("price")
    var price: String? = null

    @JsonProperty("refunded")
    @get:JsonProperty("refunded")
    var refunded: String? = null

    @JsonProperty("next")
    @get:JsonProperty("next")
    var next: String? = null

    @JsonProperty("noreciepts")
    @get:JsonProperty("noreciepts")
    var noreciepts: String? = null

    @JsonProperty("refundedReciept")
    @get:JsonProperty("refundedReciept")
    var refundedReciept: String? = null


    @JsonProperty("cash")
    @get:JsonProperty("cash")
    var cash: String? = null

    @JsonProperty("change")
    @get:JsonProperty("change")
    var change: String? = null

    @JsonProperty("deselectPayment")
    @get:JsonProperty("deselectPayment")
    var deselectPayment: String? = null

    @JsonProperty("userSelect")
    @get:JsonProperty("userSelect")
    var userSelect: String? = null

    @JsonProperty("refundedDiscount")
    @get:JsonProperty("refundedDiscount")
    var refundedDiscount: String? = null

@JsonProperty("invalidEmail")
    @get:JsonProperty("invalidEmail")
    var invalidEmail: String? = null


}