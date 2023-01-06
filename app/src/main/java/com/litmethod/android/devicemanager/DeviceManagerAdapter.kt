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
import com.litmethod.android.R
import com.litmethod.android.models.GetCustomers.Equipment
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DataPreferenceObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DeviceManagerAdapter(
    val context: Context,
    val equipmentData: List<Equipment>,
    val deviceClickListener: DeviceAdapterClickListener
) : RecyclerView.Adapter<DeviceManagerAdapter.DeviceManagerAdapterViewHolder>() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
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
        scope.launch {

            if (DataPreferenceObject(context).read("rightLitAxis") != null && DataPreferenceObject(
                    context
                ).read("leftLitAxis") != null
            ) {
                if (item.id == AppConstants.DEVICE_LIT_AXIS) {
                    holder.btn_pair.text = "Forget"
                    holder.btn_pair.setOnClickListener {
                        deviceClickListener.onUnpairRequest(position, item)
                    }
                    holder.btn_pair.setBackgroundColor(ContextCompat.getColor(context, R.color.red))

                }
            }
            if (DataPreferenceObject(context).read("hrSensor") != null) {
                if (item.id == AppConstants.DEVICE_HEART_RATE) {
                    holder.btn_pair.text = "Forget"
                    holder.btn_pair.setOnClickListener {
                        deviceClickListener.onUnpairRequest(position, item)
                    }
                    holder.btn_pair.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                }
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