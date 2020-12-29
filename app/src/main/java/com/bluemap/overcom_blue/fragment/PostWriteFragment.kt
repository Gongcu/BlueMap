package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.NavMainDirections
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.FragmentPostWriteBinding
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_post.back_btn
import kotlinx.android.synthetic.main.fragment_post.back_text_view
import kotlinx.android.synthetic.main.fragment_post_write.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostWriteFragment : Fragment() {
    private lateinit var repository : Repository
    private lateinit var binding: FragmentPostWriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        repository = Repository(activity!!.application)
        binding = DataBindingUtil.inflate<FragmentPostWriteBinding>(inflater,R.layout.fragment_post_write,container,false)
        binding.fragment = this@PostWriteFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = (activity!!.application as BaseApplication).userId
        done_btn.setOnClickListener {
            val post: Post = Post(userId, title_edit_text_view.text.toString(), content_edit_text_view.text.toString())
            repository.writePost(post)
                    .doOnSubscribe {
                        Util.progressOnInFragment(this@PostWriteFragment)
                    }.doFinally {
                        Util.progressOffInFragment()
                    }
                    .subscribe({
                        backToBoard()
                    }, {
                        Toast.makeText(requireActivity(), "게시글 작성 실패.", Toast.LENGTH_SHORT).show()
                        Log.e("POST_WRITE", it.toString())
                    })
        }
    }


    fun backToBoard() = run {
        val directions = NavMainDirections.actionGlobalCommunityFragment()
        findNavController().navigate(directions)
    }


}