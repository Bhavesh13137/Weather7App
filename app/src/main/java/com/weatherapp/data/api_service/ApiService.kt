package com.weatherapp.data.api_service

import com.weatherapp.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun fetchWeather(@Query("q") q: String, @Query("lat") lat : String,@Query("lon") lon : String,@Query("APPID") APPID : String): Response
}