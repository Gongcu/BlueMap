package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.NavMainDirections
import com.bluemap.overcom_blue.R
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_btn.setOnClickListener(onClickListener)
        back_text_view.setOnClickListener(onClickListener)
    }


    private val onClickListener= View.OnClickListener {
        val directions = NavMainDirections.actionGlobalCommunityFragment()
        findNavController().navigate(directions)
    }
}