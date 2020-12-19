package com.bluemap.overcom_blue.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.network.Retrofit
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
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
            if(name_text_view.text.toString().isNotBlank())
                repository.postUser(User(name_text_view.text.toString())).enqueue(object: Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if(response.isSuccessful){
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("user",response.body())
                            startActivity(intent)
                            this@LoginActivity.finish()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("LOGIN_ACTIVITY", t.message)
                    }
                })
        }
    }
}