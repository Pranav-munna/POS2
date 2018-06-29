package com.twixt.pranav.pos.Controller

import com.twixt.pranav.pos.Controller.Responce.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Pranav on 11/22/2017.
 */
interface POSInterphase {

    /* @GET("aps_api/get_companydetails_arabic/{id}")
     fun getDetailsarab(@Path("id") id: String): Call<DetailsResponse>
 */

    @FormUrlEncoded
    @POST("api/verifyApp")
    fun getActivated(@Field("appId") appid: String,
                     @Field("key") key: String): Call<ResponceActivateStatus>

    @FormUrlEncoded
    @POST("api/headerFooter")
    fun getHeaderFooter(@Field("shopId") appid: String): Call<ResponceHeaderFooter>

    @FormUrlEncoded
    @POST("api/backEndChange")
    fun getBackEnd(@Field("userId") appid: String): Call<ResponceBackEndChange>

    //    @FormUrlEncoded
    @GET("api/checkNetwork")
    fun getstatus(): Call<ResponceActivateStatus>

    @FormUrlEncoded
    @POST("api/emailReciept")
    fun getReceiptsEmail(@Field("email") email: String, @Field("orderId") orderId: String,
                         @Field("shopId") shopId: String): Call<ResponceActivateStatus>

    @FormUrlEncoded
    @POST("api/applogin")
    fun getLogin(@Field("email") email: String,
                 @Field("password") pass: String,
                 @Field("firebaseId") token: String): Call<ResponceLogin>

    @FormUrlEncoded
    @POST("api/categories/7")
    fun getCategories(@Field("shopId") shopId: String,
                      @Query("page") page: Int): Call<Array<ResponceCategories>>

    @FormUrlEncoded
    @POST("api/allmodifiers/7")
    fun getModifiers(@Field("shopId") shopId: String,
                     @Query("page") page: Int): Call<Array<ResponceModifier>>

    @FormUrlEncoded
    @POST("api/payLaterReciepts/7")
    fun getPayLaterMail(@Field("shopId") shopId: String,
                        @Query("page") page: Int): Call<Array<ResponcePayLaterMails>>

    @FormUrlEncoded
    @POST("api/searchCategory")
    fun getCategoriesSearch(@Field("shopId") shopId: String,
                            @Field("search_key") key: String): Call<Array<ResponceCategories>>

    @FormUrlEncoded
    @POST("api/items/7")
    fun getProducts(@Field("categoryId") id: String,
                    @Query("page") page: Int): Call<Array<ResponceProducts>>

    @FormUrlEncoded
    @POST("api/allitems/7")
    fun getAllProducts(@Field("shopId") id: String,
                       @Query("page") page: Int): Call<Array<ResponceProducts>>

    @FormUrlEncoded
    @POST("api/payLaterRecieptDetails")
    fun getPayLaterRecieptDetails(@Field("shopId") shopId: String,
                                  @Query("id") id: String): Call<Array<ResponcePayLaterRecieptDetails>>

    @FormUrlEncoded
    @POST("api/paymenttype")
    fun getPaymentTypes(@Field("shopId") id: String): Call<Array<ResponcePaymentTypes>>

    @FormUrlEncoded
    @POST("api/charge")
    fun pay(@Field("shopId") shopId: String,
            @Field("userId") userId: String,
            @Field("total") total: String,
            @Field("discount") discount: String,
            @Field("discountAmount") discountAmount: String,
            @Field("payableAmount") payableAmount: String,
            @Field("discountType") discountType: String,
            @Field("paymentType") paymentType: String,
            @Field("items") items: String): Call<ResponcePay>

    @FormUrlEncoded
    @POST("api/completePayLater")
    fun isPayed(@Field("shopId") shopId: String,
                @Field("userId") userId: String,
                @Field("total") total: String,
                @Field("discount") discount: String,
                @Field("discountAmount") discountAmount: String,
                @Field("payableAmount") payableAmount: String,
                @Field("discountType") discountType: String,
                @Field("paymentType") paymentType: String,
                @Field("orderId") items: String): Call<ResponcePayLater>

    @FormUrlEncoded
    @POST("api/payLater")
    fun paylater(@Field("shopId") shopId: String,
                 @Field("userId") userId: String,
                 @Field("total") total: String,
                 @Field("discount") discount: String,
                 @Field("discountAmount") discountAmount: String,
                 @Field("payableAmount") payableAmount: String,
                 @Field("discountType") discountType: String,
                 @Field("paymentType") paymentType: String,
                 @Field("items") items: String,
                 @Field("paylaterId") email: String,
                 @Field("print") print: String): Call<ResponcePay>


    @FormUrlEncoded
    @POST("api/splitcharge")
    fun paysplit(@Field("shopId") shopId: String,
                 @Field("userId") userId: String,
                 @Field("total") total: String,
                 @Field("discount") discount: String,
                 @Field("discountAmount") discountAmount: String,
                 @Field("payableAmount") payableAmount: String,
                 @Field("discountType") discountType: String,
                 @Field("paymentType") paymentType: String,
                 @Field("items") items: String,
                 @Field("splitDetails") splitDetails: String): Call<ResponcePay>

    @FormUrlEncoded
    @POST("api/reciepts/7")
    fun getReceipts(@Field("shopId") shopId: String,
                    @Query("page") page: Int): Call<Array<ResponceReceipts>>

    @FormUrlEncoded
    @POST("api/searchreceipts")
    fun getReceiptSearch(@Field("shopId") shopId: String,
                         @Field("orderId") orderId: String): Call<Array<ResponceReceipts>>

    @FormUrlEncoded
    @POST("api/searchreceiptsbydate/7")
    fun getReceiptSearchDate(@Field("shopId") shopId: String,
                             @Field("date") date: String,
                             @Query("page") page: Int): Call<Array<ResponceReceipts>>

    @FormUrlEncoded
    @POST("api/refundOrder")
    fun refund(@Field("orderId") orderId: String,
               @Field("userId") userId: String): Call<ResponceRefund>


    @FormUrlEncoded
    @POST("api/deletepaylater")
    fun getDeletePayLater(@Field("orderId") orderId: String,
                          @Field("items") items: String): Call<ResponceActivateStatus>

    @FormUrlEncoded
    @POST("api/refundProducts")
    fun refunditems(@Field("orderId") orderId: String,
                    @Field("userId") userId: String,
                    @Field("products") products: String,
                    @Field("shopId") shopId: String,
                    @Field("payableAmount") payableAmount: String,
                    @Field("paymentType") paymentType: String): Call<ResponceActivateStatus>

//    fun checkLanguage(userId: String): Any Call<ResponceLanguageCheck>

    @FormUrlEncoded
    @POST("api/lang")
    fun checkLanguage(@Field("userId") userId: String): Call<ResponceLanguageCheck>

//    fun getLanguage(userId: String): Any {}

    @FormUrlEncoded
    @POST("api/langContent")
    fun getLanguage(@Field("userId") userId: String): Call<ResponceLanguage>


    @FormUrlEncoded
    @POST("api/discounts")
    fun getDescount(@Field("shopId") id: String): Call<Array<ResponceDiscounts>>


}