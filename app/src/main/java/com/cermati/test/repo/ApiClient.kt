package com.cermati.test.repo

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by siapaSAYA on 7/18/2020
 */


class ApiClient {

    companion object {

        private val TAG = ApiClient::class.java.simpleName
        private var retrofit: Retrofit? = null
        private val REQUEST_TIMEOUT = 60
        private var okHttpClient: OkHttpClient? = null
        @JvmStatic
        fun GetClient(baseUrl : String): Retrofit? {

            if (okHttpClient == null)
                initOkHttp()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
            }
            return retrofit
        }

        private fun initOkHttp() {
            val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)

            okHttpClient = httpClient.build()
        }

        fun resetApiClient() {
            retrofit = null
            okHttpClient = null
        }
    }

}