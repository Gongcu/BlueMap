package com.bluemap.overcom_blue.ui.main.board.write_post

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import com.bluemap.overcom_blue.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_post_write.*
import javax.inject.Inject

@HiltViewModel
class PostWriteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){

    private val mDisposable  = CompositeDisposable()
    val writeFinish = MutableLiveData<Boolean>(false)

    fun writePost(title:String,content:String){
        if(title.isBlank() || content.isBlank()) {
            writeFinish.value = false
            return
        }

        val post = Post(UserManager.userId,title,content)
        val disposable = repository.writePost(post)
            .subscribe({
                writeFinish.value = true
            }, {
                writeFinish.value = false
                Log.e("POST_WRITE", it.toString())
            })
        mDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }
}