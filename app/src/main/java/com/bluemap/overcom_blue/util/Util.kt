package com.bluemap.overcom_blue.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.model.DiagnosisModel
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

object Util {

    fun requestPermission(application: Application){
        TedPermission.with(application)
            .setPermissionListener(LocationPermission())
            .setRationaleMessage("앱을 사용하려면 위치정보가 필요합니다. 다음에 나오는 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다..\n앱을 이용하려면 [설정] > [권한] 에서 권한을 허용하세요.")
            .setPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }
    class LocationPermission() : PermissionListener{
        override fun onPermissionGranted() {
        }
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        }
    }

    fun progressOnInFragment(fragment: Fragment){
        BaseApplication.instance?.progressOnInFragment(fragment)
    }
    fun progressOffInFragment(){
        BaseApplication.instance?.progressOffInFragment()
    }

    fun progressOn(activity: FragmentActivity){
        BaseApplication.instance?.progressOn(activity)
    }
    fun progressOff(){
        BaseApplication.instance?.progressOff()
    }
}