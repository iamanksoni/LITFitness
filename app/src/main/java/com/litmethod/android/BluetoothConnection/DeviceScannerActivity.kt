package com.litmethod.android.BluetoothConnection

import android.os.Bundle
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityDeviceScannerBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_HEART_RATE
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_LIT_AXIS
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_NAME
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_STRENGTH_MACHINE

class DeviceScannerActivity : BaseActivity() {
    lateinit var binding: ActivityDeviceScannerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val deviceName = intent?.getStringExtra(DEVICE_NAME)

        when (deviceName) {
            DEVICE_LIT_AXIS -> {
                Glide.with(this).load(R.drawable.device_lit_axis_animation)
                    .into(binding.ivAnimation)

            }
            DEVICE_HEART_RATE -> {
                Glide.with(this).load(R.drawable.heart_rate).into(binding.ivAnimation)

            }
            DEVICE_STRENGTH_MACHINE -> {
                Glide.with(this).load(R.drawable.strength_machine).into(binding.ivAnimation)

            }
        }
    }


}