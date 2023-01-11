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
import com.litmethod.android.models.VideoXXX

class ShowAllVideoAdapterHome(
    val result: ArrayList<VideoXXX>,
    val context: Context,
) : RecyclerView.Adapter<ShowAllVideoAdapterHomeViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ShowAllVideoAdapterHomeViewHolder {
        return ShowAllVideoAdapterHomeViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_all_video,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowAllVideoAdapterHomeViewHolder, position: Int) {
        var item = result[position]
        holder.tv_video_title.text = item.title
        holder.tv_sub_min.text = item.getDurations
        holder.tv_sub_level.text = item.getLevelName
        Glide.with(context)
            .load(item.thumbnail)
            .into(holder.iv_video)

        holder.itemView.setOnClickListener {
            showAllClickListener.onItemClickListener(position, item.id)
        }
    }

    interface ShowAllClickListener {
        fun onItemClickListener(position: Int,attribCode:String)
    }
    companion object{
        lateinit var showAllClickListener: ShowAllClickListener

        fun setAdapterListener(showAllClickListener: ShowAllClickListener) {
            this.showAllClickListener = showAllClickListener
        }


    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class ShowAllVideoAdapterHomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_video_title: TextView = view.findViewById(R.id.tv_header1)
    val tv_sub_min: TextView = view.findViewById(R.id.tv_sub_min)
    val tv_sub_level: TextView = view.findViewById(R.id.tv_sub_level)
    val iv_video: ImageView = view.findViewById(R.id.iv_video)
}