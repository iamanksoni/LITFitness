package com.litmethod.android.ui.root.ShowAllScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.GetAllAccessFilter.Data7

class ShowAllVideoAdapter(
    val result: ArrayList<Data7>,
    val context: Context,
) : RecyclerView.Adapter<ShowAllVideoAdapterViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ShowAllVideoAdapterViewHolder {
        return ShowAllVideoAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_all_video,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowAllVideoAdapterViewHolder, position: Int) {
        var item = result[position]
        holder.tv_video_title.text = item.title
        holder.tv_sub_min.text = item.getDurations
        holder.tv_sub_level.text = item.getLevelName
        Glide.with(context)
            .load(item.thumbnail)
            .into(holder.iv_video);
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class ShowAllVideoAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_video_title: TextView = view.findViewById(R.id.tv_header1)
    val tv_sub_min: TextView = view.findViewById(R.id.tv_sub_min)
    val tv_sub_level: TextView = view.findViewById(R.id.tv_sub_level)
    val iv_video: ImageView = view.findViewById(R.id.iv_video)
}