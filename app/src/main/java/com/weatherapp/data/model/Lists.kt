package com.weatherapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class Lists(
    val clouds: Clouds? = null,
    val dt: Int? = null,
    val dt_txt: String? = null,
    val main: Main? = null,
    val pop: Double? = null,
    val rain: Rain? = null,
    val sys: Sys? = null,
    val visibility: Int? = null,
    val weather: List<Weather>? = null,
    val wind: Wind? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Clouds::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(Main::class.java.classLoader),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readParcelable(Rain::class.java.classLoader),
        parcel.readParcelable(Sys::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(Weather),
        parcel.readParcelable(Wind::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(clouds, flags)
        parcel.writeValue(dt)
        parcel.writeString(dt_txt)
        parcel.writeParcelable(main, flags)
        parcel.writeValue(pop)
        parcel.writeParcelable(rain, flags)
        parcel.writeParcelable(sys, flags)
        parcel.writeValue(visibility)
        parcel.writeTypedList(weather)
        parcel.writeParcelable(wind, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lists> {
        override fun createFromParcel(parcel: Parcel): Lists {
            return Lists(parcel)
        }

        override fun newArray(size: Int): Array<Lists?> {
            return arrayOfNulls(size)
        }
    }
}