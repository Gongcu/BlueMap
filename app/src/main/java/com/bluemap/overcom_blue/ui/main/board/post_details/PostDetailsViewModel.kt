package com.bluemap.overcom_blue.ui.main.board.post_details

import android.app.Application
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluemap.overcom_blue.model.Comment
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import com.bluemap.overcom_blue.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_post_details.*
import javax.inject.Inject

private const val TAG = "PostDetailsViewModel"


@HiltViewModel
class PostDetailsViewModel @Inject constructor(
        private val repository: Repository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    var parentCommentId = -1

    val post: MutableLiveData<Post> = MutableLiveData<Post>()
    val comments: MutableLiveData<List<Comment>> = MutableLiveData<List<Comment>>()
    val writeCommentFinish: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val reloadSpecificComment : MutableLiveData<Int> = MutableLiveData<Int>()

    init {
        getPost(UserManager.accessPostId)
        getComments(UserManager.accessPostId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun getPost(postId: Int) {
        val disposable = repository.getPostById(postId)
                .subscribe({
                    post.value = it
                }, {
                    Log.d(TAG, it.message)
                })
        compositeDisposable.add(disposable)
    }

    private fun getComments(postId: Int) {
        val disposable =repository.getComment(postId)
                .subscribe({
                    comments.value = it
                }, {
                    Log.d(TAG, it.message)
                })
        compositeDisposable.add(disposable)
    }

    private fun setComments(observable: Single<List<Comment>>) {
        val disposable =observable
                .subscribe({
                    comments.value = it
                }, {
                    Log.d(TAG, it.message)
                })
        compositeDisposable.add(disposable)
    }

    fun likePost(postId: Int){
        val disposable = repository.likePost(postId)
                .subscribe { it ->
                    post.value = post.value?.copy(like=it.compareTo(false))
                }
        compositeDisposable.add(disposable)
    }

    fun likeComment(commentId: Int, position: Int) {
        val disposable = repository.likeComment(commentId)
                .subscribe { it ->
                    if (it) {
                        comments.value?.get(position)?.like = 1
                        comments.value?.get(position)?.likeCount = comments.value?.get(position)?.likeCount!! + 1
                    } else {
                        comments.value?.get(position)?.like = 0
                        comments.value?.get(position)?.likeCount = comments.value?.get(position)?.likeCount!! - 1
                    }
                    reloadSpecificComment.value = position // -> notify to adapter have to change a item
                }
        compositeDisposable.add(disposable)
    }

    //댓글 작성 완료 버튼 클릭
    fun writeComment(comment: String){
        if(parentCommentId==-1)
            setComments(repository.writeComment(UserManager.accessPostId, Comment(UserManager.userId,comment)))
        else
            setComments(repository.writeReplyComment(UserManager.accessPostId, parentCommentId, Comment(UserManager.userId, comment)))
        post.value?.commentCount = post.value?.commentCount!! + 1
        parentCommentId=-1

        writeCommentFinish.value = true
    }


    //KeyboardUp
    fun onKeyboardUp(){
        Log.i(TAG,"KEYBOARD UP")
        writeCommentFinish.value = false
    }
}