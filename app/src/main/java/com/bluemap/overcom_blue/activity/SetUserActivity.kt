package com.bluemap.overcom_blue.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import kotlinx.android.synthetic.main.activity_set_user.*

class SetUserActivity : AppCompatActivity() {
    private val repository:Repository by lazy{
        Repository(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_user)
        login_btn.setOnClickListener {
            if (name_text_view.text.toString().isNotBlank())
                repository.patchNickname(User(BaseApplication.instance!!.userId,name_text_view.text.toString()))
                    .doOnSubscribe { Util.progressOn(this) }
                    .doFinally { Util.progressOff() }
                    .subscribe({
                        startMainActivity(it)
                    }, {
                        Log.d("SET_USER_ACTIVITY", it.message)
                    })
        }
    }

    private fun startMainActivity(user: User){
        val intent = Intent(this@SetUserActivity, MainActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
        this@SetUserActivity.finish()
    }
}