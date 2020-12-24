package com.bluemap.overcom_blue.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.network.Retrofit
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var repository :Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        repository = Repository(application)

        login_btn.setOnClickListener {
            if (name_text_view.text.toString().isNotBlank())
                repository.postUser(User(name_text_view.text.toString()))
                        .doOnSubscribe { Util.progressOn(this) }
                        .doFinally { Util.progressOff() }
                        .subscribe({
                            startMainActivity(it)
                        }, {
                            Log.d("LOGIN_ACTIVITY", it.message)
                        })
        }
    }

    private fun startMainActivity(user: User){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
        this@LoginActivity.finish()
    }
}