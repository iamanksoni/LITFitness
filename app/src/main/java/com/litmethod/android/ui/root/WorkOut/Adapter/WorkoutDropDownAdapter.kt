package com.litmethod.android.ui.root.WorkOut.Adapter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.VideoType
import com.litmethod.android.utlis.CustomTypefaceSpan

class WorkoutDropDownAdapter(
    val result: ArrayList<VideoType>,
    val context: Context,
) : RecyclerView.Adapter<WorkoutDropDownAdapterViewHolder>() {
    lateinit var levelAdapterListener: WorkoutDropDownAdapterListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutDropDownAdapterViewHolder {
        return WorkoutDropDownAdapterViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.workout_dropdown_layout,
            parent,
            false
        ))
    }

    interface WorkoutDropDownAdapterListener {
        fun onItemClick(position: Int,code:String)
    }
    fun setAdapterListener(levelAdapterListener: WorkoutDropDownAdapterListener) {
        this.levelAdapterListener = levelAdapterListener
    }

    override fun onBindViewHolder(holder: WorkoutDropDownAdapterViewHolder, position: Int) {
        var item = result[position]
        val typeFacebold = Typeface.createFromAsset(context.assets, "futura_std_condensed.otf")
        val typeFace = Typeface.createFromAsset(context.assets, "futura_std_condensed_light.otf")
        if (position==0){
            holder.iv_drop_icon.visibility = View.VISIBLE
        } else{
            holder.iv_drop_icon.visibility = View.GONE
        }

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
            levelAdapterListener.onItemClick(position,item.id)

        }
    }

    override fun getItemCount(): Int {
        return result.size
    }


}

class WorkoutDropDownAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val ivLevel: ImageView = view.findViewById(R.id.iv_level)
    val iv_drop_icon: ImageView = view.findViewById(R.id.iv_drop_icon)


}