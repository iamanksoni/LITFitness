package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ProgramsCoverScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.GetProgramById.Clases
import com.litmethod.android.models.GetProgramById.WeekVideo
import com.litmethod.android.utlis.PeekingLinearLayoutManager

class ProgramsCoverHeaderAdapter(
    var mContext: Context,
    val list: MutableList<WeekVideo>
) : RecyclerView.Adapter<ProgramsCoverHeaderAdapterViewHolder>(){
    private var programChildAdapter:ProgramChildAdapter?= null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProgramsCoverHeaderAdapterViewHolder {
        return ProgramsCoverHeaderAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_programs_cover_header,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProgramsCoverHeaderAdapterViewHolder, position: Int) {
        val item = list[position]
        holder.tv_week_desc.text = "${item.weekDescription}"
        holder.tv_week_title.text = item.weekTitle
        holder.rv_program_child!!.layoutManager = PeekingLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        programChildAdapter = ProgramChildAdapter(list[position].clasess,mContext)
        holder.rv_program_child.adapter = programChildAdapter
    }

}

class ProgramsCoverHeaderAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val rv_program_child = row.findViewById(R.id.rv_program_child) as RecyclerView?
    val tv_week_desc = row.findViewById(R.id.tv_trainer_desc) as TextView
    val tv_week_title = row.findViewById(R.id.tv_week_count) as TextView

}
