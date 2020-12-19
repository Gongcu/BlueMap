package com.bluemap.overcom_blue.model

import android.os.Parcel
import android.os.Parcelable

data class Post(
    val id: Int?,
    val name: String?,
    val userId: Int,
    val title: String,
    val content: String,
    val createdAt: String?,
    var viewCount: Int?,
    var likeCount: Int?,
    var commentCount: Int?,
    var like: Int?
):Parcelable {
    constructor(userId: Int, title: String, content: String)
            : this(null, null, userId, title, content, null, null, null, null, null)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id!!)
        dest.writeString(name)
        dest.writeInt(userId)
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(createdAt)
        dest.writeInt(viewCount!!)
        dest.writeInt(likeCount!!)
        dest.writeInt(commentCount!!)
        dest.writeInt(like!!)

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