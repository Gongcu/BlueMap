package com.bluemap.overcom_blue.view_model

import android.util.Log
import com.bluemap.overcom_blue.ApiAbstract
import com.bluemap.overcom_blue.di.AppModule
import com.bluemap.overcom_blue.model.Location
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.ui.main.board.BoardViewModel
import com.bluemap.overcom_blue.ui.main.diagnosis.result.map.MapVIewModel
import com.bluemap.overcom_blue.ui.main.diagnosis.result.map.search.CenterSearchViewModel
import com.bluemap.overcom_blue.util.LocationService
import com.naver.maps.geometry.LatLng
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BoardViewModelTest : ApiAbstract<BluemapAPI>(){
    private lateinit var viewModel: BoardViewModel
    private lateinit var repository: Repository
    private lateinit var service : BluemapAPI

    @Before
    fun setup(){
        service = createService(BluemapAPI::class.java,AppModule.BASE_URL)
        repository = Repository(service)
        viewModel = BoardViewModel(repository)
    }

    @Test
    fun getPostList() {
        val defaultOffset = 0
        repository.getPostList(defaultOffset)
            .test()
            .assertSubscribed()
            .assertNoErrors()
            .assertValue {list ->
                print( "getPostList: ${list.size}")
                list.isNotEmpty()
            }
            .dispose()


        viewModel.loadPosts()

    }

    @Test
    fun getNotice(){
        repository.getNotice()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue {post ->
                    print( "getNotice: ${post.toString()}")
                    post.id==1
                }
                .dispose()
        viewModel.getNotice()
    }
}