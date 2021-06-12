package com.bluemap.overcom_blue.ui.main.board

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.PostDataSourceFactory
import com.bluemap.overcom_blue.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class BoardViewModel @Inject constructor(
        val repository: Repository
) : ViewModel() {
    private val mDisposable = CompositeDisposable()

    private val config= PagedList.Config.Builder()
            .setPageSize(20)    //Defines the number of items loaded at once from the DataSource.
            .setInitialLoadSizeHint(20) //Defines how many items to load when first load occurs. Default = PAGE SIZE * 3
            .setPrefetchDistance(20)    //Defines how far from the edge of loaded content an access must be to trigger further loading.
            .setEnablePlaceholders(true)    //Pass false to disable null placeholders in PagedLists using this Config.
            .build()
    private lateinit var pagedItems: Disposable

    private val builder: RxPagedListBuilder<Int, Post>
            = RxPagedListBuilder<Int, Post>(PostDataSourceFactory(repository,mDisposable), config)

    val posts = MutableLiveData<PagedList<Post>>()
    val notice = MutableLiveData<Post>()
    val refreshStatus = ObservableBoolean()

    val search  = MutableLiveData<String>()

    init {
        loadPosts()
        getNotice()
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    fun loadPosts(){
        refreshStatus.set(true)
        pagedItems = builder.buildObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    posts.value = it
                    refreshStatus.set(false)
                }, {
                    it.stackTrace
                    refreshStatus.set(false)
                })
        mDisposable.add(pagedItems)
    }

    fun getNotice() {
        val disposable = repository.getNotice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    notice.value = it
                }, {
                    it.stackTrace
                })
        mDisposable.add(disposable)
    }
}