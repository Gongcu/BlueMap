package com.bluemap.overcom_blue.ui.main.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.ui.main.MainActivity
import com.bluemap.overcom_blue.adapter.PostPageAdapter
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.fragment.BoardFragmentDirections
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.PostDataSource
import com.bluemap.overcom_blue.repository.PostDataSourceFactory
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_board.view.*
import kotlinx.android.synthetic.main.item_notice.view.view_count_text_view
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject

@AndroidEntryPoint
class BoardFragment : Fragment() {
    private var post : Post? = null

    @Inject
    lateinit var repository: Repository
    lateinit var adapter: PostPageAdapter
    private val mDisposable = CompositeDisposable()
    private lateinit var noticeObservable : Single<Post>
    private lateinit var pagedItems: Disposable
    private lateinit var builder:RxPagedListBuilder<Int,Post>
    private val config= PagedList.Config.Builder()
        .setPageSize(20)    //Defines the number of items loaded at once from the DataSource.
        .setInitialLoadSizeHint(20) //Defines how many items to load when first load occurs. Default = PAGE SIZE * 3
        .setPrefetchDistance(20)    //Defines how far from the edge of loaded content an access must be to trigger further loading.
        .setEnablePlaceholders(true)    //Pass false to disable null placeholders in PagedLists using this Config.
        .build()

    private var userId : Int = -1
    //Callback when fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PostPageAdapter(requireContext()) {
            goToPostFragment(it)
        }
        userId = (requireActivity().application as BaseApplication).userId
        noticeObservable = repository.getNotice(userId =userId )
        builder = RxPagedListBuilder<Int, Post>(PostDataSourceFactory(userId,repository,mDisposable), config)
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
        view.swipe_refresh_layout.setOnRefreshListener {
            post = null
            initData()
            bindNotice(view)
        }
        bindNotice(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setItemViewCacheSize(20)
        recycler_view.adapter = adapter
    }


    private fun initData(){
        swipe_refresh_layout?.isRefreshing=false //Using another progress bar so the refresh progress bar not required
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
                }, {
                    it.stackTrace
                    Util.progressOffInFragment()
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


    private fun bindNotice(view:View){
        if(post==null){
            val disposable = noticeObservable.subscribe(
                    { post ->
                        this.post = post
                        bindNoticeView(view)
                    }, {
                it.stackTrace
            })
            mDisposable.add(disposable)
        }else{
            bindNoticeView(view)
        }
    }

    private fun bindNoticeView(view:View){
        view.notice_layout.like_count_text_view.text = post!!.likeCount.toString()
        view.notice_layout.comment_count_text_view.text = post!!.commentCount.toString()
        view.notice_layout.view_count_text_view.text = post!!.viewCount.toString()
        if (post!!.like!! == 1)
            view.notice_layout.like_image_view.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
        view.notice_layout.setOnClickListener {
            goToPostFragment(post!!)
        }
    }
    
    private fun goToPostFragment(post:Post){
        val directions = BoardFragmentDirections.actionCommunityFragmentToPostFragment(post.id!!)
        findNavController().navigate(directions)
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
    }
}