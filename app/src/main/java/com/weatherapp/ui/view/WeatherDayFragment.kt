package com.weatherapp.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.weatherapp.R
import com.weatherapp.adapter.CurrentWeatherAdapter
import com.weatherapp.adapter.WeatherAdapter
import com.weatherapp.data.api_helper.ApiHelper
import com.weatherapp.data.model.Lists
import com.weatherapp.data.network.Constant
import com.weatherapp.data.network.RetrofitBuilder
import com.weatherapp.repository.WeatherRepository
import com.weatherapp.ui.factory.WeatherViewModelFactory
import com.weatherapp.ui.view_model.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherDayFragment : Fragment() , WeatherAdapter.OnClickListener, CurrentWeatherAdapter.OnClickListener{
    private val adapter = WeatherAdapter(this)
    private val adapterCurrentWeather = CurrentWeatherAdapter(this)
    private lateinit var viewModel: WeatherViewModel
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private lateinit var progressBar : ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_weather_day, container, false)
    }

    fun showNewProgress() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
    }

    fun hideNewProgress() {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Weather Forecast"
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val recView = view.findViewById<RecyclerView>(R.id.recView)
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter

        val recViewCurrentWeather = view.findViewById<RecyclerView>(R.id.recViewCurrentWeather)
        recViewCurrentWeather.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recViewCurrentWeather.adapter = adapterCurrentWeather

        val service = RetrofitBuilder.apiService
        val helper = ApiHelper(service)
        val repository = WeatherRepository(helper)
        viewModel = ViewModelProvider(requireActivity(),WeatherViewModelFactory(repository))[WeatherViewModel::class.java]
        init()
    }

    private fun init(){
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
        //viewModel.fetchWeather("Pune", "", "", Constant.API_KEY)
    }

    private val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,30).apply {
        setMinUpdateDistanceMeters(30F)
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setWaitForAccurateLocation(true)
    }.build()

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                getAddress(location)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun getAddress(location: Location) {

        val geocoder = Geocoder(requireContext())
        var address: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                address = addresses[0].locality
                println()
                CoroutineScope(Dispatchers.Main).launch {
                    fetchCurrentWeather(address,location.latitude.toString(),location.longitude.toString())
                }
            }
        } else {
            val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            address = list?.get(0)?.locality ?: "Pune"
            CoroutineScope(Dispatchers.Main).launch {
                fetchCurrentWeather(address,location.latitude.toString(),location.longitude.toString())
            }
        }
    }


    private fun fetchCurrentWeather(param1 : String,param2 : String,param3 : String) {
        showNewProgress()
        viewModel.list.observe(viewLifecycleOwner){
            hideNewProgress()


            val firstDate = it.list?.get(0)?.dt_txt?.slice(8..9)
            var otherDates = firstDate
            var i = 1
            val data2 = mutableListOf<Lists>()

            while (otherDates == firstDate) {
                it.list?.get(i - 1)?.let { it1 -> data2.add(it1) }
                otherDates = it.list?.get(i)?.dt_txt?.slice(8..9)
                i += 1
            }
            adapterCurrentWeather.setList(data2)


            var data3 = mutableListOf<Lists>()
            for (a in i - 1..39) {
                if (it.list?.get(a)?.dt_txt?.slice(11..12) == "12") {
                    it.list[a].dt_txt?.let { it1 -> Log.d("Something date", it1) }
                    data3.add(it.list[a])
                }
            }

            adapter.setList(data3)
        }
        viewModel.fetchWeather(param1,param2,param3,Constant.API_KEY)
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                        // Now check background location
                        checkBackgroundLocation()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", requireContext().packageName, null),
                            ),
                        )
                    }
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                        Toast.makeText(
                            requireContext(),
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }

    override fun onViewDetails(i: Int, model: Lists) {
        val bundle = Bundle()
        bundle.putParcelable("param1",model)
        findNavController().navigate(R.id.action_weatherDayFragment_to_weatherDetailsFragment,bundle)
    }

    override fun onViewDetail(i: Int, model: Lists) {

    }
}