package com.bluemap.overcom_blue.fragment

import android.annotation.SuppressLint
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
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.PostDataSourceFactory
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_board.*


class BoardFragment : Fragment() {
    lateinit var repository: Repository
    lateinit var adapter: PostPageAdapter
    private val mDisposable = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adapter = PostPageAdapter(requireContext()) {
            val directions = BoardFragmentDirections.actionCommunityFragmentToPostFragment(it.id!!)
            findNavController().navigate(directions)
            (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
        }
        repository = Repository(activity!!.application)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        write_post_btn.setOnClickListener { goToWritePostFragment() }
    }

    private fun init(){
        val config= PagedList.Config.Builder()
                .setPageSize(20)    //Defines the number of items loaded at once from the DataSource.
                .setInitialLoadSizeHint(20) //Defines how many items to load when first load occurs. Default = PAGE SIZE * 3
                .setPrefetchDistance(20)    //Defines how far from the edge of loaded content an access must be to trigger further loading.
                .setEnablePlaceholders(true)    //Pass false to disable null placeholders in PagedLists using this Config.
                .build()

        val builder = RxPagedListBuilder<Int, Post>(PostDataSourceFactory(repository), config)

        val pagedItems = builder.buildObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("ITEMS", it.size.toString())
                    (recycler_view.adapter as PostPageAdapter).submitList(it)
                }, {
                    it.stackTrace
                })
        mDisposable.add(pagedItems)
        recycler_view.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        mDisposable.clear()
    }

    private fun goToWritePostFragment(){
        val navDirections = BoardFragmentDirections.actionCommunityFragmentToPostWriteFragment()
        findNavController().navigate(navDirections)
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
    }
}