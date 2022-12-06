package com.litmethod.android.ui.Dashboard.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.litmethod.android.R
import com.litmethod.android.models.VideoXXX
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.FilterScreen.FilterChildAdapter

class WorkoutGoalChildAdapter(
    val list: MutableList<VideoXXX>,
    var mContext: Context
) :RecyclerView.Adapter<WorkoutGoalChildAdapterViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkoutGoalChildAdapterViewHolder {
        return WorkoutGoalChildAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_home_workout_child,
                p0,
                false
            )
        )
    }

    interface WorkoutGoalChildAdapterListener {
        fun onItemWorkOutClick(position: Int,attribCode:String)
    }
    companion object{
        lateinit var workoutGoalChildAdapterListener: WorkoutGoalChildAdapterListener

        fun setAdapterListener(workoutGoalChildAdapterListener: WorkoutGoalChildAdapterListener) {
            this.workoutGoalChildAdapterListener = workoutGoalChildAdapterListener
        }


    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WorkoutGoalChildAdapterViewHolder, position: Int) {
        val item = list[position]
        setVideoTitle(holder,position)
        holder.tv_sub_min.text = list[position].getDurations.replace("+","")
        holder.tv_sub_level.text = list[position].getLevelName
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
        holder.tv_class_type.text = list[position].class_type
        Glide.with(mContext)
            .load(list[position].thumbnail)
            .placeholder(R.drawable.ic_video_screen)
            .into(holder.iv_video)

        holder.itemView.setOnClickListener {
            workoutGoalChildAdapterListener.onItemWorkOutClick(position, item.id)
        }
    }

    private fun setVideoTitle(holder: WorkoutGoalChildAdapterViewHolder,position: Int){
        val firstWord = list[position].title
        val secondWord = " with "+list[position].getInstructor
        val spannable: Spannable = SpannableString(firstWord+secondWord)
        spannable.setSpan(ForegroundColorSpan(mContext.getResources().getColor(R.color.white)), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(mContext.getResources().getColor(R.color.mono_grey_60)), firstWord!!.length, firstWord!!.length+secondWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.tv_header1.text = spannable
    }

}
class WorkoutGoalChildAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val tv_header1 = row.findViewById(R.id.tv_header1) as TextView
    val tv_sub_min = row.findViewById(R.id.tv_sub_min) as TextView
    val tv_sub_level = row.findViewById(R.id.tv_sub_level) as TextView
    val iv_video = row.findViewById(R.id.iv_video) as carbon.widget.ImageView
    val tv_class_type = row.findViewById(R.id.tv_class_type) as TextView
}