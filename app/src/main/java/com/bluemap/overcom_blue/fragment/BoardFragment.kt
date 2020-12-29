package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.activity.MainActivity
import com.bluemap.overcom_blue.adapter.PostAdapter
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardFragment : Fragment() {
    private var REFRESH = false
    lateinit var repository: Repository
    lateinit var adapter: PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = PostAdapter(requireContext()) {
            val directions = BoardFragmentDirections.actionCommunityFragmentToPostFragment(it.id!!)
            findNavController().navigate(directions)
            (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
        }
        repository = Repository(activity!!.application)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.VISIBLE
        setPost()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
        swipe_refresh_layout.setOnRefreshListener {
            REFRESH = true
            setPost()
        }
        write_post_btn.setOnClickListener {
            val navDirections = BoardFragmentDirections.actionCommunityFragmentToPostWriteFragment()
            findNavController().navigate(navDirections)
            (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
        }
    }

    private fun setPost(){
        repository.getPostList()
                .subscribe({
                    adapter.setList(ArrayList(it))
                    if (REFRESH)
                        operateRefresh()

                }, {
                    Log.d("GET:POST", it.message!!)
                    if (REFRESH)
                        operateRefresh()
                })
    }

    private fun operateRefresh(){
        swipe_refresh_layout.isRefreshing = false
        REFRESH=false
    }
}