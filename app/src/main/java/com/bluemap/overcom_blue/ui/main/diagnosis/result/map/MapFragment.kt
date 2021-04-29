package com.bluemap.overcom_blue.ui.main.diagnosis.result.map

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
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding : FragmentMapBinding
    private val viewModel : MapVIewModel by viewModels()

    private lateinit var naverMap: NaverMap
    private val args by navArgs<MapFragmentArgs>()

    var selectedCenterShowMode = false

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



        return binding.root
    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        if(selectedCenterShowMode){
            //조회한 센터 위치로 이동
            val center = args.center!!
            viewModel.showClickedCenter(center,naverMap)
        }else{
            viewModel.markers.observe(viewLifecycleOwner,{
                for(marker in it)
                    marker.map = naverMap
            })
        }
        viewModel.location.observe(viewLifecycleOwner,{
            naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude)))
        })
    }



    fun goToCenterSearchFragment() = run{
        if(findNavController().popBackStack(R.id.centerSearchFragment, false))
            return
        else{
            val directions = MapFragmentDirections.actionMapFragmentToCenterSearchFragment()
            findNavController().navigate(directions)
        }
    }

}