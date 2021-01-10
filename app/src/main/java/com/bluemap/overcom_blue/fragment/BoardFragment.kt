package com.bluemap.overcom_blue.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.activity.MainActivity
import com.bluemap.overcom_blue.adapter.PostPageAdapter
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.PostDataSource
import com.bluemap.overcom_blue.repository.PostDataSourceFactory
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_board.view.*


class BoardFragment : Fragment() {
    lateinit var repository: Repository
    lateinit var adapter: PostPageAdapter
    private val mDisposable = CompositeDisposable()
    private lateinit var pagedItems: Disposable
    private lateinit var builder:RxPagedListBuilder<Int,Post>
    private val config= PagedList.Config.Builder()
        .setPageSize(20)    //Defines the number of items loaded at once from the DataSource.
        .setInitialLoadSizeHint(20) //Defines how many items to load when first load occurs. Default = PAGE SIZE * 3
        .setPrefetchDistance(20)    //Defines how far from the edge of loaded content an access must be to trigger further loading.
        .setEnablePlaceholders(true)    //Pass false to disable null placeholders in PagedLists using this Config.
        .build()


    //Callback when fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PostPageAdapter(requireContext()) {
            val directions = BoardFragmentDirections.actionCommunityFragmentToPostFragment(it.id!!)
            findNavController().navigate(directions)
            (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
        }
        repository = Repository(requireActivity().application)
        builder = RxPagedListBuilder<Int, Post>(PostDataSourceFactory(repository,mDisposable), config)
        initData()
    }

    //Callback first init & return from back stack
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.VISIBLE
        view.write_post_btn.setOnClickListener { goToWritePostFragment() }
        view.swipe_refresh_layout.setOnRefreshListener { initData() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setItemViewCacheSize(20)
        recycler_view.adapter = adapter
    }


    private fun initData(){
        pagedItems = builder.buildObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Util.progressOnInFragment(this)
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    adapter.submitList(it)
                    //That means that doFinally it will be called anyway, and it is not warranted that you have a valid context.
                    //I do not use doFinally for this things. I set the loading(false) onNext or onError always. Not pretty but effective.
                    Util.progressOffInFragment()
                    swipe_refresh_layout?.isRefreshing=false
                }, {
                    it.stackTrace
                    Util.progressOffInFragment()
                    swipe_refresh_layout?.isRefreshing=false
                })
        mDisposable.add(pagedItems)
    }

    override fun onStop() {
        super.onStop()
        mDisposable.clear()
        PostDataSource.offset = 0
    }

    private fun goToWritePostFragment(){
        val navDirections = BoardFragmentDirections.actionCommunityFragmentToPostWriteFragment()
        findNavController().navigate(navDirections)
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
    }
}