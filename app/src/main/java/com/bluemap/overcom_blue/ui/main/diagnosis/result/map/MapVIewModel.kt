package com.bluemap.overcom_blue.ui.main.diagnosis.result.map

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Location
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.user.UserManager
import com.bluemap.overcom_blue.util.LocationService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_center_info.view.*
import javax.inject.Inject

@HiltViewModel
class MapVIewModel @Inject constructor(
        private val repository: Repository,
        private val locationService: LocationService
): ViewModel(){
    private val visibleCenter = ArrayList<Center>()
    val newCenters = MutableLiveData<List<Center>>()
    val location = locationService.mLocation
    private val mDisposable: CompositeDisposable = CompositeDisposable()


    init {
        locationService.requestSingleUpdate()
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }


    fun getCentersByLocation(latLng: LatLng){
        val location = Location(latLng.latitude,latLng.longitude)
        val disposable = repository.getCenter(location)
            .subscribe { it ->
                val newVisibleCenters= it.filter { center ->
                    !visibleCenter.contains(center)
                }
                visibleCenter.addAll(newVisibleCenters)
                newCenters.value = newVisibleCenters
            }
        mDisposable.add(disposable)
    }
}