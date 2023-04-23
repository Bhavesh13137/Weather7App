package com.weatherapp.repository

import com.weatherapp.data.api_helper.ApiHelper

class WeatherRepository(private val apiHelper: ApiHelper) {
    suspend fun fetchWeather(param1: String,param2: String) =  apiHelper.fetchWeather(param1,param2)
}