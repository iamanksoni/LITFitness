package com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.litmethod.android.R
import com.litmethod.android.models.Video
import com.litmethod.android.ui.Onboarding.LevelScreen.LevelAdapter


class VideoGetStartedAdapter(
    val result: MutableList<Video>,
    val context: Context,
) : RecyclerView.Adapter<VideoGetStartedAdapterViewHolder>() {
    lateinit var videoGetStartedAdapterListener: VideoGetStartedAdapterListener
    var adapter: RecyclerView.Adapter<*>? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoGetStartedAdapterViewHolder {
        adapter = this
        return VideoGetStartedAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_programs_cover_child,
                p0,
                false
            )
        )
    }

    interface VideoGetStartedAdapterListener {
        fun onVideoFileItemClick(position: Int,videoUrl:String)
    }
    fun setAdapterListener(videoGetStartedAdapterListener: VideoGetStartedAdapterListener) {
        this.videoGetStartedAdapterListener = videoGetStartedAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: VideoGetStartedAdapterViewHolder, position: Int) {
        val item = result[position]
        Glide.with(context)
            .load(result[position].video_thumbnail)
            .placeholder(R.drawable.ic_classes)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    @Nullable transition: Transition<in Drawable?>?
                ) {
                    holder.rl_level_second.setBackground(resource)
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {
                    holder.rl_level_second.setBackground(placeholder)
                }
            })

        holder.tv_video_title.text = result[position].video_title
        holder.tv_video_time.text = result[position].video_duration + " MINS"
        holder.itemView.setOnClickListener {

            videoGetStartedAdapterListener.onVideoFileItemClick(position,item.video_file)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class VideoGetStartedAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val rl_level_second: RelativeLayout = row.findViewById(R.id.rl_level_second)
    val tv_video_title: TextView = row.findViewById(R.id.tv_video_title)
    val tv_video_time: TextView = row.findViewById(R.id.tv_video_time)
    val tv_intermediate: TextView = row.findViewById(R.id.tv_intermediate)
}