package com.litmethod.android.ui.root.WorkOut.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R

class CalendarAdapter(
    private val context: Context,
    private val onItemClickListener: OnClickOfItem,
    private val listOfItem: ArrayList<String>
) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var mSelectedPosition: Int = -1

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_for_custom_calendar, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.name.text = listOfItem[position]
        holder.itemView.setOnClickListener {
            mSelectedPosition = position
            onItemClickListener.onItemClick(position)
            notifyDataSetChanged()
        }

        if(mSelectedPosition == position){
            holder.itemView.background = context.getDrawable(R.drawable.selected_border)
        }
    }

    override fun getItemCount(): Int {
        return listOfItem.size
    }

    interface OnClickOfItem {
        fun onItemClick(position: Int)
    }


}