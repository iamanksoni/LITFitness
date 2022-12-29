package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.ClassDetails.EquipmentVideo

class ClassesCoverDeviceVideoAdapter(
    val result: ArrayList<EquipmentVideo>,
    val context: Context,
) : RecyclerView.Adapter<ClassesCoverDeviceVideoViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClassesCoverDeviceVideoViewHolder {
        return ClassesCoverDeviceVideoViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_classes_device_video,
                p0,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ClassesCoverDeviceVideoViewHolder, position: Int) {
        val item = result[position]
        Glide.with(context)
            .load(item.video_thumbnail)
            .into(holder.iv_video)
        holder.tv_header1.text = item.title
        holder.tv_sub_header.text = "${item.video_duration}"

    }

    override fun getItemCount(): Int {
        return result.size
    }
}
class ClassesCoverDeviceVideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tv_header1 = view.findViewById(R.id.tv_header1) as TextView
    val tv_sub_header = view.findViewById(R.id.tv_sub_header) as TextView
    val iv_video = view.findViewById(R.id.iv_video) as ImageView


}