package com.bluemap.overcom_blue.view_model

import com.bluemap.overcom_blue.ApiAbstract
import com.bluemap.overcom_blue.di.AppModule
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.ui.main.board.write_post.PostWriteViewModel
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.Verifier
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class PostWriteViewModelTest : ApiAbstract<BluemapAPI>(){
    private lateinit var viewModel: PostWriteViewModel
    private lateinit var repository: Repository
    private lateinit var service : BluemapAPI

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val mockUrl = "http://localhost:${mockWebServer.port}"
        service = createService(BluemapAPI::class.java,mockUrl)

        repository = Repository(service)
        viewModel = PostWriteViewModel(repository)
    }

    @Test
    fun writePost() {
        val userId = 0
        val title = "Unit Test"
        val content = "Gongcu is good at Unit Test"
        val mockPost : Post= Post(userId,title,content)

        mockWebServer.enqueue(
                MockResponse()
                        .addHeader("Content-Type","application/json")
        )

        repository.writePost(mockPost)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .dispose()

        viewModel.writePost(mockPost.title,mockPost.content)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}