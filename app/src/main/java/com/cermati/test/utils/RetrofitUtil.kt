package com.cermati.test.utils

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by siapaSAYA on 7/18/2020
 */

object RetrofitUtil{
    /**
     *
     * @param baseUrl is a base url use the service request
     * @param timeOutMilisecond is a client timout
     * @param service is a class of retrofit service
     * @param <T> like a service generyc type of interface class will return
     * @return a service
    </T> */
    @JvmStatic
    fun <T> createService(
        baseUrl: String,
        timeOutMilisecond: Long,
        service: Class<T>
    ): T {
        val builder = OkHttpClient.Builder()
        if (timeOutMilisecond > 0) {
            builder.readTimeout(timeOutMilisecond, TimeUnit.MILLISECONDS)
            builder.connectTimeout(timeOutMilisecond, TimeUnit.MILLISECONDS)
            builder.writeTimeout(timeOutMilisecond, TimeUnit.MILLISECONDS)
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(builder.build())
            .build()
            .create(service)
    }

    /**
     *
     * @param baseUrl is a base url use the service request
     * @param loggingLevel is a logging interceptor level [BODY] or [HEADER]
     * @param timeOutMilisecond is a client timout
     * @param service is a interface class of retrofit service
     * @param <T> like a service generyc type of interface class will return
     * @return a service
    </T> */
    @JvmStatic
    fun <T> createServiceWithLoggingIntercpetor(
        baseUrl: String,
        loggingLevel: HttpLoggingInterceptor.Level,
        timeOutMilisecond: Long,
        service: Class<T>
    ): T {
        val builder = OkHttpClient.Builder()
        if (timeOutMilisecond > 0) {
            builder.readTimeout(timeOutMilisecond, TimeUnit.MILLISECONDS)
            builder.connectTimeout(timeOutMilisecond, TimeUnit.MILLISECONDS)
            builder.writeTimeout(timeOutMilisecond, TimeUnit.MILLISECONDS)
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = loggingLevel
        builder.addInterceptor(interceptor)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(builder.build())
            .build()
            .create(service)
    }
}