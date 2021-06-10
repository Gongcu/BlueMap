package com.bluemap.overcom_blue.view_model

import android.util.Log
import com.bluemap.overcom_blue.ApiAbstract
import com.bluemap.overcom_blue.di.AppModule
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.ui.main.diagnosis.result.map.search.CenterSearchViewModel
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CenterSearchViewModelTest : ApiAbstract<BluemapAPI>(){
    private lateinit var viewModel: CenterSearchViewModel
    private lateinit var repository: Repository
    private lateinit var service: BluemapAPI

    @Before
    fun setup(){
        service = createService(BluemapAPI::class.java)
        repository = Repository(service)
        viewModel = CenterSearchViewModel(repository)
    }

    @Test
    fun getSearchedKeywordCenter() {
        repository.getCenter("강서",0)
            .test()
            .assertNoErrors()
            .assertSubscribed()
            .assertValue {list ->
                print( "getSearchedKeywordCenter: ${list.size}")
                list.isNotEmpty()
            }
            .dispose()

        viewModel.search()
    }
}