package com.bluemap.overcom_blue.model

import android.os.Parcel
import android.os.Parcelable

data class Center(
    val id: Int,
    val specification: String,
    val county: String,
    val city: String,
    val agencyName:String,
    val phone:String,
    val homepage:String,
    val address:String,
    val specificAddress:String,
    val latitude:Double,
    val longitude :Double
):Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(specification)
        dest.writeString(county)
        dest.writeString(city)
        dest.writeString(agencyName)
        dest.writeString(phone)
        dest.writeString(homepage)
        dest.writeString(address)
        dest.writeString(specificAddress)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }


    companion object CREATOR : Parcelable.Creator<Center> {
        override fun createFromParcel(parcel: Parcel): Center {
            return Center(parcel)
        }

        override fun newArray(size: Int): Array<Center?> {
            return arrayOfNulls(size)
        }
    }
}