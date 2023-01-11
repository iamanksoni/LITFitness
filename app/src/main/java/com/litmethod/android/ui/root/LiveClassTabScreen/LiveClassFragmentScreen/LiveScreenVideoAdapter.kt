package com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen

import android.content.Context
import android.os.Build
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.litmethod.android.R
import com.litmethod.android.models.Liveclass.Data
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class LiveScreenVideoAdapter(
    val result: List<Data>,
    val context: Context,
) : RecyclerView.Adapter<LiveScreenVideoAdapterViewHolder>() {

    private lateinit var liveItemClickListner: LiveScreenVideoAdapter.LiveClassItemInterface

    fun setClickListener(liveItemClickListener: LiveScreenVideoAdapter.LiveClassItemInterface){
        this.liveItemClickListner=liveItemClickListener
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LiveScreenVideoAdapterViewHolder {
        return LiveScreenVideoAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_live_classes_child,
                p0,
                false
            )
        )
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: LiveScreenVideoAdapterViewHolder, position: Int) {
        var running = false
        var seconds = 0
        val item = result[position]
        Log.d("theitemsize", "the new response array size is ${result}")
        val instructorimage = item.instructor_info[0].instructor_image
        holder.btn_start_workout.backgroundTintList = null
        Glide.with(context)
            .load(instructorimage)
            .into(holder.iv_level)
        seconds = getLocalDateFrom(item.class_time_show)

        running = true
        setTheAdapter(holder, seconds, running)
        setVideoTitle(holder, item)
        setTimeLine(holder, item)

        holder.btn_start_workout.setOnClickListener{
            Toast.makeText(context,"vv",Toast.LENGTH_SHORT).show()
            liveItemClickListner.onLiveVideoItemClick(item)
        }
    }


    interface LiveClassItemInterface{
       fun onLiveVideoItemClick(item:Data)
    }

    private fun setVideoTitle(holder: LiveScreenVideoAdapterViewHolder, item: Data) {
        val firstWord = "${item.title}"
        val secondWord = " WITH ${item.getInstructor}"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.white)),
            0,
            firstWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(
                context.resources.getColor(R.color.mono_grey_60)
            ),
            firstWord.length,
            firstWord.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.tv_video_title.text = spannable
    }

    private fun setTimeLine(holder: LiveScreenVideoAdapterViewHolder, item: Data) {
        val firstWordtime = "${item.getDurations} mins"
        val secondWordtime = "  •  "
        val ThirdWordtime = "${item.getLevelName}"


        val spannabletime: Spannable =
            SpannableString(firstWordtime + secondWordtime + ThirdWordtime)
        spannabletime.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.white)),
            0,
            firstWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannabletime.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.mono_grey_60)),
            firstWordtime.length,
            firstWordtime.length + secondWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        when (item.getLevelName) {
            "Intermediate" -> {

                spannabletime.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.intermediate)),
                    firstWordtime.length + secondWordtime.length,
                    firstWordtime.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Beginner" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.beginner)),
                    firstWordtime.length + secondWordtime.length,
                    firstWordtime.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Advanced" -> {

                spannabletime.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.Advanced)),
                    firstWordtime.length + secondWordtime.length,
                    firstWordtime.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        }


        holder.tv_video_time.text = spannabletime
    }

    private fun setTheAdapter(
        holder: LiveScreenVideoAdapterViewHolder,
        seconds: Int,
        running: Boolean
    ) {
        var seconds = seconds

        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val day = seconds / 86400
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60

                // Format the seconds into hours, minutes,
                // and seconds.
                val time: String = java.lang.String
                    .format(
                        Locale.getDefault(),
                        "%02dD : %02dH : %02dM : %02dS", day, hours,
                        minutes, secs
                    )

                // Set the text view text.
                holder.tv_date.text = time

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds--
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocalDateFrom(utcDateString: String): Int {
        val temporalAccessor = DateTimeFormatter.ISO_DATE_TIME.parse(utcDateString)
        val instant = Instant.from(temporalAccessor)
        val date = Date.from(instant)

        val currentDate = Date()

        val diff = abs(currentDate.time - date.time)

        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(diff)
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diff)
        val diffInMin: Long = TimeUnit.MILLISECONDS.toMinutes(diff)
        val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diff)


        return diffInSec.toInt()
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class LiveScreenVideoAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //    val iv_live_now: ImageView = view.findViewById(R.id.iv_live_now)
    val tv_date: TextView = view.findViewById(R.id.tv_date)
    val tv_video_title: TextView = view.findViewById(R.id.tv_video_title)
    val tv_video_time: TextView = view.findViewById(R.id.tv_video_time)
    val btn_start_workout: MaterialButton = view.findViewById(R.id.btn_start_workout)
    val iv_level: ImageView = view.findViewById(R.id.iv_level)
//    val btn_sign_me_up: MaterialButton = view.findViewById(R.id.btn_sign_me_up)
}