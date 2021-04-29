package com.bluemap.overcom_blue.ui.main

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController by lazy{
        findNavController(R.id.fragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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