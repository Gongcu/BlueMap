package com.bluemap.overcom_blue.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bluemap.overcom_blue.R

class CustomBackButton(
        context: Context,
        attrs:AttributeSet,
) : LinearLayout(context,attrs){
    init {
        inflate(context,R.layout.custom_back_button,this)
    }
}