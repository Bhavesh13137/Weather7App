package com.weatherapp.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.weatherapp.data.network.Resource
import com.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    fun fetchWeather(param1 : String, param2 : String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.fetchWeather(param1,param2),"No Data Found"))
        } catch (e: HttpException) {
            emit(Resource.error(data = null, message = e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e : IOException){
            emit(Resource.error(data = null, message = "Couldn't reach server. check your internet connection."))
        }
    }



}