package com.bluemap.overcom_blue.ui.splash

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "SplashViewModel"


@HiltViewModel
class SplashViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val loginResult = MutableLiveData<Int>(LOGIN_INIT)


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    /**
     * Call the method When Kakao Login success
     */
    fun findOrCreateUser(kakaoId: Int) {
        val disposable = repository.postUser(User(kakaoId)) //kakaoId
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    UserManager.userId = it.id!!
                    if (it.name!!.isBlank())
                        loginResult.value = REGISTER_NEEDED
                    else
                        loginResult.value = it.id
                }, {
                    loginResult.value = LOGIN_FAILED
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