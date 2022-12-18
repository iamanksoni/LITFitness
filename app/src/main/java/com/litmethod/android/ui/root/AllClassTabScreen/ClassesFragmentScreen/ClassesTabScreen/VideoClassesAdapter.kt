package com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ClassesTabScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
import com.litmethod.android.models.GetClassCatagoryById.Data


class VideoClassesAdapter(
    val result: ArrayList<Boolean>,
    val context: Context,
    val getClassCatagoryByIdResponseList :List<Data>
) : RecyclerView.Adapter<VideoClassesAdapterViewHolder>() {
    lateinit var videoClassesAdapterListener:VideoClassesAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoClassesAdapterViewHolder {
        return VideoClassesAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_video_classes_view,
                p0,
                false
            )
        )
    }

    interface VideoClassesAdapterListener {
        fun onVideoItemClick(position: Int,code:String)
    }

    fun setAdapterListenerVideoClass(videoClassesAdapterListener:VideoClassesAdapterListener) {
        this.videoClassesAdapterListener = videoClassesAdapterListener
    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: VideoClassesAdapterViewHolder, position: Int) {
        var item = getClassCatagoryByIdResponseList[position]
        Log.d("getItem","$position")
        holder.tvVideoTime.text  = item.getDurations.toString()+" MINS"
        holder.tvIntermediate.text = item.getLevelName
        holder.classType.text = item.class_type
        when (item.getLevelName){
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
//        holder.tvIntermediate.setTextColor(Color.parseColor(item.))
        holder.ivNew.visibility = View.VISIBLE
//        holder.ivNew.visibility = if (item.isViewed == true) View.INVISIBLE else View.VISIBLE
        Glide.with(context)
            .load(item.thumbnail)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(holder.ivNew2);



        val firstWord = "${item.title} "
        val secondWord = "with ${item.getInstructor}"
        val spannable: Spannable = SpannableString(firstWord+secondWord)
        spannable.setSpan(ForegroundColorSpan(context.getResources().getColor(R.color.white)), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(context.getResources().getColor(R.color.mono_grey_60)), firstWord!!.length, firstWord!!.length+secondWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.tv_video_title.text = spannable

        holder.itemView.setOnClickListener {
            videoClassesAdapterListener.onVideoItemClick(position,item.id)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class VideoClassesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_video_title: TextView = view.findViewById(R.id.tv_video_title)
    val tvVideoTime:TextView  = view.findViewById(R.id.tv_video_time)
    val tvIntermediate:TextView  = view.findViewById(R.id.tv_intermediate)
    val classType:TextView  = view.findViewById(R.id.tv_class_tp)
    val ivNew:ImageView  = view.findViewById(R.id.iv_new)
    val ivNew2:ImageView  = view.findViewById(R.id.iv_new2)
}