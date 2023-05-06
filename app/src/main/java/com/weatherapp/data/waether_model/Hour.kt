package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class Hour(
    val chance_of_rain: Int? = null,
    val chance_of_snow: Int? = null,
    val cloud: Int? = null,
    val condition: Condition? = null,
    val dewpoint_c: Double? = null,
    val dewpoint_f: Double? = null,
    val feelslike_c: Double? = null,
    val feelslike_f: Double? = null,
    val gust_kph: Double? = null,
    val gust_mph: Double? = null,
    val heatindex_c: Double? = null,
    val heatindex_f: Double? = null,
    val humidity: Int? = null,
    val is_day: Int? = null,
    val precip_in: Double? = null,
    val precip_mm: Double? = null,
    val pressure_in: Double? = null,
    val pressure_mb: Double? = null,
    val temp_c: Double? = null,
    val temp_f: Double? = null,
    val time: String? = null,
    val time_epoch: Int? = null,
    val uv: Double? = null,
    val vis_km: Double? = null,
    val vis_miles: Double? = null,
    val will_it_rain: Int? = null,
    val will_it_snow: Int? = null,
    val wind_degree: Int? = null,
    val wind_dir: String? = null,
    val wind_kph: Double? = null,
    val wind_mph: Double? = null,
    val windchill_c: Double? = null,
    val windchill_f: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Condition::class.java.classLoader),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(chance_of_rain)
        parcel.writeValue(chance_of_snow)
        parcel.writeValue(cloud)
        parcel.writeParcelable(condition, flags)
        parcel.writeValue(dewpoint_c)
        parcel.writeValue(dewpoint_f)
        parcel.writeValue(feelslike_c)
        parcel.writeValue(feelslike_f)
        parcel.writeValue(gust_kph)
        parcel.writeValue(gust_mph)
        parcel.writeValue(heatindex_c)
        parcel.writeValue(heatindex_f)
        parcel.writeValue(humidity)
        parcel.writeValue(is_day)
        parcel.writeValue(precip_in)
        parcel.writeValue(precip_mm)
        parcel.writeValue(pressure_in)
        parcel.writeValue(pressure_mb)
        parcel.writeValue(temp_c)
        parcel.writeValue(temp_f)
        parcel.writeString(time)
        parcel.writeValue(time_epoch)
        parcel.writeValue(uv)
        parcel.writeValue(vis_km)
        parcel.writeValue(vis_miles)
        parcel.writeValue(will_it_rain)
        parcel.writeValue(will_it_snow)
        parcel.writeValue(wind_degree)
        parcel.writeString(wind_dir)
        parcel.writeValue(wind_kph)
        parcel.writeValue(wind_mph)
        parcel.writeValue(windchill_c)
        parcel.writeValue(windchill_f)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hour> {
        override fun createFromParcel(parcel: Parcel): Hour {
            return Hour(parcel)
        }

        override fun newArray(size: Int): Array<Hour?> {
            return arrayOfNulls(size)
        }
    }
}