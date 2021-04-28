package com.bluemap.overcom_blue.di

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bluemap.overcom_blue.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun provideNavController(activity: Activity) : NavController{
        return activity.findNavController(R.id.fragment)
    }
}