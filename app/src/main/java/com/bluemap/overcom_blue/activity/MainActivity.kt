package com.bluemap.overcom_blue.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bluemap.overcom_blue.application.BaseApplication
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.model.User
import com.bluemap.overcom_blue.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = intent.getParcelableExtra<User>("user")
        (application as BaseApplication).userId=user.id!!
        Util.requestPermission(application)
        NavigationUI.setupWithNavController(main_bottom_navigation, findNavController(R.id.fragment))
    }
}