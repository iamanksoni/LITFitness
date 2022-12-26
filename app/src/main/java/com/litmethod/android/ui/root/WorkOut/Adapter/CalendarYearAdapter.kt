package com.litmethod.android.ui.root.WorkOut.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R

class CalendarYearAdapter(
    private val context: Context,
    private val onItemClickListener: OnClickOfItemOfYear,
    private val listOfItemOfYear: ArrayList<String>
) :
    RecyclerView.Adapter<CalendarYearAdapter.CalendarViewHolder>() {

    private var selectedPosition: Int = -1

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
        holder.name.text = listOfItemOfYear[position]
        holder.itemView.setOnClickListener {
            selectedPosition = position
            onItemClickListener.onItemClickOfYear(position)
            notifyDataSetChanged()
        }

        if(selectedPosition == position){
            holder.itemView.background = context.getDrawable(R.drawable.selected_border)
        }else{
            holder.itemView.background = null
        }
    }

    override fun getItemCount(): Int {
        return listOfItemOfYear.size
    }

    interface OnClickOfItemOfYear {
        fun onItemClickOfYear(position: Int)
    }


}