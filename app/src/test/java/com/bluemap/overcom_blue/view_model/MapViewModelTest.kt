package com.bluemap.overcom_blue.view_model

import android.util.Log
import com.bluemap.overcom_blue.ApiAbstract
import com.bluemap.overcom_blue.di.AppModule
import com.bluemap.overcom_blue.model.Location
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
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

class MapViewModelTest : ApiAbstract<BluemapAPI>(){
    private lateinit var viewModel: MapVIewModel
    private lateinit var repository: Repository
    private lateinit var service : BluemapAPI
    private lateinit var locationService: LocationService

    @Before
    fun setup(){
        locationService = mock(LocationService::class.java)
        service = createService(BluemapAPI::class.java)
        repository = Repository(service)
        viewModel = MapVIewModel(repository,locationService)
    }

    @Test
    fun getCenterList() {
        val latLng = LatLng(37.5, 127.0)
        val location = Location(37.5, 127.0)
        repository.getCenter(location)
            .test()
            .assertSubscribed()
            .assertNoErrors()
            .assertValue {list ->
                print( "getSearchedKeywordCenter: ${list.size}")
                true
            }
            .dispose()

        viewModel.getCentersByLocation(latLng)
    }
}