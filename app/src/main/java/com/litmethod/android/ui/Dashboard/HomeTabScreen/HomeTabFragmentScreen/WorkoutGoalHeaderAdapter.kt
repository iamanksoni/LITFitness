package com.litmethod.android.ui.Dashboard.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.VideoXX
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.FilterScreen.FilterChildAdapter

class WorkoutGoalHeaderAdapter(
    var mContext: Context,
    val list: MutableList<VideoXX>
) :RecyclerView.Adapter<WorkoutGoalHeaderAdapterViewHolder>(){
    private var workoutGoalChildAdapter:WorkoutGoalChildAdapter?= null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkoutGoalHeaderAdapterViewHolder {
        return WorkoutGoalHeaderAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_home_workout_header,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WorkoutGoalHeaderAdapterViewHolder, position: Int) {
        holder.tv_work_goal.text = list[position].headerTitle
        if(list[position].video_count>2){
          holder.tv_show_all.visibility = View.VISIBLE
        }else{
            holder.tv_show_all.visibility = View.GONE
        }
        holder.rv_home_workout_child!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        workoutGoalChildAdapter = WorkoutGoalChildAdapter(list[position].videos,mContext)
        holder.rv_home_workout_child.adapter = workoutGoalChildAdapter
    }

}
class WorkoutGoalHeaderAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val rv_home_workout_child = row.findViewById(R.id.rv_home_workout_child) as RecyclerView?
    val tv_work_goal = row.findViewById(R.id.tv_work_goal) as TextView
    val tv_show_all = row.findViewById(R.id.tv_show_all) as TextView

}