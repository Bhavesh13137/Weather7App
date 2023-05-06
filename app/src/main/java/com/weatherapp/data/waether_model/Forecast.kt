package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class Forecast(
    val forecastday: List<Forecastday>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Forecastday))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(forecastday)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Forecast> {
        override fun createFromParcel(parcel: Parcel): Forecast {
            return Forecast(parcel)
        }

        override fun newArray(size: Int): Array<Forecast?> {
            return arrayOfNulls(size)
        }
    }
}