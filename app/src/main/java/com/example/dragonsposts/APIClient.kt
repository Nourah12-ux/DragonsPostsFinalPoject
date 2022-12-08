package com.example.dragonsposts

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    var retrofit: Retrofit? = null
    fun getClient(): Retrofit? {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val gsonBuilder = GsonBuilder().setLenient().create()


        retrofit = Retrofit.Builder()
            .baseUrl("https://apidojo.pythonanywhere.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()

        return retrofit
    }

}