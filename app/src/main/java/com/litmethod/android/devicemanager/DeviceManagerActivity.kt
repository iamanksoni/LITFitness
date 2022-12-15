package com.litmethod.android.devicemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityDeviceManagerBinding
import com.litmethod.android.shared.BaseActivity

class DeviceManagerActivity  : BaseActivity() {
    lateinit var binding:ActivityDeviceManagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_manager)
    }
}