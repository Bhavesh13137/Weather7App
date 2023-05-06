package com.weatherapp.data.waether_model

import android.os.Parcel
import android.os.Parcelable

data class Condition(
    val code: Int? = null,
    val icon: String? = null,
    val text: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(code)
        parcel.writeString(icon)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Condition> {
        override fun createFromParcel(parcel: Parcel): Condition {
            return Condition(parcel)
        }

        override fun newArray(size: Int): Array<Condition?> {
            return arrayOfNulls(size)
        }
    }
}