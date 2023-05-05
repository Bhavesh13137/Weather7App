package com.weatherapp.data.network

import com.weatherapp.data.api_service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitBuilder {

    private fun getRetrofit() : Retrofit {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS).readTimeout(20,
                TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    val apiService : ApiService = getRetrofit().create(ApiService::class.java)
}