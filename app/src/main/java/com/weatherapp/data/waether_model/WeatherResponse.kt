package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class WeatherResponse(
    val current: Current? = null,
    val forecast: Forecast? = null,
    val location: Location? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Current::class.java.classLoader),
        parcel.readParcelable(Forecast::class.java.classLoader),
        parcel.readParcelable(Location::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(current, flags)
        parcel.writeParcelable(forecast, flags)
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherResponse> {
        override fun createFromParcel(parcel: Parcel): WeatherResponse {
            return WeatherResponse(parcel)
        }

        override fun newArray(size: Int): Array<WeatherResponse?> {
            return arrayOfNulls(size)
        }
    }
}