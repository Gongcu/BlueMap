package com.bluemap.overcom_blue.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bluemap.overcom_blue.NavMainDirections
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.adapter.CommentAdapter
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.databinding.FragmentPostBinding
import com.bluemap.overcom_blue.model.Comment
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_post.*


class PostFragment : Fragment() {
    var postId:Int = -1
    var userId:Int = -1
    private val repository: Repository by lazy {
        Repository(requireActivity().application)
    }
    private val adapter: CommentAdapter by lazy{
        CommentAdapter(requireContext(), {
            replyModeOn(it.id!!)
        }, {
            likeComment(adapter.list[it].id!!, it)
        })
    }
    lateinit var binding: FragmentPostBinding
    private val compositeDisposable = CompositeDisposable()
    private val args:PostFragmentArgs by navArgs<PostFragmentArgs>()
    private val imm: InputMethodManager by lazy{
        requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    var parentCommentId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = args.postId
        userId = (requireActivity().application as BaseApplication).userId

        //NETWORKING
        setPost(postId)
        setComment(repository.getComment(postId))
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentPostBinding>(inflater, R.layout.fragment_post, container, false)
        binding.fragment=this@PostFragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


    private fun setComment(observable: Single<List<Comment>>) {
        val disposable =observable
                .subscribe({
                    adapter.setList(ArrayList(it))
                }, {
                    Log.d(TAG, it.message)
                })
        compositeDisposable.add(disposable)
    }

    private fun setPost(postId: Int) {
        val disposable = repository.getPostById(postId).
                doOnSubscribe {
                    Util.progressOnInFragment(this)
                }
                .doFinally {
                    Util.progressOffInFragment()
                }
                .subscribe({
                    if (it.like == 1)
                        like_btn.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
                    binding.model = it
                }, {
                    Log.d(TAG, it.message)
                })
        compositeDisposable.add(disposable)
    }

    fun back() = run {
        requireActivity().onBackPressed()
    }

    fun likePost(postId: Int){
        val disposable = repository.likePost(postId)
            .subscribe { it ->
                if (it) {
                    like_btn.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.model?.like = 1
                    like_count_text_view.text = (like_count_text_view.text.toString().toInt() + 1).toString()
                } else {
                    like_btn.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.deepGray), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.model?.like = 0
                    like_count_text_view.text = (like_count_text_view.text.toString().toInt() - 1).toString()
                }
            }
        compositeDisposable.add(disposable)
    }

    private fun likeComment(commentId: Int, position: Int) {
        val disposable = repository.likeComment(commentId)
            .subscribe { it ->
                if (it) {
                    adapter.list[position].like = 1
                    adapter.list[position].likeCount = adapter.list[position].likeCount!! + 1
                } else {
                    adapter.list[position].like = 0
                    adapter.list[position].likeCount = adapter.list[position].likeCount!! - 1
                }
                adapter.notifyItemChanged(position)
            }
        compositeDisposable.add(disposable)
    }

    fun writeComment(){
        if(parentCommentId==-1)
            setComment(repository.writeComment(postId, Comment(userId, comment_edit_text.text.toString())))
        else
            setComment(repository.writeReplyComment(postId, parentCommentId, Comment(userId, comment_edit_text.text.toString())))
        comment_count_text_view.text = (comment_count_text_view.text.toString().toInt()+1).toString()
        parentCommentId=-1
        comment_edit_text.hint = "댓글을 입력하세요."
        imm.hideSoftInputFromWindow(comment_edit_text.windowToken, 0)
        comment_edit_text.setText("")
    }

    private fun replyModeOn(commentId: Int){
        comment_edit_text.requestFocus()
        comment_edit_text.hint = "답글을 입력하세요."
        imm.showSoftInput(comment_edit_text, InputMethodManager.SHOW_IMPLICIT)
        parentCommentId=commentId
    }

    companion object{
        const val TAG="POST_FRAGMENT"
    }
}