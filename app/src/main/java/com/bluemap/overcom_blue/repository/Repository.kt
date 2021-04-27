package com.bluemap.overcom_blue.repository


import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Comment
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.network.BluemapAPI
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val bluemapAPI: BluemapAPI
) {

    fun postUser(user: User):Single<User>{
        return bluemapAPI.postUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

    }

    fun patchNickname(user: User):Single<User>{
        return bluemapAPI.patchNickname(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getPostById(postId:Int, userId: Int):Single<Post>{
        return bluemapAPI.getPostById(postId,userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun getComment(postId: Int, userId: Int):Single<List<Comment>>{
        return bluemapAPI.getComment(postId,userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun writeComment(postId: Int,comment:Comment):Single<List<Comment>>{
        return bluemapAPI.writeComment(postId, comment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun writeReplyComment(postId:Int, commentId: Int,comment: Comment): Single<List<Comment>> {
        return bluemapAPI.writeReplyComment(postId,commentId,comment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun likeComment(commentId: Int,userId: Int):Single<Boolean>{
        return bluemapAPI.likeComment(commentId,userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }


    fun writePost(post: Post): Completable {
        return bluemapAPI.writePost(post)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun getNotice(userId: Int):Single<Post> {
        return bluemapAPI.getNotice(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun getPostList(offset:Int,userId: Int):Single<List<Post>> {
        return bluemapAPI.getPostList(userId,offset)
    }

    fun getCenter(lat:Double, lng:Double):Single<List<Center>>{
        return bluemapAPI.getCenter(lat,lng)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
    fun getCenter(search:String, offset:Int):Single<List<Center>>{
        return bluemapAPI.getCenterList(search,offset)
    }

    fun likePost(postId: Int,userId: Int):Single<Boolean>{
        return bluemapAPI.likePost(postId,userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}