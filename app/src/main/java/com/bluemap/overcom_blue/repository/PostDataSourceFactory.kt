package com.bluemap.overcom_blue.repository

import androidx.paging.DataSource
import com.bluemap.overcom_blue.model.Post

class PostDataSourceFactory(private val repository: Repository) :DataSource.Factory<Int, Post>(){
    override fun create(): DataSource<Int, Post> {
        return PostDataSource(repository)
    }
}