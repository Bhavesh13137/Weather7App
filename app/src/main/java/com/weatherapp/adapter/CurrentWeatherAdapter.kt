package com.weatherapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weatherapp.R
import com.weatherapp.data.model.Lists
import com.weatherapp.data.network.Constant
import java.text.SimpleDateFormat
import java.util.*

class CurrentWeatherAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<CurrentWeatherAdapter.CurrentWeatherViewHolder>() {

    private val weatherrootdatas = mutableListOf<Lists>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<Lists>){
        this.weatherrootdatas.clear()
        this.weatherrootdatas.addAll(list)
        notifyDataSetChanged()
    }

    class CurrentWeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var Ctemp=itemView.findViewById<TextView>(R.id.temp)
        var Cwedesc=itemView.findViewById<TextView>(R.id.desc)
        var Cwelogo=itemView.findViewById<ImageView>(R.id.icon)
        var CminTemp=itemView.findViewById<TextView>(R.id.minTemp)
        var CmaxTemp=itemView.findViewById<TextView>(R.id.maxTemp)
        var Chumidity=itemView.findViewById<TextView>(R.id.humidity)
        var CtodayTitle=itemView.findViewById<TextView>(R.id.todayTitle)
        var continer = itemView.findViewById<ConstraintLayout>(R.id.currentWeatherContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentWeatherViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_current_weather,parent,false)
        return CurrentWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherrootdatas.size
    }

    override fun onBindViewHolder(holder: CurrentWeatherViewHolder, position: Int) {
        val weathernew = weatherrootdatas[position]
        holder.Ctemp.text = weathernew.main?.temp?.minus(273.15)?.toInt().toString() + "\u2103"
        holder.Cwedesc.text = weathernew.weather?.get(0)?.description.toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")



        val date: Date? = weathernew.dt_txt?.let { inputFormat.parse(it) }
        val outputDate = date?.let { outputFormat.format(it) }

        holder.CtodayTitle.text = outputDate.toString()

        val ss = weathernew.main?.temp_min
        holder.CminTemp.text = ss?.minus(273.1)?.toInt().toString()+ "\u2103"


        holder.CmaxTemp.text = weathernew.main?.temp_max?.minus(273.1)?.toInt().toString() + "\u2103"
        holder.Chumidity.text = weathernew.main?.humidity.toString() + "%"
        val iconCode = weathernew.weather?.get(0)?.icon.toString()
        val iconUrl = Constant.IMAGE_URL.plus(iconCode).plus(".png")

        Glide.with(holder.Cwelogo.context)
            .load(iconUrl)
            .into(holder.Cwelogo)
    }

    interface OnClickListener {
        fun onViewDetail(i: Int, model: Lists)
    }
}