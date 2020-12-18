package com.bluemap.overcom_blue.model

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
    var like: Boolean?
) {
    constructor(userId: Int,title: String,content: String)
    :this(null,null,userId,title, content, null, null, null, null, null)
}