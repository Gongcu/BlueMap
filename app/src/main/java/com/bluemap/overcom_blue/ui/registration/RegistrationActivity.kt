package com.bluemap.overcom_blue.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ActivityRegistrationBinding
import com.bluemap.overcom_blue.ui.main.MainActivity
import com.bluemap.overcom_blue.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_center_search.*
import kotlinx.android.synthetic.main.fragment_center_search.search_edit_text_view

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private val viewModel : RegistrationViewModel by viewModels()
    private lateinit var binding : ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_registration)
        binding.viewModel = viewModel

        viewModel.user.observe(this,{
            startMainActivity()
        })

    }

    private fun startMainActivity(){
        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
        startActivity(intent)
        this@RegistrationActivity.finish()
    }
}