package com.bluemap.overcom_blue.ui.main.diagnosis.result.map

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

private const val TAG = "MapVIewModel"

@HiltViewModel
class MapVIewModel @Inject constructor(
        application: Application,
        private val repository: Repository,
        private val locationService: LocationService
): AndroidViewModel(application){
    private val context = getApplication<BaseApplication>().applicationContext
    private val centers = MutableLiveData<ArrayList<Center>>()
    val markers = MutableLiveData<ArrayList<Marker>>()
    val location = locationService.mLocation
    private val mDisposable: CompositeDisposable = CompositeDisposable()
    private val image: OverlayImage = OverlayImage.fromResource(R.drawable.ic_baseline_place_24)

    private val infoWindow: InfoWindow = InfoWindow()

    init {
        locationService.requestSingleUpdate()
        getCenters()
        setInfoWindowAdapter()
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    private fun getCenters(){
        locationService.mLocation.observeForever {
            val disposable = repository.getCenter(it)
                    .subscribe { it ->
                        centers.value = ArrayList(it)
                        markers.value = getMarkers(it)
                    }
            mDisposable.add(disposable)
        }
    }

    fun showClickedCenter(center: Center, naverMap: NaverMap){
        val marker = getMarkers(arrayListOf(center))[0]
        marker.map = naverMap
        infoWindow.open(marker)
        location.value = Location(center.latitude,center.longitude)
    }

    private fun makeInfoWindow(infoWindow: InfoWindow) : View {
        val view = View.inflate(context, R.layout.dialog_center_info, null)
        val marker = infoWindow.marker!!
        val center = marker.tag as Center
        view.apply{
            agency_name_text_view.text = center.agencyName
            phone_text_view.text = center.phone
            homepage_text_view.text = center.homepage
            address_text_view.text = center.address
            specific_address_text_view.text = center.specificAddress
            if (center.homepage == "")
                view.homepage_text_view.height = 0
            if (center.specificAddress == "")
                view.specific_address_text_view.height = 0
        }

        infoWindow.tag = marker.tag as Center
        return view
    }

    private fun setInfoWindowAdapter(){
        infoWindow.also { it ->
            it.adapter = object : InfoWindow.DefaultViewAdapter(context) {
                override fun getContentView(infowindow: InfoWindow): View {
                    return makeInfoWindow(infowindow)
                }
            }
            it.setOnClickListener {
                moveToLink(it)
                true
            }
        }
    }


    private fun moveToLink(overlay: Overlay){
        val center = overlay.tag as Center
        var url = center.homepage
        if(url.isBlank())
            return
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url";
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    private fun getMarkers(centers : List<Center>) :ArrayList<Marker>{
        val markers = ArrayList<Marker>()
        for(center in centers)
            markers.add(Marker().apply {
                tag =center
                isIconPerspectiveEnabled = true
                icon = image
                width = 100
                height = 100
                alpha = 0.7f
                zIndex = 10
                position = LatLng(center.latitude, center.longitude)
                onClickListener = markerClickListener
            })
        return markers
    }

    private val markerClickListener = Overlay.OnClickListener { overlay ->
        if (overlay is Marker) {
            val marker = overlay as Marker
            if (marker.infoWindow != null)
                infoWindow.close()
            else
                infoWindow.open(marker)
        }
        true
    }

    fun moveToMyLocation(){
        location.value = UserManager.currentLocation
        Log.d(TAG,location.value.toString())
    }
}