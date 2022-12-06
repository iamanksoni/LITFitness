package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.TrainerProfileScreen

import android.content.Context
import android.graphics.Color
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
import com.litmethod.android.models.GetInstructorInfo.Video

class TraineerProfileAdapter(
    val result: ArrayList<Video>,
    val context: Context,
) : RecyclerView.Adapter<TraineerProfileAdapterViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TraineerProfileAdapterViewHolder {
        return TraineerProfileAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_trainer_video,
                p0,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TraineerProfileAdapterViewHolder, position: Int) {
    val item = result[position]

        holder.tv_header1.text =item.title
        Glide.with(context)
            .load(item.thumbnail)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(holder.iv_video);
        holder.tv_sub_min.text = item.getDurations
        holder.tv_sub_level.text = item.getLevelName
        when (item.getLevelName){
            "Intermediate" -> {
                holder.tv_sub_level.setTextColor(Color.parseColor("#f95a01"))
            }
            "Beginner" -> {
                holder.tv_sub_level.setTextColor(Color.parseColor("#52cfc5"))
            }
            "Advanced" -> {
                holder.tv_sub_level.setTextColor(Color.parseColor("#ed2124"))
            }

        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}
class TraineerProfileAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val iv_video: ImageView = view.findViewById(R.id.iv_video)
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val tv_sub_min: TextView = view.findViewById(R.id.tv_sub_min)
    val tv_sub_level: TextView = view.findViewById(R.id.tv_sub_level)
}