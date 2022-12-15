package com.litmethod.android.ui.root.HomeTabScreen.PerformanceDetailsScreen

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.ResultUserAnalytics

class PerformanceMetricsDetailsAdapter(
    val list: MutableList<ResultUserAnalytics>,
    var mContext: Context,
    var selectedPosition: Int
) : RecyclerView.Adapter<PerformanceMetricsDetailsAdapterViewHolder>() {

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): PerformanceMetricsDetailsAdapterViewHolder {
        return PerformanceMetricsDetailsAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_performance_metrics,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: PerformanceMetricsDetailsAdapterViewHolder,
        position: Int
    ) {
        setItemData(holder,position)
    }

    private fun setItemData( holder: PerformanceMetricsDetailsAdapterViewHolder, position: Int) {
        holder.tv_rate_value.text = list[position].data.toString()
        if (list[position].key.equals("heart_rate")) {
            holder.tv_rate_name.text = "BPM"
            if(selectedPosition == 0){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_heart_rate_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.red)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.red)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_heart_rate))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }
        } else if (list[position].key.equals("calories")) {
            holder.tv_rate_name.text = "kcal"
            if(selectedPosition == 1){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_kcal_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.yellow)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.yellow)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_kcal))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }
        } else if (list[position].key.equals("distance")) {
            holder.tv_rate_name.text = "  M "
            if(selectedPosition == 2){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_location_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.blue)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.blue)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_location))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }
        } else if (list[position].key.equals("total_force")) {
            holder.tv_rate_name.text = "lbs"
            if(selectedPosition == 3){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_total_force_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.purple_new_100)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.purple_new_100)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_total_force))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }
        } else if (list[position].key.equals("total_reps")) {
            holder.tv_rate_name.text = "reps"
            if(selectedPosition == 4){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_reps_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.green_100)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.green_100)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_reps))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }

        } else if (list[position].key.equals("time_under_tension")) {
            holder.tv_rate_name.text = "TUT"
            if(selectedPosition == 5){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_tension_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.blue)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.blue)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_tension))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }

        } else if (list[position].key.equals("total_time")) {
            holder.tv_rate_name.text = "mins"
            if(selectedPosition == 6){
                var colourNew = mContext.getColor(R.color.black)
                holder.tv_rate_value.setTextColor(colourNew)
                holder.tv_rate_name.setTextColor(colourNew)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_time_fill))
                    .into(holder.iv_heart_rate)
                val colorInt1 = mContext.resources.getColor(R.color.black)
                holder.iv_heart_rate.imageTintList = ColorStateList.valueOf(colorInt1)
                var colourNewBack = mContext.getColor(R.color.mono_grey_100)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }else{
                var colourNew = mContext.getColor(R.color.white)
                holder.tv_rate_value.setTextColor(colourNew)
                var colourNew2 = mContext.getColor(R.color.mono_grey_100)
                holder.tv_rate_name.setTextColor(colourNew2)
                Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.ic_time))
                    .into(holder.iv_heart_rate)
                var colourNewBack = mContext.getColor(R.color.mono_grey_5)
                holder.rl_topview.setBackgroundColor(colourNewBack)
            }

        }
    }
}

class PerformanceMetricsDetailsAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val tv_rate_value: TextView = row.findViewById(R.id.tv_rate_value)
    val tv_rate_name: TextView = row.findViewById(R.id.tv_rate_name)
    val iv_heart_rate: ImageView = row.findViewById(R.id.iv_heart_rate)
    val rl_topview: RelativeLayout = row.findViewById(R.id.rl_topview)
}