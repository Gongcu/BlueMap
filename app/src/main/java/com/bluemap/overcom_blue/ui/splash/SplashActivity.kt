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
import com.bluemap.overcom_blue.repository.Repository
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
                LoginClient.instance.run {
                    if(isKakaoTalkLoginAvailable(this@SplashActivity))
                        loginWithKakaoTalk(this@SplashActivity,callback = callback)
                    else
                        loginWithKakaoAccount(this@SplashActivity,callback = callback)
                }
            }
            else if (user != null) {
                viewModel.findOrCreateUser(user.id.toInt())
            }
        }
    }

    private val callback:(OAuthToken?, Throwable?) -> Unit = { _, error ->
        if (error != null) {
            Log.d(TAG, "kakaoLoginCallback: ${error.message}")
            Toast.makeText(this,"로그인 실패",Toast.LENGTH_LONG).show()
        } else {
            UserApiClient.instance.accessTokenInfo { tokenInfo, _ ->
                viewModel.findOrCreateUser(tokenInfo!!.id.toInt())
            }
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