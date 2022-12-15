package com.litmethod.android.devicemanager

import android.content.Intent
import android.os.Bundle
import carbon.widget.RecyclerView.LinearLayoutManager
import com.litmethod.android.BluetoothConnection.DeviceScannerActivity
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityDeviceManagerBinding
import com.litmethod.android.models.GetCustomers.Equipment
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_LIT_AXIS
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_NAME
import com.litmethod.android.utlis.AppConstants.Companion.DEVICE_STRENGTH_MACHINE
import com.litmethod.android.utlis.AppConstants.Companion.LIT_AXIS_DEVICE_ID
import com.litmethod.android.utlis.AppConstants.Companion.LIT_STRENGTH_DEVICE_ID

class DeviceManagerActivity : BaseActivity(), DeviceManagerAdapter.DeviceAdapterClickListener {
    lateinit var binding: ActivityDeviceManagerBinding
    private lateinit var yourEquipmentAdapter: DeviceManagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        var equipmentListData = BaseResponseDataObject.profilePageData.equipment
        yourEquipmentAdapter = DeviceManagerAdapter(context = this, equipmentListData, this)
        binding.rvDeviceList.apply {
            this.layoutManager = LinearLayoutManager(this@DeviceManagerActivity)
            this.adapter = yourEquipmentAdapter
        }
    }

    override fun onDeviceItemClick(
        position: Int,
        device: Equipment
    ) {

        when (device.id) {
            LIT_AXIS_DEVICE_ID -> {
                val intent = Intent(this@DeviceManagerActivity, DeviceScannerActivity::class.java)
                intent.putExtra(DEVICE_NAME, DEVICE_LIT_AXIS)
                startActivity(intent)
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
            LIT_STRENGTH_DEVICE_ID -> {
                val intent = Intent(this@DeviceManagerActivity, DeviceScannerActivity::class.java)
                intent.putExtra(DEVICE_NAME, DEVICE_STRENGTH_MACHINE)
                startActivity(intent)
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

            }
        }
    }


}