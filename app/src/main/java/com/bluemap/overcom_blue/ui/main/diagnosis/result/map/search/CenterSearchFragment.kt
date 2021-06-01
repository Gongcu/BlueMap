package com.bluemap.overcom_blue.ui.main.diagnosis.result.map.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.FragmentCenterSearchBinding
import com.bluemap.overcom_blue.model.Center
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_center_search.*


@AndroidEntryPoint
class CenterSearchFragment : Fragment() {
    private val centerSearchViewModel :CenterSearchViewModel by viewModels()
    private val imm: InputMethodManager by lazy{
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private lateinit var adapter : CenterPageAdapter
    private lateinit var binding : FragmentCenterSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CenterPageAdapter {
            moveToMapView(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_center_search, null, false)
        binding.lifecycleOwner = this
        binding.viewModel = centerSearchViewModel
        binding.fragment = this

        centerSearchViewModel.centers.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_edit_text_view.requestFocus()
        recycler_view.adapter = adapter
        imm.showSoftInput(search_edit_text_view, InputMethodManager.SHOW_IMPLICIT)
    }

    val onEditorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            Log.d("ENTER", "ENTER")
            centerSearchViewModel.search()
            true
        } else {
            false
        }
    }

    private fun moveToMapView(center: Center){
        val directions =
                CenterSearchFragmentDirections.actionCenterSearchFragmentToMapFragment(center)
        findNavController().navigate(directions)
    }

}