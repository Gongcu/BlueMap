package com.bluemap.overcom_blue.ui.main.board.post_details

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.FragmentPostDetailsBinding
import com.bluemap.overcom_blue.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_post_details.*


@AndroidEntryPoint
class PostDetailsFragment : Fragment() {
    private val viewModel : PostDetailsViewModel by viewModels()
    lateinit var binding: FragmentPostDetailsBinding


    private val adapter: CommentAdapter by lazy{
        CommentAdapter(requireContext(), {
            replyModeOn(it.id!!)
        }, {
            viewModel.likeComment(adapter.list[it].id!!, it)
        })
    }

    private val imm: InputMethodManager by lazy{
        requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentPostDetailsBinding>(inflater, R.layout.fragment_post_details, container, false)
        binding.fragment=this@PostDetailsFragment
        binding.viewModel = viewModel


        viewModel.post.observe(viewLifecycleOwner,{
            binding.model = it
        })
        viewModel.comments.observe(viewLifecycleOwner,{
            adapter.setList(ArrayList(it))
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter


        viewModel.reloadSpecificComment.observe(viewLifecycleOwner,{
            adapter.notifyItemChanged(it)
        })

        viewModel.writeCommentFinish.observe(viewLifecycleOwner,{
            if(it){
                comment_edit_text.hint = "댓글을 입력하세요."
                imm.hideSoftInputFromWindow(comment_edit_text.windowToken, 0)
                comment_edit_text.setText("")
            }
        })
    }


    fun back() = run {
        imm.hideSoftInputFromWindow(comment_edit_text.windowToken, 0)
        requireActivity().onBackPressed()
    }

    //아래 함수 리팩토링 필요 parentcommentId를 관찰한 뒤 requestFocust()등 메서드 수행
    private fun replyModeOn(commentId: Int){
        comment_edit_text.requestFocus()
        comment_edit_text.hint = "답글을 입력하세요."
        imm.showSoftInput(comment_edit_text, InputMethodManager.SHOW_IMPLICIT)
        viewModel.parentCommentId=commentId
    }
}