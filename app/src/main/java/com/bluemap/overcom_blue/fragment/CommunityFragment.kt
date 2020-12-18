package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.R
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        write_post_btn.setOnClickListener {
            val navDirections = CommunityFragmentDirections.actionCommunityFragmentToPostWriteFragment()
            findNavController().navigate(navDirections)
        }
    }
}