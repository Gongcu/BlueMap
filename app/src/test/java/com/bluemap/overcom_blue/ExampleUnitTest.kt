package com.bluemap.overcom_blue

import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Location
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleSubscribeOn
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class ApiUnitTest {

    @Mock
    lateinit var bluemapAPI: BluemapAPI //Mock 객체 생성

    @InjectMocks
    lateinit var repository: Repository //Repository에 Mocking한 객체(BluemapAPI,UserManager) 주입

    @Test
    fun getCenters(){
        //MockitoAnnotations.initMocks(this)
        val location  =  Location(37.570975,126.977759)
        val center = mock(Center::class.java)
        val temp = Single.just(listOf(center) as List<Center>)
        Mockito.`when`(bluemapAPI.getCenter(location.latitude,location.longitude)).thenReturn(temp)
        assertThat(repository.getCenter(location),`isA`(Single::class.java))
    }
}