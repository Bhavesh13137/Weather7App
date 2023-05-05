package com.weatherapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class Rain(
    val `3h`: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readValue(Double::class.java.classLoader) as? Double)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(3 )
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rain> {
        override fun createFromParcel(parcel: Parcel): Rain {
            return Rain(parcel)
        }

        override fun newArray(size: Int): Array<Rain?> {
            return arrayOfNulls(size)
        }
    }
}