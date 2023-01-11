package com.foxlabz.statisticvideoplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.foxlabz.statisticvideoplayer.Parsing.DeviceData

class ClassCoverEquipmentAdapter(
    val context: Context,
    val equipmentData: List<DeviceData>
) :
    RecyclerView.Adapter<ClassCoverEquipmentAdapter.ClassCoverEquipmentViewHolder>() {
    lateinit var yourEquipmentAdapterListener: EquipmentAdapterClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassCoverEquipmentViewHolder {
        return ClassCoverEquipmentViewHolder(
            LayoutInflater.from(context!!)
                .inflate(R.layout.item_bluetooth_item, parent, false)
        )
    }

    fun setAdapterListener(yourEquipmentAdapterListener: EquipmentAdapterClickListener) {
        this.yourEquipmentAdapterListener = yourEquipmentAdapterListener
    }

    override fun onBindViewHolder(holder: ClassCoverEquipmentViewHolder, position: Int) {

        var item = equipmentData[position]

        holder.tv_device_name.text=item.title
        if(item.selectedItem!!) {
            holder.iv_tap_connect.text = "CONNECTING"
        }
        else{
            holder.iv_tap_connect.text = "Tap to connect..."
        }

        if(item.connectionStatus!!) {
            holder.iv_tap_connect.text = "CONNECTED"
        }

        holder.iv_tap_connect.setOnClickListener {
            yourEquipmentAdapterListener.onItemEquipClick(position, item.id)
        }

    }

    interface EquipmentAdapterClickListener {
        fun onItemEquipClick(position: Int, data: String)
    }

    override fun getItemCount(): Int {
        return equipmentData.size
    }


    class ClassCoverEquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val iv_tap_connect: TextView = view.findViewById(R.id.tv_tap_connect)
        val tv_device_name: TextView = view.findViewById(R.id.device_name)

    }


}