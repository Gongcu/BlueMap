package com.bluemap.overcom_blue.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import kotlinx.android.synthetic.main.activity_login.*
class LoginActivity : AppCompatActivity() {
    private lateinit var repository :Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        repository = Repository(application)

    }



    private fun startMainActivity(user: User){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
        this@LoginActivity.finish()
    }

    private fun startSetUserActivity(){
        val intent = Intent(this@LoginActivity, SetUserActivity::class.java)
        startActivity(intent)
        this@LoginActivity.finish()
    }
}