package com.litmethod.android.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class ServiceBuilder {
    companion object {

        val myApi: IApi by lazy {
            return@lazy getRetrofit().create(IApi::class.java)
        }
        @Volatile
        private var INSTANCE: Retrofit? = null

        private fun getRetrofit(): Retrofit {

            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl("https://api.litmethod.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.connectTimeout(90, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .addInterceptor(interceptor).addInterceptor(
                            Interceptor { chain ->
                                val request: Request =
                                    chain.request().newBuilder().addHeader("AUTHTOKEN", "7ffb6c186797e9075fce5876f61af24e").addHeader("Accept","application/json")
                                        .build()
                                chain.proceed(request)
                            }).build())

                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}