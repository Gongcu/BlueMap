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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFragment : Fragment() {
    lateinit var repository: Repository
    lateinit var adapter: PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = PostAdapter(requireContext()) {
            val directions = CommunityFragmentDirections.actionCommunityFragmentToPostFragment(it.id!!)
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
        repository.getPostList().enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Log.d("GET:POST",response.body()!!.toString())
                if(response.code()==200){
                    val list =response.body()!!
                    adapter.setList(ArrayList(list))
                    Log.d(TAG,response.body()!!.toString())
                }
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("GET:POST",t.message)
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
        swipe_refresh_layout.setOnRefreshListener {
            repository.getPostList().enqueue(getListCallback)
        }
        write_post_btn.setOnClickListener {
            val navDirections = CommunityFragmentDirections.actionCommunityFragmentToPostWriteFragment()
            findNavController().navigate(navDirections)
            (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE
        }
    }

    private val getListCallback = object: Callback<List<Post>>{
        override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
            Log.d("GET:POST",response.body()!!.toString())
            if(response.isSuccessful) {
                val list =response.body()!!
                adapter.setList(ArrayList(list))
            }
            swipe_refresh_layout.isRefreshing=false
            Log.d(TAG, response.body().toString())
        }
        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            Log.w(TAG, t.message)
            swipe_refresh_layout.isRefreshing=false
        }
    }
    companion object{
        const val TAG = "COMMUNITY_FRAGMENT"
    }
}