package com.bluemap.overcom_blue

import android.app.Application
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.network.Retrofit
import retrofit2.Call

class Repository(val application: Application) {
    private val retrofit : retrofit2.Retrofit = Retrofit.getInstance()
    private val bluemapAPI = retrofit.create(BluemapAPI::class.java)

    fun writePost(post: Post): Call<Void> {
        return bluemapAPI.writePost(post)
    }

    fun getPostList():Call<List<Post>> {
        return bluemapAPI.getPostList((application as BaseApplication).userId)
    }

    fun getCenter(lat:Double, lng:Double):Call<List<Center>>{
        return bluemapAPI.getCenter(lat,lng)
    }
}