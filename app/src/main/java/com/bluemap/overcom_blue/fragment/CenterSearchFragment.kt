package com.bluemap.overcom_blue.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.adapter.CenterPageAdapter
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.repository.CenterDataSourceFactory
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_center_search.*
import kotlinx.android.synthetic.main.fragment_center_search.view.*


class CenterSearchFragment : Fragment() {
    private val imm: InputMethodManager by lazy{
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private lateinit var repository :Repository
    private lateinit var adapter : CenterPageAdapter
    private val disposable = CompositeDisposable()
    private lateinit var pagedItems : Disposable
    private val config = PagedList.Config.Builder()
        .setPageSize(20)    //Defines the number of items loaded at once from the DataSource.
        .setInitialLoadSizeHint(20) //Defines how many items to load when first load occurs. Default = PAGE SIZE * 3
        .setPrefetchDistance(20)    //Defines how far from the edge of loaded content an access must be to trigger further loading.
        .setEnablePlaceholders(true)    //Pass false to disable null placeholders in PagedLists using this Config.
        .build()
    private lateinit var builder: RxPagedListBuilder<Int,Center>

    //adapter : 검색어 연관 아이템 나열, 아이템 클릭시 map frag로 넘어가고 지도에 해당 센터 위치 표시

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Repository(requireActivity().application)
        builder=RxPagedListBuilder<Int, Center>(CenterDataSourceFactory(repository, disposable), config)
        adapter = CenterPageAdapter(requireContext()){
            val directions = CenterSearchFragmentDirections.actionCenterSearchFragmentToMapFragment(it)
            findNavController().navigate(directions)
        }
        Log.d("CENTER_SEARCH","onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_center_search, container, false)
        view.search_edit_text_view.requestFocus()
        view.search_btn.setOnClickListener {
            if(view.search_edit_text_view.text.isNotEmpty()){
                search = view.search_edit_text_view.text.toString()
                search()
            }
        }
        Log.d("CENTER_SEARCH","onCreateView")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
        imm.showSoftInput(search_edit_text_view, InputMethodManager.SHOW_IMPLICIT)
        //강서
    }

    private fun search() {
        Util.progressOnInFragment(this)
        pagedItems = builder.buildObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                adapter.submitList(it)
                Util.progressOffInFragment()
            }, {
                it.stackTrace
            })
        disposable.add(pagedItems)
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    companion object {
        var search = ""
    }
}