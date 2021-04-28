package com.bluemap.overcom_blue.databinding

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluemap.overcom_blue.R

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
}