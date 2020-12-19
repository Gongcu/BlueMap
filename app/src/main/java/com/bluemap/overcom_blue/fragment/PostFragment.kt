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
import com.bluemap.overcom_blue.model.Post
import kotlinx.android.synthetic.main.fragment_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostFragment : Fragment() {
    lateinit var repository: Repository
    lateinit var adapter: CommentAdapter
    lateinit var post: Post
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
            //댓글 대댓글 클릭
            comment_edit_text.requestFocus()
            comment_edit_text.hint = "답글을 입력하세요."
            imm.showSoftInput(comment_edit_text,InputMethodManager.SHOW_IMPLICIT)
            parentCommentId=it.id!!
        },{//댓글 좋아요 클릭
            repository.likeComment(adapter.list[it].id!!).enqueue(object:Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        if(response.body()!!) {
                            adapter.list[it].like = 1
                            adapter.list[it].likeCount = adapter.list[it].likeCount!!+1
                        }
                        else {
                            adapter.list[it].like = 0
                            adapter.list[it].likeCount = adapter.list[it].likeCount!!-1
                        }
                    }
                    adapter.notifyItemChanged(it)
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d(TAG,t.message.toString())
                }
            })
        })
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentPostBinding>(inflater,R.layout.fragment_post,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = args.postId
        val userId = (requireActivity().application as BaseApplication).userId
        recycler_view.adapter = adapter

        repository.getPostById(postId).enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(response.code()==200) {
                    post = response.body()!!
                    if(post.like == 1)
                        like_btn.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
                    binding.model = post
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }
        })

        repository.getComment(postId).enqueue(commentCallback)

        like_btn.setOnClickListener {
            repository.likePost(post.id!!).enqueue(likeCallback)
        }

        write_comment_btn.setOnClickListener {
            if(parentCommentId==-1)
                repository.writeComment(postId, Comment(userId,comment_edit_text.text.toString())).enqueue(commentCallback)
            else
                repository.writeReplyComment(postId,parentCommentId,Comment(userId,comment_edit_text.text.toString())).enqueue(commentCallback)
            parentCommentId=-1
            comment_edit_text.hint = "댓글을 입력하세요."
            imm.hideSoftInputFromWindow(comment_edit_text.windowToken,0)
            comment_edit_text.setText("")
        }

        back_btn.setOnClickListener(onClickListener)
        back_text_view.setOnClickListener(onClickListener)
    }


    private val onClickListener= View.OnClickListener {
        val directions = NavMainDirections.actionGlobalCommunityFragment()
        findNavController().navigate(directions)
    }

    private val commentCallback = object: Callback<List<Comment>>{
        override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
            if(response.code()==200)
                adapter.setList(ArrayList(response.body()!!))
        }

        override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
            Log.d(TAG,t.message.toString())
        }
    }

    private val likeCallback = object:Callback<Boolean>{
        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
            Log.d(TAG,response.body().toString())
            if(response.code()==200){
                val result = response.body()!!
                if(result) {
                    like_btn.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
                    post.like=1
                    like_count_text_view.text=(like_count_text_view.text.toString().toInt()+1).toString()
                }else{
                    like_btn.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deepGray), android.graphics.PorterDuff.Mode.SRC_IN)
                    post.like=0
                    like_count_text_view.text=(like_count_text_view.text.toString().toInt()-1).toString()
                }
            }
        }
        override fun onFailure(call: Call<Boolean>, t: Throwable) {
            Log.d(TAG,t.message.toString())
        }
    }

    companion object{
        const val TAG="POST_FRAGMENT"
    }
}