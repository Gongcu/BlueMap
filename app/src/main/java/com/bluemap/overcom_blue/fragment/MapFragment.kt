package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.Repository
import com.bluemap.overcom_blue.model.Center
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.dialog_center_info.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(),OnMapReadyCallback, Overlay.OnClickListener{
    lateinit var repository:Repository
    lateinit var centers: List<Center>
    lateinit var naverMap: NaverMap
    lateinit var infoWindow: InfoWindow
    lateinit var image:OverlayImage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository= Repository(activity!!.application)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_view.getMapAsync(this)
        image = OverlayImage.fromResource(R.drawable.ic_baseline_place_24)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        infoWindow = InfoWindow()
        infoWindow.adapter = object: InfoWindow.DefaultViewAdapter(context!!){
            override fun getContentView(p0: InfoWindow): View {
                val marker = infoWindow.marker
                val center = centers[marker!!.tag as Int]
                val view = View.inflate(context!!,R.layout.dialog_center_info,null)
                view.agency_name_text_view.text=center.agencyName
                view.phone_text_view.text=center.phone
                view.homepage_text_view.text=center.homepage
                view.address_text_view.text=center.address
                view.specific_address_text_view.text=center.specificAddress
                if(center.homepage=="")
                    view.homepage_text_view.height=0
                if(center.specificAddress=="")
                    view.specific_address_text_view.height=0
                return view
            }
        }
        repository.getCenter(37.57,126.97).enqueue(object: Callback<List<Center>>{
            override fun onResponse(call: Call<List<Center>>, response: Response<List<Center>>) {
                if(response.isSuccessful){
                    centers = response.body()!!
                    Log.d("GET:CENTER",response.body().toString())
                    for(i in centers!!.indices){
                        addMarker(centers[i].latitude,centers[i].longitude,i)
                    }
                }
            }

            override fun onFailure(call: Call<List<Center>>, t: Throwable) {
                Log.d("GET:CENTER",t.message)
            }
        })
    }

    override fun onClick(overlay: Overlay): Boolean {
        if(overlay is Marker){
            val marker = overlay as Marker
            if(marker.infoWindow!=null) {
                infoWindow.close()
            }
            else {

                infoWindow.open(marker)
            }
            return true
        }
        return false
    }

    private fun addMarker(lat:Double, lng:Double,position:Int){
        val marker = Marker()
        marker.tag=position
        marker.isIconPerspectiveEnabled=true
        marker.icon= image
        marker.width=100
        marker.height=100
        marker.alpha=0.7f
        marker.position= LatLng(lat,lng)
        marker.zIndex=10
        marker.onClickListener = this
        marker.map = naverMap
    }
}