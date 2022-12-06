package com.litmethod.android.ui.Dashboard.AllClassTabScreen.FilterScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.FilterList.Filter
import com.litmethod.android.models.FilterList.Parameter
import net.cachapa.expandablelayout.ExpandableLayout

class FilterHeaderAdapter(
    var mContext: Context,
    val list: MutableList<FilterActivityData>,
    val rvFilter: RecyclerView
) :
    RecyclerView.Adapter<FilterHeaderAdapterViewHolder>(),ExpandableLayout.OnExpansionUpdateListener {
    private var filterChildAdapter:FilterChildAdapter?= null
    private var currentPosition = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FilterHeaderAdapterViewHolder {
        return FilterHeaderAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.filter_header,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FilterHeaderAdapterViewHolder, position: Int) {
        val dataList = list[position]

        holder.apply {
            parentTV?.text = dataList.title
        }
        holder.itemView.setOnClickListener {
            onClickItem(holder)
        }
        holder.rv_filter_child!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        filterChildAdapter = FilterChildAdapter(list[position].subList,mContext,
            list[position].title!!,holder.rv_filter_child
        )
        holder.rv_filter_child.adapter = filterChildAdapter
        holder.expandableLayout!!.setInterpolator(OvershootInterpolator())
        holder.expandableLayout.setOnExpansionUpdateListener(this)
    }

    private fun onClickItem(holder: FilterHeaderAdapterViewHolder){
        if (holder != null) {
            holder.expandableLayout!!.collapse()
        }
        if(list[currentPosition].isExpanded){
            list[currentPosition].isExpanded = false
            holder.expandableLayout!!.collapse()
            holder.downIV!!.rotation = 0.0f
        }else{
            list[currentPosition].isExpanded = true
            holder.expandableLayout!!.expand()
            holder.downIV!!.rotation = 180.0f
        }
    }

    override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
        if (state == ExpandableLayout.State.EXPANDING) {
            rvFilter.smoothScrollToPosition(currentPosition)
        }
    }

}

class FilterHeaderAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val parentTV = row.findViewById(R.id.parent_Title) as TextView?
    val downIV = row.findViewById(R.id.down_iv) as ImageView?
    val rv_filter_child = row.findViewById(R.id.rv_filter_child) as RecyclerView?
    val expandableLayout = row.findViewById(R.id.expandable_layout) as ExpandableLayout?
}
