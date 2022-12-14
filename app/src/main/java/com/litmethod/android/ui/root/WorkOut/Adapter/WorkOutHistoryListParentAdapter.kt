package com.litmethod.android.ui.root.WorkOut.Adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.ClassHistoryList.Data
import com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen.AllClassMilestoneHeaderAdapterViewHolder
import com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen.LiveScreenVideoAdapter
import com.litmethod.android.ui.root.LiveClassTabScreen.TimeUtil.LiveClassSeparatedByDate
import com.litmethod.android.ui.root.LiveClassTabScreen.TimeUtil.SetThePostFixinDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class WorkOutHistoryListParentAdapter (
    val list: MutableList<Data>,
    var mContext: Context,
) : RecyclerView.Adapter<WorkOutHistoryListParentAdapterViewHolder>() {

    private var allClassMilestoneChildAdapter: WorkOutListChildAdapter? = null
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): WorkOutHistoryListParentAdapterViewHolder {
        return WorkOutHistoryListParentAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_workout_parent,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WorkOutHistoryListParentAdapterViewHolder, position: Int) {
        val item = list[position]



       //val time= getLocalDateFrom(item.date)
        //holder.tv_work_goal.text = time
        //Log.d("thedateis","the month $time and date ")
        holder.rv_home_workout_child!!.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        allClassMilestoneChildAdapter =
            WorkOutListChildAdapter(list[position].video, mContext)
        holder.rv_home_workout_child.adapter = allClassMilestoneChildAdapter

    }
//    yyyy-MMMM-EEEE
}

class WorkOutHistoryListParentAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val rv_home_workout_child = row.findViewById(R.id.rv_home_workout_child) as RecyclerView?
}