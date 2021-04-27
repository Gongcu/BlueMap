package com.bluemap.overcom_blue.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.bluemap.overcom_blue.ui.main.diagnosis.map.search.CenterSearchFragment
import com.bluemap.overcom_blue.model.Center
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CenterDataSource  @Inject constructor(
        private val repository: Repository,
        private val compositeDisposable: CompositeDisposable
    ) : PageKeyedDataSource<Int, Center>() {

    //자체적으로 백그라운드 스레드를 통해 비동기로 가져옴 -> UI 접근 불가

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Center>
    ) {
        val disposable = repository.getCenter(CenterSearchFragment.search,0)
                .subscribe { it ->
                    offset += it.size
                    Log.i(TAG, offset.toString())
                    callback.onResult(it, null,it.size);
                }
        compositeDisposable.add(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Center>) {
        //do nothing
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Center>) {
        val disposable = repository.getCenter(CenterSearchFragment.search,params.key)
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
        const val TAG ="CenterDataSource"
    }
}