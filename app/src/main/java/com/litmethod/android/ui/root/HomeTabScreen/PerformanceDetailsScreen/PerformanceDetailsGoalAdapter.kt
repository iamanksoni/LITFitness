package com.litmethod.android.ui.root.HomeTabScreen.PerformanceDetailsScreen

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.ImageView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.HomePageModels.VideoGetUserAnalycticsDetiles

class PerformanceDetailsGoalAdapter(
    val list: MutableList<VideoGetUserAnalycticsDetiles>,
    var mContext: Context,
    var selectedPosition: Int
) : RecyclerView.Adapter<WorkoutGoalChildAdapterViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkoutGoalChildAdapterViewHolder {
        return WorkoutGoalChildAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_home_workout_child,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WorkoutGoalChildAdapterViewHolder, position: Int) {
        holder.tv_sub_min.text = list[position].getDurations.toString()
        holder.tv_sub_level.text = list[position].getLevelName
        holder.tv_class_type.text = list[position].class_type
        Glide.with(mContext)
            .load(list[position].thumbnail)
            .into(holder.iv_video)
        setVideoTitle(holder, position)
    }

    private fun setVideoTitle(holder: WorkoutGoalChildAdapterViewHolder, position: Int) {
        val firstWord = list[position].class_type + " "
        val secondWord = "with " + list[position].getInstructor
        when (selectedPosition) {
            0 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.red)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.light_red)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
            1 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.yellow)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.light_yellow)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
            2 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.blue)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.light_blue)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
            3 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.purple_new_100)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.light_purple)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
            4 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.green_100)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.green_light)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
            5 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.blue)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.light_blue)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
            6 -> {
                val spannable: Spannable = SpannableString(firstWord + secondWord)
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.mono_grey_100)
                    ), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.grey_light)
                    ),
                    firstWord!!.length,
                    firstWord!!.length + secondWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.tv_header1.text = spannable
            }
        }
    }

}

class WorkoutGoalChildAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val tv_header1 = row.findViewById(R.id.tv_header1) as TextView
    val iv_video = row.findViewById(R.id.iv_video) as ImageView
    val tv_sub_min = row.findViewById(R.id.tv_sub_min) as TextView
    val tv_sub_level = row.findViewById(R.id.tv_sub_level) as TextView
    val tv_class_type = row.findViewById(R.id.tv_class_type) as TextView
}