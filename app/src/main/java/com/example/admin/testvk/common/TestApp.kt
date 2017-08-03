package com.example.admin.testvk.common

import android.app.Application
import com.vk.sdk.VKSdk

class TestApp : Application() {

    companion object {
        val USERIDKEY = "userid"
    }

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}