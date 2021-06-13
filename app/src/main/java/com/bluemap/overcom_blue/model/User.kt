package com.bluemap.overcom_blue.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class User(
    val id:Int?,
    val kakaoId: Int?,
    val name: String?,
    var login:Boolean?,
    var bluePoint:Int?
): Parcelable {

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
    )

    constructor(kakaoId: Int?):this(null,kakaoId,null,null,null)
    constructor(id: Int?,name: String):this(id,null,name,null,null)
    constructor(id: Int?,name: String,login: Boolean?,bluePoint: Int?):this(id,null,name,login,bluePoint)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id!!)
        dest.writeInt(kakaoId!!)
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