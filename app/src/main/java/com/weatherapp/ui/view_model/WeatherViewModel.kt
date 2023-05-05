package com.weatherapp.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weatherapp.data.model.WeatherResponse
import com.weatherapp.repository.WeatherRepository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _list : MutableLiveData<WeatherResponse> = MutableLiveData<WeatherResponse>()
    val list : LiveData<WeatherResponse>
    get() = _list

    fun fetchWeather(param1: String, param2: String, param3: String, param4: String) {
            repository.fetchWeather(param1, param2, param3, param4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WeatherResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }

                    override fun onNext(t: WeatherResponse) {
                        _list.postValue(t)
                    }

                })
    }

}