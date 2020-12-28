package com.bluemap.overcom_blue.util

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
    fun setDiagnosisPaper(list: ArrayList<DiagnosisModel>){
        //1~10
        list.add(DiagnosisModel("나는 슬픔을 느낀다."))
        list.add(DiagnosisModel("나는 앞날에 대해 낙담한다."))
        list.add(DiagnosisModel("나는 실패자라고 느낀다."))
        list.add(DiagnosisModel("나는 일상생활에 불만족스럽다."))
        list.add(DiagnosisModel("나는 죄책감을 자주 느낀다."))
        list.add(DiagnosisModel("나는 벌을 받고 있다고 느낀다."))
        list.add(DiagnosisModel("나는 내 자신에게 자주 실망한다."))
        list.add(DiagnosisModel("나는 내가 다른 사람보다 못하다고 느낀다."))
        list.add(DiagnosisModel("나는 자살을 생각한다."))
        list.add(DiagnosisModel("나는 자주 운다."))

        //11~21
        list.add(DiagnosisModel("나는 전보다 짜증이 많다."))
        list.add(DiagnosisModel("나는 다른 사람들에게 관심이 없다."))
        list.add(DiagnosisModel("나는 결정을 잘 내리지 못한다."))
        list.add(DiagnosisModel("과어에 비해 내 모습이 나빠졌다고 생각한다."))
        list.add(DiagnosisModel("나는 일을 제대로 수행할 수 없다."))
        list.add(DiagnosisModel("나는 잠을 잘 못잔다."))
        list.add(DiagnosisModel("나는 항상 피곤하다."))
        list.add(DiagnosisModel("요즘 식욕이 거의 없다."))
        list.add(DiagnosisModel("과거에 비해 몸무게가 줄었다."))
        list.add(DiagnosisModel("과거에 비해 건강이 염려되어 아무 일도 할 수가 없다."))
        list.add(DiagnosisModel("과거에 비해 성욕이 줄었다."))

    }

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