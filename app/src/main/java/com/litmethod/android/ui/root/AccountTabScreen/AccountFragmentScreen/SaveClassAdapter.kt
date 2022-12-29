package com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen

import android.content.Context
import android.graphics.Color
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
import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.Data
import com.litmethod.android.ui.Onboarding.LevelScreen.LevelAdapter

class SaveClassAdapter(
    val result: MutableList<Data>,
    val context: Context
) : RecyclerView.Adapter<SaveClassAdapterViewHolder>() {
    lateinit var saveClassAdapterListener: SaveClassAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SaveClassAdapterViewHolder {
        return SaveClassAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_save_video_classes,
                p0,
                false
            )
        )
    }
    interface SaveClassAdapterListener {
        fun onItemRatingBarClick(position: Int,code:String)
    }
    fun setAdapterListener(saveClassAdapterListener: SaveClassAdapterListener) {
        this.saveClassAdapterListener = saveClassAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: SaveClassAdapterViewHolder, position: Int) {
        var item = result[position]
        Log.d("getItem", "$position")
        holder.tvVideoTime.text = "${item.getDurations.replace('+', ' ')} MINS"
        holder.tvIntermediate.text = item.getLevelName
        holder.tv_video_title.text = item.title
        holder.classType.text = item.class_type
        when (item.getLevelName) {
            "Intermediate" -> {
                holder.tvIntermediate.setTextColor(Color.parseColor("#f95a01"))
            }
            "Beginner" -> {
                holder.tvIntermediate.setTextColor(Color.parseColor("#52cfc5"))
            }
            "Advanced" -> {
                holder.tvIntermediate.setTextColor(Color.parseColor("#ed2124"))
            }

        }
        Glide.with(context)
            .load(item.thumbnail)
            .into(holder.ivNew2)

        holder.simpleRatingBar.setOnClickListener {
            saveClassAdapterListener.onItemRatingBarClick(position,item.id)

        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class SaveClassAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val classType:TextView  = view.findViewById(R.id.tv_class_tp)
    val tv_video_title: TextView = view.findViewById(R.id.tv_video_title)
    val tvVideoTime: TextView = view.findViewById(R.id.tv_video_time)
    val tvIntermediate: TextView = view.findViewById(R.id.tv_intermediate)
    val ivNew2: ImageView = view.findViewById(R.id.iv_new2)
    val simpleRatingBar: ImageView = view.findViewById(R.id.simpleRatingBar)
}