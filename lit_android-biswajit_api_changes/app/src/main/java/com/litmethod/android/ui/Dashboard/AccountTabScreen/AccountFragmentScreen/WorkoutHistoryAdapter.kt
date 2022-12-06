package com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.VideoType
import com.litmethod.android.utlis.CustomTypefaceSpan


class WorkoutHistoryAdapter(
    val result: ArrayList<VideoType>,
    val context: Context,
) : RecyclerView.Adapter<WorkoutHistoryAdapterViewHolder>() {
    lateinit var workoutHistoryAdapterListener: WorkoutHistoryAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkoutHistoryAdapterViewHolder {
        return WorkoutHistoryAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_work_history,
                p0,
                false
            )
        )
    }

    interface WorkoutHistoryAdapterListener {
        fun onItemWorkoutHistoryClick(position: Int,code:String)
    }
    fun setAdapterListener(workoutHistoryAdapterListener: WorkoutHistoryAdapterListener) {
        this.workoutHistoryAdapterListener = workoutHistoryAdapterListener
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: WorkoutHistoryAdapterViewHolder, position: Int) {
        var item = result[position]
        val typeFacebold = Typeface.createFromAsset(context.assets, "futura_std_condensed.otf")
        val typeFace = Typeface.createFromAsset(context.assets, "futura_std_condensed_light.otf")

        Glide.with(context)
            .load(item.image)
            .into(holder.ivLevel);

        val firstWord = "${item.name} "
        val secondWord = "(${item.count})"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            CustomTypefaceSpan("sans-serif",  typeFacebold),
            0,
            firstWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            CustomTypefaceSpan("sans-serif", typeFace),
            firstWord.length,
            firstWord.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.tv_header1.text = spannable

        holder.itemView.setOnClickListener {
            workoutHistoryAdapterListener.onItemWorkoutHistoryClick(position,item.id)

        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

}

class WorkoutHistoryAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val ivLevel: ImageView = view.findViewById(R.id.iv_level)

}