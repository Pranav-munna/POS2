package com.twixt.pranav.pos.Controller

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Pranav on 11/15/2017.
 */
class SharedPreferenceHelper(val context: Context) {

    var USER_LOGIN = "user_login"

    var PIN = "pin"
    var OFFLINE = "off_line"
    var POS_USER_ID = "user_id"
    var POS_USER_NAME = "user_name"
    var POS_USER_MAIL = "user_email"
    var POS_SHOP_ID = "shop_id"
    var POS_ROLE = "role"
    var POS_USER_IMAGE = "user_image"
    var CART_COUNT = "cart_count"
    var ACTIVATION_OK = "activation_ok"
    var LOGIN_OK = "Login_ok"
    var TOTAL_AMOUNT = "Total_amount"
    var PAYMENT_TYPE = "Payment_type"
    var PAY_COUNT = "Pay_count"
    var LANGUAGE = "Language"
    var NETWORK_PAY = "network_pay"
    var PAY_LATER = "pay_later"
    var NETWORK_SPLIT_PAY = "network_pay_split"
    var TAB = "tab"
    val ADDRESS="address"
    val PHONE="Phone"
    val CVR="Cvr"
    val CASH="cash"
    val BUSINESS_NAME="business_name"

    val HEADER="header"
    val FOOTER="footer"

    val sharedpreference: SharedPreferences = context.getSharedPreferences("sp_pos", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedpreference.edit()

    fun putString(key: String, data: String) {
        editor.putString(key, data)
        editor.commit()
    }

    fun getString(key: String, data: String): String {
        return sharedpreference.getString(key, data)
    }

    fun putInt(key: String, data: Int) {
        editor.putInt(key, data)
        editor.commit()
    }

    fun getInt(key: String, data: Int): Int {
        return sharedpreference.getInt(key, data)
    }



}