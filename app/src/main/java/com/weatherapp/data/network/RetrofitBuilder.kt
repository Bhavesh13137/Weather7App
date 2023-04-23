package com.weatherapp.data.network

import com.weatherapp.data.api_service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private fun getRetrofit() : Retrofit {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS).readTimeout(20,
                TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()

    }

    val apiService : ApiService = getRetrofit().create(ApiService::class.java)
}