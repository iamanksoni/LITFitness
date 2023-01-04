package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.GetEquipment.Data
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentData

class ClassCoverEquipmentAdapter(
    val result: ArrayList<YourEquipmentData>,
    val context: Context,
    val equipmentData: List<Data>
) :
    RecyclerView.Adapter<ClassCoverEquipmentAdapter.ClassCoverEquipmentViewHolder>() {
    lateinit var yourEquipmentAdapterListener: EquipmentAdapterClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassCoverEquipmentViewHolder {
        return ClassCoverEquipmentViewHolder(
            LayoutInflater.from(context!!)
                .inflate(R.layout.item_equipment_class_cover, parent, false)
        )
    }

    fun setAdapterListener(yourEquipmentAdapterListener: EquipmentAdapterClickListener) {
        this.yourEquipmentAdapterListener = yourEquipmentAdapterListener
    }

    override fun onBindViewHolder(holder: ClassCoverEquipmentViewHolder, position: Int) {
        var item = equipmentData[position]
        holder.tv_header1.text = item.title
        Glide.with(context)
            .load(item.image)
            .into(holder.iv_levell);
        holder.rl_level_second.strokeWidth = 0.5f
        val colorInt2 = context.resources.getColor(R.color.mono_grey_60)
        holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt2)
        holder.rl_level_second.background = null
        holder.rl_level_second.backgroundTintList = null
        val colorInt = context.resources.getColor(R.color.white)
        holder.tv_header1.setTextColor(colorInt)
        val colorInt1 = context.resources.getColor(R.color.mono_grey_60)
        holder.tv_sub_header.text = "Tap to connect..."
        holder.tv_sub_header.setTextColor(colorInt1)

        holder.tv_sub_header.setOnClickListener {
            yourEquipmentAdapterListener.onItemEquipClick(position, item.id)
        }
        if(result.get(position).selectedItem){
            holder.tv_sub_header.text = "Connecting ..."
            val colorInt1 = context.resources.getColor(R.color.blue)
            holder.tv_sub_header.setTextColor(colorInt1)
        }

        if(result.get(position).connectionStatus){
            holder.tv_sub_header.text = "Connected"
            val colorInt1 = context.resources.getColor(R.color.red)
            holder.tv_sub_header.setTextColor(colorInt1)
        }



    }

    interface EquipmentAdapterClickListener {
        fun onItemEquipClick(position: Int, data: String)
    }

    override fun getItemCount(): Int {
        return result.size
    }


    class ClassCoverEquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val iv_levell: ImageView = view.findViewById(R.id.iv_level)
        val tv_header1: TextView = view.findViewById(R.id.tv_header1)
        val tv_sub_header: TextView = view.findViewById(R.id.tv_sub_header)
        val rl_level_second: carbon.widget.RelativeLayout = view.findViewById(R.id.rl_level_second)

    }


}