package com.bluemap.overcom_blue.databinding

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluemap.overcom_blue.R
import com.naver.maps.map.MapView
import com.naver.maps.map.OnMapReadyCallback

object BindingAdapter {
    /*
    @JvmStatic
    @BindingAdapter("onRefresh")
    fun onRefresh(swipeRefreshLayout: SwipeRefreshLayout, load:()->Unit){
        swipeRefreshLayout.setOnRefreshListener {
            load()
        }
    }*/

    @JvmStatic
    @BindingAdapter("setColorFilter")
    fun setColorFilter(imageView: AppCompatImageView, like:Int){
        if(like==1)
            imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
        else
            imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.deepGray), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    @JvmStatic
    @BindingAdapter("getMapAsync")
    fun getMapAsync(mapview: MapView, callback:OnMapReadyCallback){
        Log.d("NAVER_MAP","ASYNC CALLBACK")
        mapview.getMapAsync(callback)
    }

    @JvmStatic
    @BindingAdapter("setOnEditorActionListener")
    fun setOnEditorActionListener(editText: EditText, listener:TextView.OnEditorActionListener){
        editText.setOnEditorActionListener(listener)
    }
}