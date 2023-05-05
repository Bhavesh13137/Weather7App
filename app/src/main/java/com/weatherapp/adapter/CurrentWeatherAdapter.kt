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
        holder.Ctemp.text = (weathernew.main?.temp?.minus(273.15))?.toInt().toString() + "\u2103"
        holder.Cwedesc.text = weathernew.weather?.get(0)?.description.toString().capitalize()

        holder.CtodayTitle.text = "Today, " + weathernew.dt_txt.toString().slice(10..15)


        Log.d("Something", weathernew.dt_txt.toString().slice(10..-1))

//        var tempMin = ""
//        for(a in weathernew.main.temp_min){
//        }
        var ss = weathernew.main?.temp_min
        holder.CminTemp.text = (weathernew.main?.temp_min?.toDouble()?.minus(273.1))?.toInt().toString()+ "\u2103"
//        holder.continer.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale)

//        holder.itemView.setOnFocusChangeListener { view, b ->
//
//        }

        holder.CmaxTemp.text = ((weathernew.main?.temp_max?.toDouble())?.minus(273.1))?.toInt().toString() + "\u2103"
        holder.Chumidity.text = weathernew.main?.humidity.toString() + "%"
        val iconcode = weathernew.weather?.get(0)?.icon.toString()
        val iconurl = "https://openweathermap.org/img/w/" + iconcode + ".png";

        Glide.with(holder.itemView.context)
            .load(iconurl)
            .into(holder.Cwelogo)
    }

    interface OnClickListener {
        fun onViewDetail(i: Int, model: Lists)
    }
}