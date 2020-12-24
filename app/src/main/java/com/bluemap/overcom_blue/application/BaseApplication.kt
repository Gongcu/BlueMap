package com.bluemap.overcom_blue.application

import android.app.Application
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.util.ProgressDialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BaseApplication : Application(){
    var userId : Int = 0

    override fun onCreate() {
        super.onCreate()
        instance=this
    }


    fun progressOn(view: FragmentActivity?) {
        if (view == null || view.isFinishing)
            return

        if (dialog == null || (dialog != null && dialog!!.isShowing)) {
            dialog = AppCompatDialog(view).apply {
                setCancelable(false)
                window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                setContentView(R.layout.progress_bar)
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
        if(fragmentDialog!=null && fragmentDialog!!.isResumed) {
            fragmentDialog!!.dismiss()
        }
    }

    companion object {
        lateinit var instance : BaseApplication
            private set //Only BaseApplication set the instance value
        var dialog: AppCompatDialog? = null
        var fragmentDialog: ProgressDialogFragment? = null
    }

}