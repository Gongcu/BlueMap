package com.bluemap.overcom_blue.ui.main.diagnosis.result.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.DialogCenterInfoBinding
import com.bluemap.overcom_blue.databinding.FragmentMapBinding
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Location
import com.bluemap.overcom_blue.user.UserManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_center_info.view.*

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding : FragmentMapBinding
    private val viewModel : MapVIewModel by viewModels()

    private lateinit var naverMap: NaverMap
    private val args by navArgs<MapFragmentArgs>()
    private val image: OverlayImage = OverlayImage.fromResource(R.drawable.ic_baseline_place_24)
    private val infoWindow = InfoWindow()
    private var selectedCenterShowMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(args.center!=null)
            selectedCenterShowMode=true
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_map,container,false)
        binding.viewModel = viewModel
        binding.fragment = this@MapFragment
        binding.mapView.getMapAsync(this)
        setInfoWindowAdapter(infoWindow)
        return binding.root
    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        if(selectedCenterShowMode){
            val center = args.center!!
            val markers = setMarkers(listOf(center))
            infoWindow.open(markers[0])
            naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(center.latitude, center.longitude)))
        }

        viewModel.newCenters.observe(viewLifecycleOwner, {
            setMarkers(it)
        })

        viewModel.location.observe(viewLifecycleOwner, {
            if(!selectedCenterShowMode)
                naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude)))
        })

        naverMap.addOnCameraIdleListener {
            viewModel.getCentersByLocation(naverMap.cameraPosition.target)
        }
    }



    private fun setInfoWindowAdapter(infoWindow: InfoWindow){
        infoWindow.also { it ->
            it.adapter = object : InfoWindow.DefaultViewAdapter(requireActivity()) {
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

    private fun setMarkers(centers : List<Center>) :ArrayList<Marker>{
        val markers = ArrayList<Marker>()
        for(center in centers) {
            markers.add(Marker().apply {
                tag = center
                isIconPerspectiveEnabled = true
                icon = image
                width = 50
                height = 50
                alpha = 0.6f
                zIndex = 10
                position = LatLng(center.latitude, center.longitude)
                onClickListener = markerClickListener
                map = naverMap
            })
        }
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

    private fun moveToLink(overlay: Overlay){
        val center = overlay.tag as Center
        var url = center.homepage
        if(url.isBlank())
            return
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url";
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        requireActivity().startActivity(browserIntent)
    }

    fun goToCenterSearchFragment() = run{
        if(findNavController().popBackStack(R.id.centerSearchFragment, false))
            return
        else{
            val directions = MapFragmentDirections.actionMapFragmentToCenterSearchFragment()
            findNavController().navigate(directions)
        }
    }

    fun moveCameraToCurrentLocation(){
        val mLocation = UserManager.currentLocation
        selectedCenterShowMode= false
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(mLocation.latitude, mLocation.longitude)))
    }

}