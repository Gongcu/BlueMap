package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.BaseApplication
import com.bluemap.overcom_blue.NavMainDirections
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.Repository
import com.bluemap.overcom_blue.model.Post
import kotlinx.android.synthetic.main.fragment_post.back_btn
import kotlinx.android.synthetic.main.fragment_post.back_text_view
import kotlinx.android.synthetic.main.fragment_post_write.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostWriteFragment : Fragment() {
    private lateinit var repository :Repository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        repository = Repository(activity!!.application)
        return inflater.inflate(R.layout.fragment_post_write, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = (activity!!.application as BaseApplication).userId
        done_btn.setOnClickListener {
            val post: Post = Post(userId,title_edit_text_view.text.toString(),content_edit_text_view.text.toString())
            repository.writePost(post).enqueue(object: Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.code()==200){
                        val directions = NavMainDirections.actionGlobalCommunityFragment()
                        findNavController().navigate(directions)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context,"게시글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        back_btn.setOnClickListener(onClickListener)
        back_text_view.setOnClickListener(onClickListener)
    }

    private val onClickListener= View.OnClickListener {
        val directions = NavMainDirections.actionGlobalCommunityFragment()
        findNavController().navigate(directions)
    }
}