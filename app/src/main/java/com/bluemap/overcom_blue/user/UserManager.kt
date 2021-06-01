package com.bluemap.overcom_blue.user

import androidx.lifecycle.MutableLiveData
import com.bluemap.overcom_blue.model.Location
import javax.inject.Singleton

object UserManager {
    var userId : Int = -1
    var accessPostId : Int = -1
    var currentLocation = Location(37.570975,126.977759) //Default : 광화문

}