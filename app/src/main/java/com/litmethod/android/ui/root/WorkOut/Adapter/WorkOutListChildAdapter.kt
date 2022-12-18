package com.litmethod.android.ui.root.WorkOut.Adapter

import android.content.Context
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
import com.google.android.material.button.MaterialButton
import com.litmethod.android.R
import com.litmethod.android.models.ClassHistoryList.Video
import com.litmethod.android.models.Liveclass.Data
import com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen.LiveScreenVideoAdapterViewHolder

class WorkOutListChildAdapter(
    val result: List<Video>,
    val context: Context,
) : RecyclerView.Adapter<WorkOutListChildAdapterViewHolder>()  {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkOutListChildAdapterViewHolder {
        return WorkOutListChildAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_workout_child,
                p0,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: WorkOutListChildAdapterViewHolder, position: Int) {

        val item = result[position]
        Log.d("theitemsize","the new response array size is ${result}")

        Glide.with(context)
            .load(item.instructor_image)
            .into(holder.iv_new2)



        setVideoTitle(holder,item)
        setTimeLine(holder,item)
    }


    override fun getItemCount(): Int {
        Log.d("theitemsize","the new response array size is ${result.size}")
        return result.size
    }

    private fun setVideoTitle(holder: WorkOutListChildAdapterViewHolder,item:Video){
        val firstWord = "${item.title}"
        val secondWord = " WITH ${item.getInstructor}"
        val spannable: Spannable = SpannableString(firstWord+secondWord)
        spannable.setSpan(ForegroundColorSpan(context.getResources().getColor(R.color.white)), 0, firstWord!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(context.getResources().getColor(R.color.mono_grey_60)), firstWord!!.length, firstWord!!.length+secondWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.tv_video_title.text = spannable
    }

    private fun setTimeLine(holder: WorkOutListChildAdapterViewHolder,item:Video){
        val firstWordtime = "${item.getDurations} mins"
        val secondWordtime = "  •  "
        val ThirdWordtime = "${item.getLevelName}"


        val spannabletime: Spannable =
            SpannableString(firstWordtime + secondWordtime + ThirdWordtime )
        spannabletime.setSpan(
            ForegroundColorSpan(context.getResources().getColor(R.color.white)),
            0,
            firstWordtime!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannabletime.setSpan(
            ForegroundColorSpan(context.getResources().getColor(R.color.mono_grey_60)),
            firstWordtime!!.length,
            firstWordtime!!.length + secondWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        when (item.getLevelName){
            "Intermediate" -> {

                spannabletime.setSpan(
                    ForegroundColorSpan(context.getResources().getColor(R.color.intermediate)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Beginner" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(context.getResources().getColor(R.color.beginner)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Advanced" -> {

                spannabletime.setSpan(
                    ForegroundColorSpan(context.getResources().getColor(R.color.Advanced)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        }


        holder.tv_video_time.text = spannabletime
    }
}

class WorkOutListChildAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //    val iv_live_now: ImageView = view.findViewById(R.id.iv_live_now)
//    val tv_date: TextView = view.findViewById(R.id.tv_date)
    val tv_video_title: TextView = view.findViewById(R.id.tv_video_title)
    val tv_video_time: TextView = view.findViewById(R.id.tv_video_time)

    val iv_new2: ImageView = view.findViewById(R.id.iv_new2)
//    val btn_sign_me_up: MaterialButton = view.findViewById(R.id.btn_sign_me_up)
}