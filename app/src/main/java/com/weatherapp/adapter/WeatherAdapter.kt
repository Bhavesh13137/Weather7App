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

class WeatherAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var itemViewModels = mutableListOf<Lists>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<Lists>){
        this.itemViewModels.clear()
        this.itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var wedate = view.findViewById<TextView>(R.id.weatherDate)
        var wedesc = view.findViewById<TextView>(R.id.weatherDescription)
        var wemain = view.findViewById<TextView>(R.id.weatherTemperature)
        var welogo = view.findViewById<ImageView>(R.id.weatherIcon)
        var constraint = view.findViewById<ConstraintLayout>(R.id.constraint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_weather, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weathernew = itemViewModels[position]
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")

        val date: Date? = weathernew.dt_txt?.let { inputFormat.parse(it) }
        val outputDate = date?.let { outputFormat.format(it) }

        Log.d("New Date", outputDate.toString())

        val we = weathernew.weather?.get(0)
        val we2 = weathernew.main
        holder.wedate.text = outputDate
        if (we != null) {
            holder.wedesc.text = we.description?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
        }
        if (we2 != null) {
            Log.d("weatherTemp", we2.temp.toString())
        }
        val Temp = we2?.temp?.minus(273.15)
        if (Temp != null) {
            holder.wemain.text = Temp.toInt().toString() + "\u2103"
        }

        val iconcode = weathernew.weather?.get(0)?.icon.toString()

        val iconurl = Constant.IMAGE_URL.plus(iconcode).plus(".png")

        Glide.with(holder.welogo.context)
            .load(iconurl)
            .into(holder.welogo)


        holder.constraint.setOnClickListener {
            onClickListener.onViewDetails(position,itemViewModels[position])
        }
    }

    override fun getItemCount(): Int {
        return itemViewModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OnClickListener {
        fun onViewDetails(i: Int, model: Lists)
    }
}