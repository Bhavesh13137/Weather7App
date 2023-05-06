package com.weatherapp.repository

import com.weatherapp.data.api_helper.ApiHelper
import com.weatherapp.data.waether_model.WeatherResponse
import io.reactivex.Observable

class WeatherRepository(private val apiHelper: ApiHelper) {
//    fun fetchWeather(param1: String,param2: String,param3: String,param4: String) : Observable<WeatherResponse> {
//        return apiHelper.fetchWeather(param1,param2,param3,param4)
//    }

    fun fetchWeather(param1: String,param2: String,param3: String,param4: String,param5: String) : Observable<WeatherResponse> {
        return apiHelper.fetchWeather(param1,param2,param3,param4,param5)
    }
}