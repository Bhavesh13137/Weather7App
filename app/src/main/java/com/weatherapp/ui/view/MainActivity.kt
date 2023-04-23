package com.weatherapp.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.weatherapp.R
import com.weatherapp.data.api_helper.ApiHelper
import com.weatherapp.data.model.LocationEvent
import com.weatherapp.data.network.Constant
import com.weatherapp.data.network.RetrofitBuilder
import com.weatherapp.data.network.Status
import com.weatherapp.databinding.ActivityMainBinding
import com.weatherapp.repository.WeatherRepository
import com.weatherapp.service.LocationService
import com.weatherapp.ui.factory.WeatherViewModelFactory
import com.weatherapp.ui.view_model.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    var muLocation = false
    var firstTimeLocation = false
    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var binding : ActivityMainBinding
    private val df = DecimalFormat("#.##")

    private var service: Intent ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private val backgroundLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            startService(service)
        }
    }
    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){


        when{
            it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false)->{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        backgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }
                }else{
                    startService(service)
                }
            }
            it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                startService(service)
            }
        }
    }

    private fun init() {
        service = Intent(this, LocationService::class.java)
        hideNewProgress()
        val service = RetrofitBuilder.apiService
        val helper = ApiHelper(service)
        val repository = WeatherRepository(helper)
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(repository))[WeatherViewModel::class.java]
        binding.homeTopAppBar.btnSearch.setOnClickListener {
            fetchWeather(binding.homeTopAppBar.homeSearchEditText.text.toString())
        }

        binding.homeTopAppBar.imgBack.setOnClickListener {
            finish()
        }

        binding.btnCurrentLocation.setOnClickListener {
            muLocation = false
            checkPermissions()
        }

    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    private fun checkPermissions(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            locationPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
        }else{
            startService(service)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(service)
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }


    @Subscribe
    fun receiveLocationEvent(locationEvent: LocationEvent){
        //binding.tvLatitude.text = "Latitude -> ${locationEvent.latitude}"
        //binding.tvLongitude.text = "Longitude -> ${locationEvent.longitude}"
        if(!muLocation) {
            muLocation = true
            println("locationEvent $locationEvent")
            val geocoder = Geocoder(this)
            var address = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    locationEvent.latitude!!,
                    locationEvent.longitude!!,
                    1
                ) { addresses ->
                    address = addresses[0].locality

                    CoroutineScope(Dispatchers.Main).launch {
                        fetchCurrentWeather(address)
                    }
                }
            } else {
                val list =
                    geocoder.getFromLocation(locationEvent.latitude!!, locationEvent.longitude!!, 1)
                address = list?.get(0)?.locality ?: "Pune"
                CoroutineScope(Dispatchers.Main).launch {
                    fetchCurrentWeather(address)
                }
            }
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

        println("Param $param1")

        weatherViewModel.fetchWeather(param1.plus(",US"),"","",Constant.API_KEY).observe(this){
            it.let { resource ->
                println("resource => $resource")
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideNewProgress()

                        resource.data?.let { data ->


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
                            binding.textWeather2.text = str
                            Glide.with(binding.imageWeather2.context).load(
                                Constant.IMAGE_URL.plus(data.weather[0].icon).plus(".png")
                            ).into(binding.imageWeather2)
                        }
                        if(resource.data == null){
                            showMessage("Please enter US City for Weather Information")
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

    private fun fetchCurrentWeather(param1 : String){

        println("Param $param1")

        weatherViewModel.fetchWeather(param1,"","" ,Constant.API_KEY).observe(this){
            it.let { resource ->
                println("resource => $resource")
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideNewProgress()

                        resource.data?.let { data ->

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
                            Glide.with(binding.imageWeather.context).load(
                                Constant.IMAGE_URL.plus(data.weather[0].icon).plus(".png")
                            ).into(binding.imageWeather)
                        }
                        if(resource.data == null){
                            showMessage("Please enter US City for Weather Information")
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