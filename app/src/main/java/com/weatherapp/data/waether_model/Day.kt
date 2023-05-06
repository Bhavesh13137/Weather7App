package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class Day(
    val avghumidity: Double? = null,
    val avgtemp_c: Double? = null,
    val avgtemp_f: Double? = null,
    val avgvis_km: Double? = null,
    val avgvis_miles: Double? = null,
    val condition: Condition? = null,
    val daily_chance_of_rain: Int? = null,
    val daily_chance_of_snow: Int? = null,
    val daily_will_it_rain: Int? = null,
    val daily_will_it_snow: Int? = null,
    val maxtemp_c: Double? = null,
    val maxtemp_f: Double? = null,
    val maxwind_kph: Double? = null,
    val maxwind_mph: Double? = null,
    val mintemp_c: Double? = null,
    val mintemp_f: Double? = null,
    val totalprecip_in: Double? = null,
    val totalprecip_mm: Double? = null,
    val totalsnow_cm: Double? = null,
    val uv: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readParcelable(Condition::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(avghumidity)
        parcel.writeValue(avgtemp_c)
        parcel.writeValue(avgtemp_f)
        parcel.writeValue(avgvis_km)
        parcel.writeValue(avgvis_miles)
        parcel.writeParcelable(condition, flags)
        parcel.writeValue(daily_chance_of_rain)
        parcel.writeValue(daily_chance_of_snow)
        parcel.writeValue(daily_will_it_rain)
        parcel.writeValue(daily_will_it_snow)
        parcel.writeValue(maxtemp_c)
        parcel.writeValue(maxtemp_f)
        parcel.writeValue(maxwind_kph)
        parcel.writeValue(maxwind_mph)
        parcel.writeValue(mintemp_c)
        parcel.writeValue(mintemp_f)
        parcel.writeValue(totalprecip_in)
        parcel.writeValue(totalprecip_mm)
        parcel.writeValue(totalsnow_cm)
        parcel.writeValue(uv)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Day> {
        override fun createFromParcel(parcel: Parcel): Day {
            return Day(parcel)
        }

        override fun newArray(size: Int): Array<Day?> {
            return arrayOfNulls(size)
        }
    }
}