package com.bluemap.overcom_blue.view_model

import com.bluemap.overcom_blue.ApiAbstract
import com.bluemap.overcom_blue.di.AppModule
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.ui.main.board.write_post.PostWriteViewModel
import com.bluemap.overcom_blue.ui.registration.RegistrationViewModel
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.Verifier
import org.mockito.Mockito.*

class RegistrationViewModelTest : ApiAbstract<BluemapAPI>(){
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var repository: Repository
    private lateinit var service : BluemapAPI

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val mockUrl = "http://localhost:${mockWebServer.port}"
        service = createService(BluemapAPI::class.java,mockUrl)
        repository = Repository(service)
        viewModel = RegistrationViewModel(repository)
    }

    @Test
    fun patchNickname() {
        val mockUser = mock(User::class.java)
        val responseUser = User(1,"Gongcu",true,0)

        enqueueResponse("patchNickname.json")
        enqueueResponse("patchNickname.json")

        repository.patchNickname(mockUser)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue {
                    it == responseUser
                }
                .dispose()

        viewModel.fetchNickname(anyString())
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}