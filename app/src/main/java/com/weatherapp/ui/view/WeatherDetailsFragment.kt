package com.weatherapp.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.weatherapp.R
import com.weatherapp.data.model.Lists
import com.weatherapp.data.network.Constant
import java.util.*

private const val ARG_PARAM1 = "param1"

class WeatherDetailsFragment : Fragment() {

    private var param1: Lists? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_PARAM1,Lists::class.java)
            }else{
                it.getParcelable(ARG_PARAM1)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        val cTemp = view.findViewById<TextView>(R.id.temp)
        val cWeDesc = view.findViewById<TextView>(R.id.desc)
        val cWeLogo = view.findViewById<ImageView>(R.id.icon)
        val cMinTemp = view.findViewById<TextView>(R.id.minTemp)
        val cMaxTemp = view.findViewById<TextView>(R.id.maxTemp)
        val cHumidity = view.findViewById<TextView>(R.id.humidity)
        val cTodayTitle = view.findViewById<TextView>(R.id.todayTitle)
        cTemp.text = (param1?.main?.temp?.minus(273.15))?.toInt().toString() + "\u2103"
        cWeDesc.text = param1?.weather?.get(0)?.description.toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        cTodayTitle.text = "Today, " + param1?.dt_txt.toString().slice(10..15)

        val ss = param1?.main?.temp_min
        cMinTemp.text = (ss?.minus(273.1))?.toInt().toString()+ "\u2103"
        cMaxTemp.text = ((param1?.main?.temp_max?.toDouble())?.minus(273.1))?.toInt().toString() + "\u2103"
        cHumidity.text = param1?.main?.humidity.toString() + "%"
        val iconcode = param1?.weather?.get(0)?.icon.toString()
        val iconUrl = Constant.IMAGE_URL.plus(iconcode).plus(".png") //"https://openweathermap.org/img/w/" + iconcode + ".png";

        Glide.with(cWeLogo.context)
            .load(iconUrl)
            .into(cWeLogo)
    }


    override fun onStart() {
        super.onStart()
        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(false)
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                //menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                if(menuItem.itemId == android.R.id.home){
                    findNavController().navigateUp()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}