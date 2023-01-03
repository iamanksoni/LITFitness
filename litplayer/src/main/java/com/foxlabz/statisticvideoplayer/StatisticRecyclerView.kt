package com.foxlabz.statisticvideoplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatisticRecyclerView(val dataList: ArrayList<StatisticDataModel>, val context: Context) :
    RecyclerView.Adapter<StatisticRecyclerView.StatisticViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        return StatisticViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_statistic_item_style_1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        holder.tv_tut_value.text = dataList[position].value
        holder.item_type.text = dataList[position].label
        if (dataList.get(position).label == "Heart Rate") {
            holder.tv_tut_value.text = dataList.get(position).value
            holder.labelImg.setImageResource(dataList[position].iconResource)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class StatisticViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_tut_value: TextView = itemView.findViewById(R.id.tv_tut_value)
        var item_type: TextView = itemView.findViewById(R.id.tv_item_type)
        var labelImg: ImageView = itemView.findViewById(R.id.iv_label_img)
    }
}