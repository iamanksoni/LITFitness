package com.litmethod.android.devicemanager

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.BluetoothConnection.LitDeviceConstants
import com.litmethod.android.R
import com.litmethod.android.models.GetCustomers.Equipment
import com.litmethod.android.utlis.AppConstants

class DeviceManagerAdapter(
    val context: Context,
    val equipmentData: List<Equipment>,
    val deviceClickListener: DeviceAdapterClickListener
) : RecyclerView.Adapter<DeviceManagerAdapter.DeviceManagerAdapterViewHolder>() {

    lateinit var yourEquipmentAdapterListener: DeviceAdapterClickListener
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): DeviceManagerAdapterViewHolder {
        return DeviceManagerAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_workout_device,
                p0,
                false
            )
        )
    }

    interface DeviceAdapterClickListener {
        fun onDeviceItemClick(position: Int, data: Equipment)
        fun onUnpairRequest(position: Int, data: Equipment)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(
        holder: DeviceManagerAdapterViewHolder,
        position: Int
    ) {
        var item = equipmentData[position]
        holder.tv_device_name.text = item.title
        holder.btn_pair.setOnClickListener {
            deviceClickListener.onDeviceItemClick(position, item)
        }
        if (LitDeviceConstants.mLitAxisDevicePair != null && LitDeviceConstants.mLitAxisDevicePair?.rightLitAxisDevice != null && LitDeviceConstants.mLitAxisDevicePair?.rightLitAxisDevice != null) {
            if (item.id == AppConstants.LIT_STRENGTH_DEVICE_ID) {
                holder.btn_pair.text = "Forget"
                deviceClickListener.onUnpairRequest(position, item)
                holder.btn_pair.setBackgroundColor(ContextCompat.getColor(context,R.color.red))
            }
        }
    }

    override fun getItemCount(): Int {
        return equipmentData.size
    }


    class DeviceManagerAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_device_name: TextView = view.findViewById(R.id.device_name)
        val btn_pair: Button = view.findViewById(R.id.devicePairButton)

    }
}