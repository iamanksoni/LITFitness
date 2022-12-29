package com.litmethod.android.ui.Dashboard.HomeTabScreen.PerformanceDetailsScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
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
    var selectedPosition: Int,
    var rvAllTime: RecyclerView
) : RecyclerView.Adapter<PerformanceMetricsDetailsAdapterViewHolder>() {
    lateinit var recyclerViewOnClickListener : RecyclerViewOnClickListener
    var viewChange : ViewChange? =null
    var adapter: RecyclerView.Adapter<*>? = null
    var setPosition=0
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): PerformanceMetricsDetailsAdapterViewHolder {
        adapter = this
        return PerformanceMetricsDetailsAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_performance_metrics,
                p0,
                false
            )
        )
    }
    interface RecyclerViewOnClickListener{
        fun onItemClick (position: Int)
    }

    interface ViewChange{
        fun setPerfmValue(text : String)
    }
    fun setAdapterListener(recyclerViewOnClickListener: RecyclerViewOnClickListener) {
        this.recyclerViewOnClickListener = recyclerViewOnClickListener
    }

    fun setPerformanceValue(viewChange : ViewChange,  setPosition: Int) {
        this.viewChange=viewChange
        this.setPosition=setPosition

    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: PerformanceMetricsDetailsAdapterViewHolder,
        position: Int
    ) {
        setItemData(holder,position)

        holder.itemView.setOnClickListener {
            recyclerViewOnClickListener.onItemClick(position)
            changeTheValuList(holder.position)
        }
        allTimeSelected(position, holder)
    }

    private fun allTimeSelected(position: Int, holder: PerformanceMetricsDetailsAdapterViewHolder) {

        if (list[position].selected) {
            if (list[0].selected.equals(true)){
                if(position == 0){
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
                }
                else{
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

            }
            if (list[1].selected.equals(true)){
                if(position == 1){
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
            }
            if (list[2].selected.equals(true)){
                if(position == 2){
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
            }
            if (list[3].selected.equals(true)){
                if(position == 3){
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
            }
            if (list[4].selected.equals(true)){
                if(position == 4){
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
            }
            if (list[5].selected.equals(true)){
                if(position == 5){
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
            }
            if (list[6].selected.equals(true)){
                if(position == 6){
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
            selectedPosition=-1

        }
    }
    fun changeTheValuList(newPosition: Int) {
        adapter!!.notifyDataSetChanged()
        rvAllTime.layoutManager!!.scrollToPosition(newPosition)
    }
    private fun setItemData( holder: PerformanceMetricsDetailsAdapterViewHolder, position: Int) {

        if (list[position].key.equals("heart_rate")) {
            if (setPosition==position){
                viewChange?.setPerfmValue(list[position].data.toString())
            }
            holder.tv_rate_value.text = list[position].data.toString()
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
            }
            else{
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
        }
        else if (list[position].key.equals("calories")) {
            if (setPosition==position){
                viewChange?.setPerfmValue(list[position].data.toString())
            }
            holder.tv_rate_value.text = list[position].data.toString()
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
        }
        else if (list[position].key.equals("distance")) {
            if (setPosition==position){
                viewChange?.setPerfmValue(list[position].data.toString())
            }
            holder.tv_rate_value.text = list[position].data.toString()
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
        }
        else if (list[position].key.equals("total_force")) {
            if (setPosition==position){
                viewChange?.setPerfmValue(list[position].data.toString())
            }
            holder.tv_rate_value.text = list[position].data.toString()
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
        }
        else if (list[position].key.equals("total_reps")) {
            if (setPosition==position){
                viewChange?.setPerfmValue(list[position].data.toString())
            }
            holder.tv_rate_value.text = list[position].data.toString()
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

        }
        else if (list[position].key.equals("time_under_tension")) {
            var  minutes = (list[position].data.toString().toInt() % 3600) / 60;
            var seconds = list[position].data.toString().toInt() % 60;


            if(minutes==0 && seconds==0){
                holder.tv_rate_value.text = "0$minutes:0$seconds"

                if (setPosition==position){
                    viewChange?.setPerfmValue("0$minutes:0$seconds")
                }
            }else{     if (setPosition==position){
                viewChange?.setPerfmValue("$minutes:$seconds")
            }
                holder.tv_rate_value.text = "$minutes:$seconds"
            }

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

        }
        else if (list[position].key.equals("total_time")) {
            var  minutes = (list[position].data.toString().toInt() % 3600) / 60;
            var seconds = list[position].data.toString().toInt() % 60;
            if (setPosition==position){
                viewChange?.setPerfmValue("$minutes:$seconds")
            }
            holder.tv_rate_value.text = "$minutes:$seconds"


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