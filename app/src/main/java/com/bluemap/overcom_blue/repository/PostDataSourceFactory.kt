package com.bluemap.overcom_blue.repository

import androidx.paging.DataSource
import com.bluemap.overcom_blue.model.Post
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostDataSourceFactory (
        private val repository: Repository,
        private val compositeDisposable: CompositeDisposable
    ) :DataSource.Factory<Int, Post>(){
    override fun create(): DataSource<Int, Post> {
        return PostDataSource(repository,compositeDisposable)
    }
}