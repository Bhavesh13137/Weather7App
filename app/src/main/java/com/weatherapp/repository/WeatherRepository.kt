package com.weatherapp.repository

import com.weatherapp.data.api_helper.ApiHelper

class WeatherRepository(private val apiHelper: ApiHelper) {
    suspend fun fetchWeather(param1: String,param2: String,param3: String,param4: String) =  apiHelper.fetchWeather(param1,param2,param3,param4)
}