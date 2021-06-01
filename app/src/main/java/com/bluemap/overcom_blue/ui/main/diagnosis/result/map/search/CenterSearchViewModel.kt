package com.bluemap.overcom_blue.ui.main.diagnosis.result.map.search

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.repository.CenterDataSourceFactory
import com.bluemap.overcom_blue.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CenterSearchViewModel @Inject constructor(
        private val repository: Repository
):ViewModel() {
    private val mDisposable = CompositeDisposable()
    private val config = PagedList.Config.Builder()
            .setPageSize(20)    //Defines the number of items loaded at once from the DataSource.
            .setInitialLoadSizeHint(20) //Defines how many items to load when first load occurs. Default = PAGE SIZE * 3
            .setPrefetchDistance(20)    //Defines how far from the edge of loaded content an access must be to trigger further loading.
            .setEnablePlaceholders(true)    //Pass false to disable null placeholders in PagedLists using this Config.
            .build()
    private val builder: RxPagedListBuilder<Int, Center>
            =RxPagedListBuilder<Int, Center>(CenterDataSourceFactory(repository, mDisposable), config)

    val centers = MutableLiveData<PagedList<Center>>()


    fun search() {
        val pagedItems = builder.buildObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    centers.value=it
                }, {
                    it.stackTrace
                })
        mDisposable.add(pagedItems)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    companion object {
        val searchText = ObservableField<String>("")
    }
}