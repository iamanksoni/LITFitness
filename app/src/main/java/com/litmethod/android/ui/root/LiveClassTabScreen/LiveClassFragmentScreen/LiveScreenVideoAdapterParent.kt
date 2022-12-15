package com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen

import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.ui.root.LiveClassTabScreen.TimeUtil.LiveClassSeparatedByDate
import com.litmethod.android.ui.root.LiveClassTabScreen.TimeUtil.SetThePostFixinDate
import java.time.LocalDate
import java.util.*

class LiveScreenVideoAdapterParentAdapter(
                                          val list: MutableList<LiveClassSeparatedByDate>,
                                          var mContext: Context,
) : RecyclerView.Adapter<AllClassMilestoneHeaderAdapterViewHolder>() {

    private var allClassMilestoneChildAdapter: LiveScreenVideoAdapter? = null
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): AllClassMilestoneHeaderAdapterViewHolder {
        return AllClassMilestoneHeaderAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_home_workout_header,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AllClassMilestoneHeaderAdapterViewHolder, position: Int) {
        val item = list[position]
        holder.tv_show_all!!.visibility = View.GONE
        val date = item.localDate
        val dt: LocalDate = LocalDate.parse(date.toString())
        val month=  dt.month
        val dateOfMonth = dt.dayOfMonth
       val suffixMonth = SetThePostFixinDate()

        val getLast =suffixMonth.getTheLastNo(dateOfMonth.toString())
       val suffixSet = suffixMonth.setThePostFix(getLast)
        holder.tv_work_goal.text = "$month ${dateOfMonth}$suffixSet"

        Log.d("thedateis","the month $month and date $dateOfMonth")
        holder.rv_home_workout_child!!.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        allClassMilestoneChildAdapter =
            LiveScreenVideoAdapter(list[position].videoList, mContext)
        holder.rv_home_workout_child.adapter = allClassMilestoneChildAdapter

    }



}

class AllClassMilestoneHeaderAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val rv_home_workout_child = row.findViewById(R.id.rv_home_workout_child) as RecyclerView?
    val tv_show_all = row.findViewById(R.id.tv_show_all) as TextView
    val tv_work_goal = row.findViewById(R.id.tv_work_goal) as TextView


}