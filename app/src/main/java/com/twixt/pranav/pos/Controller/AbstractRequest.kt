package com.twixt.pranav.pos.Controller

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Pranav on 11/22/2017.
 */
abstract class AbstractRequest<T>(protected var context: Context, private val responcehandler: ProcessResponcceInterphase<T>?) : Callback<T> {
    protected  var pOSInterphase: POSInterphase
    private var base_url = "http://kasy.dk/"

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .client(OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor)
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
        pOSInterphase=retrofit.create(POSInterphase::class.java)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.e("response", "  ok")
        processResponse(response.body())
    }

    override fun onFailure(call: Call<T>, t: Throwable?) {
        Log.e("response ", t!!.message + "  poe")
        processResponse(null)
    }

    private fun processResponse(response: T?) {
        if (response != null) {
            responcehandler?.processResponce(response)
        }
    }
}