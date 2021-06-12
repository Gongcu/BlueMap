package com.bluemap.overcom_blue.view_model

import com.bluemap.overcom_blue.ApiAbstract
import com.bluemap.overcom_blue.di.AppModule
import com.bluemap.overcom_blue.model.Comment
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.ui.main.board.post_details.PostDetailsViewModel
import com.bluemap.overcom_blue.ui.main.board.write_post.PostWriteViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.Verifier
import org.mockito.Mockito.*

class PostDetailsViewModelTest : ApiAbstract<BluemapAPI>(){
    private lateinit var viewModel: PostDetailsViewModel
    private lateinit var repository: Repository
    private lateinit var service : BluemapAPI

    private lateinit var mockService : BluemapAPI
    private lateinit var mockServiceInjectViewModel: PostDetailsViewModel
    private lateinit var mockServiceInjectRepository: Repository

    /**
     * - GET과 같은 단순 조회 메서드는 실제 API 주소 기반 서비스 이용
     * - POST,DELETE,PUT 등은 MOCK API 이용 -> 실제 데이터 삽입, 삭제, 갱신 방지
     * */

    @Before
    fun setup(){
        val url = AppModule.BASE_URL
        val mockUrl = "http://localhost:${mockWebServer.port}"

        service = createService(BluemapAPI::class.java,url)
        repository = Repository(service)
        viewModel = PostDetailsViewModel(repository)

        mockService = createService(BluemapAPI::class.java,mockUrl)
        mockServiceInjectRepository = Repository(mockService)
        mockServiceInjectViewModel = PostDetailsViewModel(mockServiceInjectRepository)

    }
    
    @Test
    fun getPost(){
        val postId = 1
        repository.getPostById(postId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue { post->
                    print(post)
                    post.title == "공지사항"
                }
                .dispose()
        viewModel.getPost(postId)
    }

    @Test
    fun getComments(){
        val postId = 1
        repository.getComments(postId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue { comments->
                    comments.isNotEmpty()
                }
                .dispose()
        viewModel.getComments(postId)
    }

    @Test
    fun likePost(){
        val postId = 1
        enqueueResponse("likePost.json")
        enqueueResponse("likePost.json")
        mockServiceInjectRepository.likePost(postId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue {
                    print(it)
                    it
                }
                .dispose()

        mockServiceInjectViewModel.likePost(postId)
    }

    @Test
    fun likeComment(){
        val commentId = 1
        val position = 1
        enqueueResponse("likeComment.json")
        enqueueResponse("likeComment.json")
        mockServiceInjectRepository.likeComment(commentId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue {
                    it
                }
                .dispose()

        mockServiceInjectViewModel.likeComment(commentId, position)
    }

    @Test
    fun writeComment(){
        val poseId = 0
        val mockComment = Comment(0,"Gongcu is good at UnitTest")
        enqueueResponse("writeComment.json")
        enqueueResponse("writeComment.json")

        mockServiceInjectRepository.writeComment(poseId, mockComment)
            .test()
            .assertSubscribed()
            .assertNoErrors()
            .assertValue {
                it.size==6
            }
            .dispose()

        mockServiceInjectViewModel.writeComment(mockComment.comment)
    }

    @Test
    fun writeReplyComment() {
        val poseId = 0
        val commentId = 0
        val mockComment = Comment(0,"Gongcu is good at UnitTest")
        enqueueResponse("writeComment.json")
        enqueueResponse("writeComment.json")

        mockServiceInjectRepository.writeReplyComment(poseId,commentId,mockComment)
            .test()
            .assertSubscribed()
            .assertNoErrors()
            .assertValue {
                it.size==6
            }
            .dispose()

        mockServiceInjectViewModel.writeComment(mockComment.comment)

    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}