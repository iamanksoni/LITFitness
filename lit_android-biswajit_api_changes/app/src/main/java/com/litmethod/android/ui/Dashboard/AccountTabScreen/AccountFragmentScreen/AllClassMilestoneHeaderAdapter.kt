package com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R

//class AllClassMilestoneHeaderAdapter(
//    var mContext: Context,
//    val list: MutableList<AllClassMilestoneReponse>
//) : RecyclerView.Adapter<AllClassMilestoneHeaderAdapterViewHolder>() {
//    private var allClassMilestoneChildAdapter: AllClassMilestoneChildAdapter? = null
//    override fun onCreateViewHolder(
//        p0: ViewGroup,
//        p1: Int
//    ): AllClassMilestoneHeaderAdapterViewHolder {
//        return AllClassMilestoneHeaderAdapterViewHolder(
//            LayoutInflater.from(mContext).inflate(
//                R.layout.item_home_workout_header,
//                p0,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    override fun onBindViewHolder(holder: AllClassMilestoneHeaderAdapterViewHolder, position: Int) {
//        holder.tv_show_all!!.visibility = View.GONE
//        holder.rv_home_workout_child!!.layoutManager =
//            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
//        allClassMilestoneChildAdapter =
//            AllClassMilestoneChildAdapter(list[position].classMilestone, mContext)
//        holder.rv_home_workout_child.adapter = allClassMilestoneChildAdapter
//    }
//}
//
//class AllClassMilestoneHeaderAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
//    val rv_home_workout_child = row.findViewById(R.id.rv_home_workout_child) as RecyclerView?
//    val tv_show_all = row.findViewById(R.id.tv_show_all) as TextView
//}