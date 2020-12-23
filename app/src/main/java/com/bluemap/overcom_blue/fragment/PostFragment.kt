package com.bluemap.overcom_blue.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.NavMainDirections
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.adapter.CommentAdapter
import com.bluemap.overcom_blue.databinding.FragmentPostBinding
import com.bluemap.overcom_blue.model.Comment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_post.*


class PostFragment : Fragment() {
    var postId:Int = -1
    var userId:Int = -1
    lateinit var repository: Repository
    lateinit var adapter: CommentAdapter
    lateinit var binding: FragmentPostBinding

    private val args:PostFragmentArgs by navArgs<PostFragmentArgs>()
    private val imm: InputMethodManager by lazy{
        requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    var parentCommentId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = Repository(requireActivity().application)
        adapter = CommentAdapter(requireContext(), {
            replyModeOn(it.id!!)
        },{
            Log.d("click", adapter.list[it].toString())
            likeComment(adapter.list[it].id!!,it)
        })
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentPostBinding>(inflater,R.layout.fragment_post,container,false)
        binding.fragment=this@PostFragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = args.postId
        userId = (requireActivity().application as BaseApplication).userId

        recycler_view.adapter = adapter

        setPost(postId)

        setComment(repository.getComment(postId))
    }


    fun back(){
        val directions = NavMainDirections.actionGlobalCommunityFragment()
        findNavController().navigate(directions)
    }

    private fun setComment(observable : Single<List<Comment>>){
        observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                adapter.setList(ArrayList(it))
            },{
                Log.d(TAG,it.message)
            })
    }

    private fun setPost(postId:Int){
        repository.getPostById(postId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if(it.like == 1)
                    like_btn.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
                binding.model = it
            },{
                Log.d(TAG,it.message)
            })
    }

    fun likePost(postId: Int){
        repository.likePost(postId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
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
    }

    private fun likeComment(commentId: Int, position: Int) {
        repository.likeComment(commentId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
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
    }

    fun writeComment(){
        if(parentCommentId==-1)
            setComment(repository.writeComment(postId, Comment(userId,comment_edit_text.text.toString())))
        else
            setComment(repository.writeReplyComment(postId,parentCommentId,Comment(userId,comment_edit_text.text.toString())))
        parentCommentId=-1
        comment_edit_text.hint = "댓글을 입력하세요."
        imm.hideSoftInputFromWindow(comment_edit_text.windowToken,0)
        comment_edit_text.setText("")
    }

    private fun replyModeOn(commentId: Int){
        comment_edit_text.requestFocus()
        comment_edit_text.hint = "답글을 입력하세요."
        imm.showSoftInput(comment_edit_text,InputMethodManager.SHOW_IMPLICIT)
        parentCommentId=commentId
    }


    companion object{
        const val TAG="POST_FRAGMENT"
    }
}