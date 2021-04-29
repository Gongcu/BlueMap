package com.bluemap.overcom_blue.ui.main.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.ui.main.MainActivity
import com.bluemap.overcom_blue.databinding.FragmentBoardBinding
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.PostDataSource
import com.bluemap.overcom_blue.user.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_board.view.*
import javax.inject.Inject

@AndroidEntryPoint
class BoardFragment : Fragment() {
    private lateinit var binding: FragmentBoardBinding
    val viewModel : BoardViewModel by viewModels()

    @Inject
    lateinit var adapter: PostPageAdapter


    //Callback when fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.posts.observe(this,{
            adapter.submitList(it)
        })
    }

    //Callback first init & return from back stack
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.VISIBLE
        binding = DataBindingUtil.inflate<FragmentBoardBinding>(inflater,R.layout.fragment_board,container,false)
        binding.viewModel = viewModel
        binding.fragment = this@BoardFragment
        viewModel.notice.observe(viewLifecycleOwner,{
            binding.notice = it
            bindNoticeView(binding.root, it)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setItemViewCacheSize(20)
        recycler_view.adapter = adapter
    }


    override fun onStop() {
        super.onStop()
        PostDataSource.offset = 0
    }

    fun goToWritePostFragment(){
        val navDirections = BoardFragmentDirections.actionCommunityFragmentToPostWriteFragment()
        findNavController().navigate(navDirections)
    }


    private fun bindNoticeView(view:View, post:Post){
        view.notice_layout.setOnClickListener {
            goToPostFragment(post)
        }
    }
    
    private fun goToPostFragment(post:Post){
        UserManager.accessPostId = post.id!!
        val directions = BoardFragmentDirections.actionCommunityFragmentToPostFragment(post.id!!)
        findNavController().navigate(directions)
    }
}