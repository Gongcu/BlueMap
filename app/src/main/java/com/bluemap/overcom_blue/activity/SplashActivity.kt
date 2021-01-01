package com.bluemap.overcom_blue.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import io.reactivex.disposables.CompositeDisposable

class SplashActivity : AppCompatActivity() {
    private lateinit var repository : Repository
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        repository = Repository(application)

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
                findOrCreateUser(user.id.toInt())
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    val callback:(OAuthToken?,Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("LOGIN_ERORR", error.message)
        } else {
            UserApiClient.instance.accessTokenInfo { tokenInfo, _ ->
                findOrCreateUser(tokenInfo!!.id.toInt())
            }
        }
    }

    private fun findOrCreateUser(kakaoId: Int){
        val disposable = repository.postUser(User(kakaoId)) //kakaoId
            .subscribe({
                BaseApplication.instance!!.userId = it.id!!
                if (it.name!!.isBlank())
                    startSetUserActivity()
                else
                    startMainActivity(it)
            }, {
                Log.d("LOGIN_ACTIVITY", it.toString())
            })
        compositeDisposable.add(disposable)
    }

    private fun startMainActivity(user: User){
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        this@SplashActivity.finish()
    }

    private fun startSetUserActivity(){
        val intent = Intent(this@SplashActivity, SetUserActivity::class.java)
        startActivity(intent)
        this@SplashActivity.finish()
    }


    override fun onBackPressed() {
        //스플래시 화면에서 뒤로가기 불가
    }

}