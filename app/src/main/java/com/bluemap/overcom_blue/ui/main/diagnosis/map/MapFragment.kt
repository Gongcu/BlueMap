package com.bluemap.overcom_blue.ui.main.diagnosis.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.fragment.MapFragmentArgs
import com.bluemap.overcom_blue.fragment.MapFragmentDirections
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.repository.Repository
import com.bluemap.overcom_blue.util.Util
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_center_info.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    private var lat: Double = 37.57
    private var lng: Double = 126.97

    @Inject
    lateinit var repository: Repository
    lateinit var centers: List<Center>
    lateinit var naverMap: NaverMap
    private val infoWindow: InfoWindow = InfoWindow()
    private val image: OverlayImage by lazy{
        OverlayImage.fromResource(R.drawable.ic_baseline_place_24)
    }
    private val locationManager by lazy {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val args by navArgs<MapFragmentArgs>()
    private var INIT_TYPE = CURRENT_LOCATION
    private var LOCATION_SERVICE = LOCATION_UNAVAILABLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(args.center != null)
            INIT_TYPE = SEARCH

        when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LOCATION_SERVICE = GPS
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LOCATION_SERVICE = NETWORK
        }

        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(requireContext()) {
            override fun getContentView(infowindow: InfoWindow): View {
                return makeInfoWindow(infoWindow)
            }
        }
        infoWindow.setOnClickListener {
            infoWindowLinkClicked(it)
            true
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.location_text_view.setOnClickListener {
            goToCenterSearchFragment()
        }
        return view
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



        if(INIT_TYPE == CURRENT_LOCATION){
            //Get Current Location. Then, add markers
            if(LOCATION_SERVICE == GPS)
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, Looper.myLooper())
            else
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, Looper.myLooper())
        }else{
            val center = args.center!!
            naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(center.latitude, center.longitude)))
            centers = listOf(center)
            val marker = addMarker(center.latitude, center.longitude, 0)
            infoWindow.open(marker)
        }

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

    private fun infoWindowLinkClicked(overlay: Overlay){
        var url = centers[overlay.tag as Int].homepage
        if(url.isNullOrBlank())
            return
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url";
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun addMarker(lat: Double, lng: Double, position: Int) :Marker{
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
        return marker
    }

    private fun makeInfoWindow(infoWindow: InfoWindow) :View{
        val marker = infoWindow.marker
        val center = centers[marker!!.tag as Int]
        val view = View.inflate(requireContext(), R.layout.dialog_center_info, null)
        infoWindow.tag = marker!!.tag as Int
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

    private fun getCenters(lat: Double, lng: Double){
        val disposable = repository.getCenter(lat, lng)
                .doOnSubscribe {
                    Util.progressOnInFragment(this)
                }
                .doFinally {
                    Util.progressOffInFragment()
                }
            .subscribe{ it ->
                centers = it
                for (i in centers.indices)
                    addMarker(centers[i].latitude, centers[i].longitude, i)
            }
        compositeDisposable.add(disposable)
    }

    private val locationListener = object:LocationListener{
        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                lat = location.latitude
                lng = location.longitude
            }
            naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(lat, lng)))
            getCenters(lat, lng)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {
            getCenters(lat, lng)
        }
    }


    private fun goToCenterSearchFragment() = run{
        if(findNavController().popBackStack(R.id.centerSearchFragment, false)){
            Log.d(TAG, "centerSearchFragment found in backstack")
        }else{
            Log.d(TAG, "noExist")
            val directions = MapFragmentDirections.actionMapFragmentToCenterSearchFragment()
            findNavController().navigate(directions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object{
        private const val CURRENT_LOCATION = 1
        private const val SEARCH = 2
        private const val TAG = "MapFragment"

        private const val LOCATION_UNAVAILABLE = 1000
        private const val GPS = 1001
        private const val NETWORK = 1002
    }

}