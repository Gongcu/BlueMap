package com.bluemap.overcom_blue.ui.main.board.write_post

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.FragmentPostWriteBinding
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.ui.main.MainActivity
import com.bluemap.overcom_blue.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_post_write.*
import javax.inject.Inject

@AndroidEntryPoint
class PostWriteFragment : Fragment() {
    val viewModel : PostWriteViewModel by viewModels()

    private lateinit var binding: FragmentPostWriteBinding
    private val imm: InputMethodManager by lazy{
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).main_bottom_navigation.visibility=View.GONE

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentPostWriteBinding>(inflater,R.layout.fragment_post_write,container,false)
        binding.fragment = this@PostWriteFragment
        binding.viewModel = viewModel

        viewModel.writeFinish.observe(viewLifecycleOwner,{
            if(it)
                backToBoard()
            else
                Toast.makeText(requireActivity(),"제목, 내용을 입력해주세요.",Toast.LENGTH_LONG).show()
        })

        return binding.root
    }

    fun backToBoard() = run {
        imm.hideSoftInputFromWindow(title_edit_text_view.windowToken, 0)
        imm.hideSoftInputFromWindow(content_edit_text_view.windowToken, 0)
        requireActivity().onBackPressed()
    }
}