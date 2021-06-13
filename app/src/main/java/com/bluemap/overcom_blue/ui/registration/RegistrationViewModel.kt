package com.bluemap.overcom_blue.ui.registration

import android.app.Application
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import com.bluemap.overcom_blue.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val repository: Repository
) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val user : MutableLiveData<User> = MutableLiveData()

    val onEditorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            onLoginBtnClick(v.text.toString())
            true
        } else {
            false
        }
    }

    fun fetchNickname(name: String) {
        val disposable = repository.patchNickname(User(UserManager.userId, name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    user.value = it
                }, {
                    Log.d("REGISTRATION_ERROR", it.message)
                })
        compositeDisposable.add(disposable)
    }

    fun onLoginBtnClick(name : String){
        if (name.isNotBlank())
               fetchNickname(name)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}