package com.weatherapp.data.api_helper

import com.weatherapp.data.api_service.ApiService
import com.weatherapp.data.model.WeatherResponse
import io.reactivex.Observable

class ApiHelper(private val apiService: ApiService) {
    fun fetchWeather(param1: String,param2: String,param3: String,param4: String) : Observable<WeatherResponse> {
        return apiService.fetchWeather(param1,param2,param3,param4)
    }
}