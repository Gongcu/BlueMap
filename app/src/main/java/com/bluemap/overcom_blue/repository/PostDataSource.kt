package com.bluemap.overcom_blue.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.bluemap.overcom_blue.model.Post
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostDataSource @Inject constructor(
        private val userId: Int,
        private val repository: Repository,
        private val compositeDisposable: CompositeDisposable
    ) : PageKeyedDataSource<Int,Post>() {

    //자체적으로 백그라운드 스레드를 통해 비동기로 가져옴 -> UI 접근 불가

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Post>
    ) {
        val disposable = repository.getPostList(0,userId)
                .subscribe { it ->
                    offset += it.size
                    Log.i(TAG, offset.toString())
                    callback.onResult(it, null,it.size);
                }
        compositeDisposable.add(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
        //do nothing
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
        val disposable = repository.getPostList(params.key,userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { it ->
                    Log.i(TAG,offset.toString())
                    offset += it.size
                    callback.onResult(it, offset)
                }
        compositeDisposable.add(disposable)
    }

    companion object{
        var offset = 0
        const val TAG ="PostDataSource"
    }
}