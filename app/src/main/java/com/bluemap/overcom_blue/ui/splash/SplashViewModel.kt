package com.bluemap.overcom_blue.ui.splash

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "SplashViewModel"


@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    val repository: Repository
) : AndroidViewModel(application){

    //https://stackoverflow.com/questions/51451819/how-to-get-context-in-android-mvvm-viewmodel
    private val context = getApplication<Application>().applicationContext

    private val compositeDisposable = CompositeDisposable()

    val loginResult = MutableLiveData<Int>(LOGIN_INIT)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun kakaoLogin(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                LoginClient.instance.run {
                    if(isKakaoTalkLoginAvailable(context))
                        loginWithKakaoTalk(context,callback = callback)
                    else
                        loginWithKakaoAccount(context,callback = callback)
                }
            }
            else if (user != null) {
                findOrCreateUser(user.id.toInt())
            }
        }
    }

    private val callback:(OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("LOGIN_ERORR", error.message)
            loginResult.value = LOGIN_FAILED
        } else {
            UserApiClient.instance.accessTokenInfo { tokenInfo, _ ->
                findOrCreateUser(tokenInfo!!.id.toInt())
            }
        }
    }

    /**
     * Call the method When Kakao Login success
     */
    private fun findOrCreateUser(kakaoId: Int){
        val disposable = repository.postUser(User(kakaoId)) //kakaoId
            .subscribe({
                BaseApplication.instance!!.userId = it.id!!
                UserManager.userId = it.id!!
                if (it.name!!.isBlank())
                    loginResult.value = REGISTER_NEEDED
                else
                    loginResult.value = it.id
            }, {
                Log.d(TAG, it.toString())
            })
        compositeDisposable.add(disposable)
    }

    companion object {
        const val LOGIN_INIT = 0
        const val LOGIN_FAILED = -200
        const val REGISTER_NEEDED = -100
    }
}