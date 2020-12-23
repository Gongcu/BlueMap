package com.bluemap.overcom_blue.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.model.Center
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_center_info.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    private var lat: Double = 37.57
    private var lng: Double = 126.97
    lateinit var repository: Repository
    lateinit var centers: List<Center>
    lateinit var naverMap: NaverMap
    lateinit var infoWindow: InfoWindow
    private val image: OverlayImage by lazy{
        OverlayImage.fromResource(R.drawable.ic_baseline_place_24)
    }
    private val locationManager by lazy {
        context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        repository = Repository(activity!!.application)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_view.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return

        infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(context!!) {
            override fun getContentView(infowindow: InfoWindow): View {
                return makeInfoWindow(infoWindow)
            }
        }

        //Get Current Location. Then, add markers
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,object: LocationListener{
            override fun onLocationChanged(location: Location?) {
                if(location!=null){
                    lat = location.latitude
                    lng = location.longitude
                }
                naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(lat,lng)))
                setCenters(lat,lng)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {
                setCenters(lat,lng)
            }
        }, Looper.myLooper())
    }

    override fun onClick(overlay: Overlay): Boolean {
        if (overlay is Marker) {
            val marker = overlay as Marker
            if (marker.infoWindow != null)
                infoWindow.close()
            else
                infoWindow.open(marker)
            return true
        }
        return false
    }

    private fun addMarker(lat: Double, lng: Double, position: Int) {
        val marker = Marker()
        marker.tag = position
        marker.isIconPerspectiveEnabled = true
        marker.icon = image
        marker.width = 100
        marker.height = 100
        marker.alpha = 0.7f
        marker.position = LatLng(lat, lng)
        marker.zIndex = 10
        marker.onClickListener = this
        marker.map = naverMap
    }

    private fun makeInfoWindow(infoWindow:InfoWindow) :View{
        val marker = infoWindow.marker
        val center = centers[marker!!.tag as Int]
        val view = View.inflate(requireContext(), R.layout.dialog_center_info, null)
        view.agency_name_text_view.text = center.agencyName
        view.phone_text_view.text = center.phone
        view.homepage_text_view.text = center.homepage
        view.address_text_view.text = center.address
        view.specific_address_text_view.text = center.specificAddress
        if (center.homepage == "")
            view.homepage_text_view.height = 0
        if (center.specificAddress == "")
            view.specific_address_text_view.height = 0
        return view
    }

    private fun setCenters(lat:Double,lng:Double){
        repository.getCenter(lat,lng)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe{ it ->
                centers = it
                for (i in centers.indices)
                    addMarker(centers[i].latitude, centers[i].longitude, i)
            }
    }
}