package com.bluemap.overcom_blue.repository

import androidx.paging.DataSource
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Post
import io.reactivex.disposables.CompositeDisposable

class CenterDataSourceFactory(
        private val repository: Repository,
        private val compositeDisposable: CompositeDisposable
    ) :DataSource.Factory<Int, Center>(){
    override fun create(): DataSource<Int, Center> {
        return CenterDataSource(repository,compositeDisposable)
    }
}