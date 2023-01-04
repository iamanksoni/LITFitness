package com.litmethod.android.devicemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import carbon.widget.RecyclerView.LinearLayoutManager
import com.litmethod.android.BluetoothConnection.DeviceScannerActivity
import com.litmethod.android.BluetoothConnection.LitDeviceConstants
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityDeviceManagerBinding
import com.litmethod.android.models.GetCustomers.Equipment
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_HEART_RATE
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_LIT_AXIS
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_NAME
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_STRENGTH_MACHINE
import com.litmethod.android.utlis.AppConstants.Companion.LIT_STRENGTH_DEVICE_ID
import com.litmethod.android.utlis.DataPreferenceObject
import com.welie.blessed.ConnectionFailedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DeviceManagerActivity : BaseActivity(), DeviceManagerAdapter.DeviceAdapterClickListener {

    lateinit var binding: ActivityDeviceManagerBinding
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var yourEquipmentAdapter: DeviceManagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        var equipmentListData = arrayListOf<Equipment>()
        var hearRateSensor = Equipment("LIT Hear Rate Sensor", DEVICE_HEART_RATE, "", "HEART RATE")
        var strengthMachine =
            Equipment("LIT Strength Machine", LIT_STRENGTH_DEVICE_ID, "", "LIT STRENGTH MACHINE")
        var litAxis = Equipment("LIT axis Machine", DEVICE_LIT_AXIS, "", "LIT AXIS™")
        equipmentListData.add(hearRateSensor)
        equipmentListData.add(strengthMachine)
        equipmentListData.add(litAxis)
        yourEquipmentAdapter = DeviceManagerAdapter(context = this, equipmentListData, this)
        binding.rvDeviceList.apply {
            this.layoutManager = LinearLayoutManager(this@DeviceManagerActivity)
            this.adapter = yourEquipmentAdapter
        }
        binding.ibBackButton.setOnClickListener {
            finish()
        }
    }

    override fun onDeviceItemClick(
        position: Int,
        device: Equipment
    ) {

        when (device.id) {
            DEVICE_LIT_AXIS -> {
                val intent = Intent(this@DeviceManagerActivity, DeviceScannerActivity::class.java)
                intent.putExtra(DEVICE_NAME, DEVICE_LIT_AXIS)
                startActivity(intent)
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
            DEVICE_STRENGTH_MACHINE -> {
                val intent = Intent(this@DeviceManagerActivity, DeviceScannerActivity::class.java)
                intent.putExtra(DEVICE_NAME, DEVICE_STRENGTH_MACHINE)
                startActivity(intent)
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

            }
            DEVICE_HEART_RATE -> {
                val intent = Intent(this@DeviceManagerActivity, DeviceScannerActivity::class.java)
                intent.putExtra(DEVICE_NAME, DEVICE_HEART_RATE)
                startActivity(intent)
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

            }
        }
    }

    override fun onResume() {
        super.onResume()
        yourEquipmentAdapter.notifyDataSetChanged()
    }

    override fun onUnpairRequest(position: Int, device: Equipment) {
        when (device.id) {
            DEVICE_LIT_AXIS -> {
                scope.launch {
                    DataPreferenceObject(this@DeviceManagerActivity).deleteKey(AppConstants.leftListAxisKey)
                    DataPreferenceObject(this@DeviceManagerActivity).deleteKey(AppConstants.rightListAxisKey)
                }
            }
            DEVICE_STRENGTH_MACHINE -> {
                scope.launch {
                    DataPreferenceObject(this@DeviceManagerActivity).deleteKey(AppConstants.leftListAxisKey)
                }
            }
            DEVICE_HEART_RATE -> {
                scope.launch {
                    DataPreferenceObject(this@DeviceManagerActivity).deleteKey(AppConstants.heartRateKey)
                }
            }
        }

    }


}