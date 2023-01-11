package com.litmethod.android.ui.Onboarding.YourEquipmentScreen

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.GetEquipment.Data

class YourEquipmentAdapter(
    val result: ArrayList<YourEquipmentData>,
    val context: Context,
    val equipmentData:List<Data>
) : RecyclerView.Adapter<YourEquipmentAdapterViewHolder>() {
    lateinit var yourEquipmentAdapterListener: YourEquipmentAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): YourEquipmentAdapterViewHolder {
        return YourEquipmentAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_equipment_class_cover,
                p0,
                false
            )
        )
    }

    interface YourEquipmentAdapterListener {
        fun onItemEquipClick(position: Int,data:String)
    }

    fun setAdapterListener(yourEquipmentAdapterListener: YourEquipmentAdapterListener) {
        this.yourEquipmentAdapterListener = yourEquipmentAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: YourEquipmentAdapterViewHolder, position: Int) {
        var item = equipmentData[position]

        holder.tv_header1.text =item.title
        Glide.with(context)
            .load(item.image)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(holder.iv_levell);

        Log.d("thePic","pic is : ${item.image} and item $item" )
//        holder.tv_header1.text = "LIT Bands"
        if (result[position].selectedItem) {
            val colorInt1 = context.getColor(R.color.black)
            holder.tv_header1.setTextColor(colorInt1)
            holder.rl_level_second.strokeWidth = 3.0f
            val colorInt = context.resources.getColor(R.color.red)
            holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
            holder.rl_level_second.alpha = 1f
            val colorInt2 = context.resources.getColor(R.color.white)
            holder.rl_level_second.background = context.getDrawable(R.drawable.view_background_grey)
            holder.rl_level_second.backgroundTintList = ColorStateList.valueOf(colorInt2)
            val colorInt3 = context.resources.getColor(R.color.red)
            holder.tv_sub_header.text = "selected"
            holder.tv_sub_header.setTextColor(colorInt3)
        } else {
            holder.rl_level_second.strokeWidth = 0.5f
            val colorInt2 = context.resources.getColor(R.color.mono_grey_60)
            holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt2)
            holder.rl_level_second.background = null
            holder.rl_level_second.backgroundTintList = null
            val colorInt = context.resources.getColor(R.color.white)
            holder.tv_header1.setTextColor(colorInt)
            val colorInt1 = context.resources.getColor(R.color.mono_grey_60)
            holder.tv_sub_header.text = "Tap to select"
            holder.tv_sub_header.setTextColor(colorInt1)
        }
        holder.tv_sub_header.setOnClickListener {
            yourEquipmentAdapterListener.onItemEquipClick(position,item.id)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

}

class YourEquipmentAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val iv_levell: ImageView = view.findViewById(R.id.iv_level)
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val tv_sub_header: TextView = view.findViewById(R.id.tv_sub_header)
    val rl_level_second: carbon.widget.RelativeLayout = view.findViewById(R.id.rl_level_second)
}