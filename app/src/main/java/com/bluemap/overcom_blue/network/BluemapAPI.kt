package com.bluemap.overcom_blue.network

import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Comment
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface BluemapAPI {
    @GET("post/{userId}")
    fun getPostList(
        @Path("userId") userId:Int,
        @Query("offset") offset:Int
    ) : Single<List<Post>>

    /*
    @GET("post/exercise/{exercise}/hot")
    fun getHotPostList(@Path("exercise") exercise:String) : Call<List<GuideItem>>
    */

    @GET("post/{postId}/{userId}")
    fun getPostById(
        @Path("postId") postId:Int,
        @Path("userId") userId:Int
    ) : Single<Post>

    @GET("post/{postId}/comment/{userId}")
    fun getComment(
        @Path("postId") postId:Int,
        @Path("userId") userId:Int
    ): Single<List<Comment>>

    @POST("user")
    fun postUser(@Body user: User) : Single<User>

    @PATCH("user/nickname")
    fun patchNickname(@Body user: User) : Single<User>

    @POST("post")
    fun writePost(@Body post: Post) : Completable

    @POST("post/{postId}/comment")
    fun writeComment(
        @Path("postId") postId:Int,
        @Body comment: Comment) : Single<List<Comment>>


    @POST("post/{postId}/reply/{commentId}")
    fun writeReplyComment(
        @Path("postId") postId:Int,
        @Path("commentId") commentId:Int,
        @Body comment: Comment
    ) : Single<List<Comment>>


    @PATCH("post/like/{postId}/{userId}")
    fun likePost(
        @Path("postId") postId:Int,
        @Path("userId") userId:Int
    ) : Single<Boolean>

    @PATCH("post/like/comment/{commentId}/{userId}")
    fun likeComment(
        @Path("commentId") commentId:Int,
        @Path("userId") userId:Int
    ) : Single<Boolean>

    @GET("center/{latitude}/{longitude}")
    fun getCenter(
        @Path("latitude") latitude:Double,
        @Path("longitude") longitude:Double
    ) : Single<List<Center>>
}