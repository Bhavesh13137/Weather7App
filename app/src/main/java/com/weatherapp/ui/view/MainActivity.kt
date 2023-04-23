package com.weatherapp.ui.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.weatherapp.R
import com.weatherapp.data.api_helper.ApiHelper
import com.weatherapp.data.network.Constant
import com.weatherapp.data.network.RetrofitBuilder
import com.weatherapp.data.network.Status
import com.weatherapp.databinding.ActivityMainBinding
import com.weatherapp.repository.WeatherRepository
import com.weatherapp.ui.factory.WeatherViewModelFactory
import com.weatherapp.ui.view_model.WeatherViewModel
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var binding : ActivityMainBinding
    private val df = DecimalFormat("#.##")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init(){
        hideNewProgress()
        val service = RetrofitBuilder.apiService
        val helper = ApiHelper(service)
        val repository = WeatherRepository(helper)
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(repository))[WeatherViewModel::class.java]
        binding.homeTopAppBar.btnSearch.setOnClickListener {
            fetchWeather(binding.homeTopAppBar.homeSearchEditText.text.toString().plus(",US"))
        }

    }

    private fun showNewProgress() {
        binding.progressBar.isIndeterminate = true
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideNewProgress() {
        binding.progressBar.isIndeterminate = false
        binding.progressBar.visibility = View.GONE
    }

    private fun fetchWeather(param1 : String){
        weatherViewModel.fetchWeather(param1, Constant.API_KEY).observe(this){
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideNewProgress()
                        resource.data?.let { data ->
                            println("data => $data")

                            val str = "Current weather of ".plus(data.name)
                                .plus(" { ").plus(data.sys.country).plus(" }")
                                .plus("\n").plus("Temp : ")
                                .plus(df.format(data.main.temp-273.15))
                                .plus(" °C")
                                .plus("\nFeels Like: ")
                                .plus(df.format(data.main.feels_like-273.15)
                                    .plus(" C"))
                                .plus("\nHumidity: ").plus(data.main.humidity).plus("%")
                                .plus("\nDescription: ").plus(data.weather[0].description)
                                .plus("\nWind Speed: ").plus(data.wind.speed).plus("m/s (meters per second)")
                                .plus("\nCloudiness: ").plus(data.clouds.all).plus("%")
                                .plus("\nPressure: ").plus(data.main.pressure).plus("hPa")
                            binding.textWeather.text = str
                            Glide.with(binding.imageWeather.context).load(Constant.IMAGE_URL.plus(data.weather[0].icon).plus(".png")).into(binding.imageWeather)
                        }
                        if(resource.data == null){
                            showMessage(resource.message!!)
                        }

                    }
                    Status.ERROR ->{
                        hideNewProgress()
                        if (resource.data == null) {
                            showMessage(resource.message!!)
                        }
                    }
                    Status.LOADING -> {
                        showNewProgress()
                    }
                }
            }
        }
    }

    private fun showMessage(msg: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snackBar = Snackbar.make(parentLayout, msg, Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.RED)
        val snackBarView = snackBar.view
        val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.textSize = 14f
        textView.typeface.isBold
        snackBar.show()
    }

}