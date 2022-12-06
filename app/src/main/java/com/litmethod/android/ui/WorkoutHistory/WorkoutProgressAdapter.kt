package com.litmethod.android.ui.WorkoutHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.RelativeLayout
import com.litmethod.android.R
import com.litmethod.android.utlis.CustomProgressBar

class WorkoutProgressAdapter(
    val result: ArrayList<String>,
    val context: Context,
) : RecyclerView.Adapter<WorkoutProgressAdapterViewHolder>() {
    val workProgressItem: ArrayList<WorkoutProgressModel> = ArrayList<WorkoutProgressModel>()
    val workProgressItemnew: ArrayList<WorkoutProgressModel> = ArrayList<WorkoutProgressModel>()
    val workProgressItemnew3: ArrayList<WorkoutProgressModel> = ArrayList<WorkoutProgressModel>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkoutProgressAdapterViewHolder {
        return WorkoutProgressAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_workout_progress,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkoutProgressAdapterViewHolder, position: Int) {
        setValueSeekBar(holder,position)
    }

    private fun setValueSeekBar(holder: WorkoutProgressAdapterViewHolder, position: Int){
        when (position) {
            0 -> {
                holder.rl_type_1.visibility = View.GONE
                holder.rl_type_2.visibility = View.VISIBLE
            }
            1 -> {
                holder.rl_type_1.visibility = View.VISIBLE
                holder.rl_type_2.visibility = View.GONE
                holder.rl_top_bar.visibility = View.VISIBLE
                holder.rl_bottom_bar.visibility = View.GONE

                workProgressItem.clear()
                workProgressItem.add(WorkoutProgressModel(0,20,20.0f))
                workProgressItem.add(WorkoutProgressModel(40,70,30.0f))
                workProgressItem.add(WorkoutProgressModel(80,90,10.0f))
                holder.seekBar0.thumb.mutate().alpha = 0
                holder.seekBar0.initData(workProgressItem)
                holder.seekBar0.invalidate()
            }
            2 -> {
                holder.rl_type_1.visibility = View.VISIBLE
                holder.rl_type_2.visibility = View.GONE
                holder.rl_top_bar.visibility = View.VISIBLE
                holder.rl_bottom_bar.visibility = View.VISIBLE

                workProgressItemnew.clear()
                workProgressItemnew.add(WorkoutProgressModel(0,10,10.0f))
                workProgressItemnew.add(WorkoutProgressModel(30,50,20.0f))
                holder.seekBar0.thumb.mutate().alpha = 0
                holder.seekBar0.initData(workProgressItemnew)
                holder.seekBar0.invalidate()


                workProgressItemnew3.clear()
                workProgressItemnew3.add(WorkoutProgressModel(10,30,20.0f))
                workProgressItemnew3.add(WorkoutProgressModel(40,50,10.0f))
                workProgressItemnew3.add(WorkoutProgressModel(80,90,10.0f))
                 holder.seekBar1.thumb.mutate().alpha = 0
                 holder.seekBar1.initData(workProgressItemnew3)
                 holder.seekBar1.invalidate()
            }
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class WorkoutProgressAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rl_type_1 = view.findViewById(R.id.rl_type_1) as RelativeLayout
    val rl_type_2 = view.findViewById(R.id.rl_type_2) as RelativeLayout

    val rl_top_bar = view.findViewById(R.id.rl_top_bar) as android.widget.RelativeLayout
    val tv_first_right = view.findViewById(R.id.tv_first_right) as TextView
    val tv_first_left = view.findViewById(R.id.tv_first_right) as TextView
    val seekBar0 = view.findViewById(R.id.seekBar0) as CustomProgressBar

    val rl_bottom_bar = view.findViewById(R.id.rl_bottom_bar) as android.widget.RelativeLayout
    val tv_second_right = view.findViewById(R.id.tv_second_right) as TextView
    val tv_second_left = view.findViewById(R.id.tv_second_left) as TextView
    val seekBar1 = view.findViewById(R.id.seekBar1) as CustomProgressBar

    val tv_heart_rate_range = view.findViewById(R.id.tv_heart_rate_range) as TextView
}