package com.bluemap.overcom_blue.ui.registration

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    application: Application,
    val repository: Repository
) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext

    private val compositeDisposable = CompositeDisposable()

    lateinit var userLiveData : MutableLiveData<User>

    fun fetchNickname(name: String){
        compositeDisposable.add(
            repository.patchNickname(User(BaseApplication.instance!!.userId,name))
            .doOnSubscribe { Util.progressOn(context) }
            .doFinally { Util.progressOff() }
            .subscribe({
                userLiveData = MutableLiveData<User>(it)
            }, {
                Log.d("SET_USER_ACTIVITY", it.message)
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}