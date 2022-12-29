package com.litmethod.android

import android.app.Application
import com.siliconlabs.bledemo.bluetooth.parsing.Engine

class BaseApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Engine.init(this)
    }
}