package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class Forecastday(
    val astro: Astro? = null,
    val date: String? = null,
    val date_epoch: Int? = null,
    val day: Day? = null,
    val hour: List<Hour>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Astro::class.java.classLoader),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Day::class.java.classLoader),
        parcel.createTypedArrayList(Hour)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(astro, flags)
        parcel.writeString(date)
        parcel.writeValue(date_epoch)
        parcel.writeParcelable(day, flags)
        parcel.writeTypedList(hour)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Forecastday> {
        override fun createFromParcel(parcel: Parcel): Forecastday {
            return Forecastday(parcel)
        }

        override fun newArray(size: Int): Array<Forecastday?> {
            return arrayOfNulls(size)
        }
    }
}