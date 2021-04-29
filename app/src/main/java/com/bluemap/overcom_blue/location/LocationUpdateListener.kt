package com.bluemap.overcom_blue.location

import com.bluemap.overcom_blue.model.Location

interface LocationUpdateListener {
    fun onSingleLocationUpdate(location: Location)
}