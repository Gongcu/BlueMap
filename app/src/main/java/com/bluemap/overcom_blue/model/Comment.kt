package com.bluemap.overcom_blue.model

data class Comment(
    val id : Int?,
    val userId: Int,
    val name: String?,
    val comment: String,
    var parent: Int?,
    var like: Int?,
    val groupId: Int?,
    val createdAt: String?,
    var likeCount: Int?
) {
    constructor(userId: Int,comment: String) : this(null,userId,null,comment,null,null,null,null,null)
}