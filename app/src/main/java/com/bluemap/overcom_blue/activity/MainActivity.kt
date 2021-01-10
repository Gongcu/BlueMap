package com.bluemap.overcom_blue.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bluemap.overcom_blue.NavMainDirections
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val navController by lazy{
        findNavController(R.id.fragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = intent.getParcelableExtra<User>("user")
        (application as BaseApplication).userId=user.id!!
        Util.requestPermission(application)
        NavigationUI.setupWithNavController(main_bottom_navigation, navController)
    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id == R.id.mapFragment){
            val direction = NavMainDirections.actionGlobalCommunityFragment()
            navController.navigate(direction)
        }else{
            super.onBackPressed()
        }
    }
}