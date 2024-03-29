package com.bluemap.overcom_blue.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.ui.main.MainActivity
import com.bluemap.overcom_blue.ui.registration.RegistrationActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SplashActivity"

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startKakaoLogin()

        viewModel.loginResult.observe(this,{
            when(it){
                SplashViewModel.LOGIN_INIT -> Log.i(TAG,"Start Login")
                SplashViewModel.LOGIN_FAILED -> Toast.makeText(this,"로그인에 실패했습니다.",Toast.LENGTH_LONG).show()
                SplashViewModel.REGISTER_NEEDED -> startRegistrationActivity()
                else -> startMainActivity()
            }
        })
    }

    private fun startKakaoLogin(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = loginCallback)
            }
            else if (user != null) {
                viewModel.findOrCreateUser(user.id.toInt())
            }
        }
    }

    private val loginCallback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패", error)
        }
        else if (token != null) {
            Log.d(TAG, "startKakaoLogin: ${token.accessToken} 성공")
        }
    }


    private fun startMainActivity(){
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        this@SplashActivity.finish()
    }

    private fun startRegistrationActivity(){
        val intent = Intent(this@SplashActivity, RegistrationActivity::class.java)
        startActivity(intent)
        this@SplashActivity.finish()
    }

    override fun onBackPressed() {
        //스플래시 화면에서 뒤로가기 불가
    }
}