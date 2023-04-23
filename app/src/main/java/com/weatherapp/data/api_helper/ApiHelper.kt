package com.weatherapp.data.api_helper

import com.weatherapp.data.api_service.ApiService

class ApiHelper(private val apiService: ApiService) {
    suspend fun fetchWeather(param1: String,param2: String,param3: String,param4: String) =  apiService.fetchWeather(param1,param2,param3,param4)
}