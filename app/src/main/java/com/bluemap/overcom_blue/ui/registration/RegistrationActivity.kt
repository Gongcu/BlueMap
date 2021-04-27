package com.bluemap.overcom_blue.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.ui.main.MainActivity
import com.bluemap.overcom_blue.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_set_user.*

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private val viewModel : RegistrationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_user)

        login_btn.setOnClickListener {
            if (name_text_view.text.toString().isNotBlank())
                viewModel.fetchNickname(name_text_view.text.toString())
        }

        viewModel.userLiveData.observe(this,{
            startMainActivity(it)
        })
    }

    private fun startMainActivity(user: User){
        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
        this@RegistrationActivity.finish()
    }
}