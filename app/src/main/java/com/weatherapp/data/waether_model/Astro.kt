package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class Astro(
    val is_moon_up: Int? = null,
    val is_sun_up: Int? = null,
    val moon_illumination: String? = null,
    val moon_phase: String? = null,
    val moonrise: String? = null,
    val moonset: String? = null,
    val sunrise: String? = null,
    val sunset: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(is_moon_up)
        parcel.writeValue(is_sun_up)
        parcel.writeString(moon_illumination)
        parcel.writeString(moon_phase)
        parcel.writeString(moonrise)
        parcel.writeString(moonset)
        parcel.writeString(sunrise)
        parcel.writeString(sunset)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Astro> {
        override fun createFromParcel(parcel: Parcel): Astro {
            return Astro(parcel)
        }

        override fun newArray(size: Int): Array<Astro?> {
            return arrayOfNulls(size)
        }
    }
}