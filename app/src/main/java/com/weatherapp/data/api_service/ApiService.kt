package com.weatherapp.data.api_service

import com.weatherapp.data.model.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast")
    fun fetchWeather(@Query("q") q: String, @Query("lat") lat : String, @Query("lon") lon : String, @Query("APPID") APPID : String, @Query("cnt") day : String = ""): Observable<WeatherResponse>
}