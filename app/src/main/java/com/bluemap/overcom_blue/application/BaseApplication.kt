package com.bluemap.overcom_blue.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.util.ProgressDialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        instance=this
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun progressOn(view: Activity?) {
        if (view == null || view.isFinishing)
            return

        if (dialog == null || (dialog != null && dialog!!.isShowing)) {
            dialog = AppCompatDialog(view).apply {
                setCancelable(false)
                window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                setContentView(R.layout.loading_dialog)
                show()
            }
        }

        Glide.with(view).load(R.raw.rolling_loader)
                .apply(RequestOptions().override(50, 50))
                .into(dialog!!.findViewById<ImageView>(R.id.load_image_view) as ImageView)
    }

    fun progressOff(){
        if(dialog!=null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

    fun progressOnInFragment(fragment: Fragment?){
        if(fragment==null || fragment.isDetached)
            return

        if(fragmentDialog==null || (fragmentDialog!=null && !fragmentDialog!!.isVisible)) {
            fragmentDialog = ProgressDialogFragment()
            fragmentDialog!!.show(fragment.childFragmentManager,"PROGRESS")
        }
    }


    fun progressOffInFragment(){
        //Log.i("isAdded",fragmentDialog!!.isAdded.toString())
        //Log.i("showsDialog",fragmentDialog!!.showsDialog.toString())

        if(fragmentDialog!=null && fragmentDialog!!.isAdded) {
            fragmentDialog!!.dismiss()
        }
    }

    companion object {
        var instance : BaseApplication? = null
            private set //Only BaseApplication set the instance value
        var dialog: AppCompatDialog? = null
        var fragmentDialog: ProgressDialogFragment? = null
    }

}