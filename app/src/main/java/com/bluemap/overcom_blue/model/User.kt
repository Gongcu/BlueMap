package com.bluemap.overcom_blue.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id:Int?,
    val name: String?,
    var login:Boolean?,
    var bluePoint:Int?
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
    )

    constructor(name: String):this(null,name,null,null)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id!!)
        dest.writeString(name)
        dest.writeInt(bluePoint!!)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}