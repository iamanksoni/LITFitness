package com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.ResultUserAnalytics
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.AppUtils

class RateKaclAdapter(
    val result: MutableList<ResultUserAnalytics>,
    val context: Context
) : RecyclerView.Adapter<RateKaclAdapterViewHolder>() {
    var adapter: RecyclerView.Adapter<*>? = null
    lateinit var rateKaclAdapterListener: RateKaclAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RateKaclAdapterViewHolder {
        adapter = this
        return RateKaclAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_home_rate_kacl,
                p0,
                false
            )
        )
    }

    interface RateKaclAdapterListener {
        fun onItemClickRateKacl(position: Int)
    }

    fun setAdapterListener(rateKaclAdapterListener: RateKaclAdapterListener) {
        this.rateKaclAdapterListener = rateKaclAdapterListener
    }

    override fun onBindViewHolder(holder: RateKaclAdapterViewHolder, position: Int) {
        holder.tv_rate_value.text = AppUtils.formatNumber(result[position].data.toString())
        if (result[position].key.equals("heart_rate")) {
            holder.tv_rate_name.text = AppConstants.heart_rate
            holder.tv_rate_name.setTextColor(context.getColor(R.color.red))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_heart_rate))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.red)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)
        } else if (result[position].key.equals("calories")) {
            holder.tv_rate_name.text = AppConstants.calories
            holder.tv_rate_name.setTextColor(context.getColor(R.color.yellow))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_kcal))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.yellow)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)

        } else if (result[position].key.equals("distance")) {
            holder.tv_rate_name.text = AppConstants.distance
            holder.tv_rate_name.setTextColor(context.getColor(R.color.blue))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_location))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.blue)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)
        } else if (result[position].key.equals("total_force")) {
            holder.tv_rate_name.text = AppConstants.total_force
            holder.tv_rate_name.setTextColor(context.getColor(R.color.purple_new_100))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_total_force))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.purple_new_100)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)
        } else if (result[position].key.equals("total_reps")) {
            holder.tv_rate_name.text = AppConstants.total_reps
            holder.tv_rate_name.setTextColor(context.getColor(R.color.green_100))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_reps))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.green_100)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)

        } else if (result[position].key.equals("time_under_tension")) {
            holder.tv_rate_name.text = AppConstants.time_under_tension
            holder.tv_rate_name.setTextColor(context.getColor(R.color.blue))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_tension))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.blue)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)
            val time = AppUtils.fetchTime(result[position].data.toString().toInt())
            val hours = time!![0]
            val minutesNew = time!![1]
            val secondsNew = time!![2]

            if(hours != 0 && minutesNew != 0 && secondsNew != 0){
                holder.tv_rate_value.text = "$hours:$minutesNew:$secondsNew"
            }
            else if(hours == 0 && minutesNew != 0 && secondsNew != 0){
                holder.tv_rate_value.text = "$minutesNew:$secondsNew"
            }
            else if(hours == 0 && minutesNew == 0 && secondsNew != 0){
                holder.tv_rate_value.text = "0$minutesNew:$secondsNew"
            }

        } else if (result[position].key.equals("total_time")) {
            holder.tv_rate_name.text = AppConstants.total_time
            holder.tv_rate_name.setTextColor(context.getColor(R.color.mono_grey_100))
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_time))
                .into(holder.iv_heart_rate)
            val colorInt1 = context.resources.getColor(R.color.mono_grey_100)
            holder.iv_heart_rate.imageTintList =ColorStateList.valueOf(colorInt1)

            val time = AppUtils.fetchTime(result[position].data.toString().toInt())
            val hours = time!![0]
            val minutesNew = time!![1]
            val secondsNew = time!![2]

            if(hours != 0 && minutesNew != 0 && secondsNew != 0){
                holder.tv_rate_value.text = "$hours:$minutesNew:$secondsNew"
            }
            else if(hours == 0 && minutesNew != 0 && secondsNew != 0){
                holder.tv_rate_value.text = "$minutesNew:$secondsNew"
            }
            else if(hours == 0 && minutesNew == 0 && secondsNew != 0){
                holder.tv_rate_value.text = "0$minutesNew:$secondsNew"
            }

        }
        holder.itemView.setOnClickListener {
            rateKaclAdapterListener.onItemClickRateKacl(position)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class RateKaclAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_rate_value: TextView = view.findViewById(R.id.tv_rate_value)
    val tv_rate_name: TextView = view.findViewById(R.id.tv_rate_name)
    val iv_heart_rate: ImageView = view.findViewById(R.id.iv_heart_rate)
}