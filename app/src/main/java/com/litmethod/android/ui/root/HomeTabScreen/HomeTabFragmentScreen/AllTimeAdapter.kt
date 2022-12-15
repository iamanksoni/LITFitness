package com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R

class AllTimeAdapter(
    val result: MutableList<HomePageVideosModel>,
    val context: Context,
    var rvAllTime: RecyclerView,
) : RecyclerView.Adapter<AllTimeAdapterViewHolder>() {
    var adapter: RecyclerView.Adapter<*>? = null
    lateinit var allTimeAdapterListener: AllTimeAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AllTimeAdapterViewHolder {
        adapter = this
        return AllTimeAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_all_time,
                p0,
                false
            )
        )
    }

    interface AllTimeAdapterListener {
        fun onItemClickTime(position: Int)
    }

    fun setAdapterListener(allTimeAdapterListener: AllTimeAdapterListener) {
        this.allTimeAdapterListener = allTimeAdapterListener
    }

    fun changeTheValuList(newPosition: Int) {
        adapter!!.notifyDataSetChanged()
        rvAllTime.layoutManager!!.scrollToPosition(newPosition)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: AllTimeAdapterViewHolder, position: Int) {
        holder.tv_child_title.text = result[position].title
        holder.itemView.setOnClickListener {
            allTimeAdapterListener.onItemClickTime(position)
            changeTheValuList(holder.position)
        }
        allTimeSelected(position, holder)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    private fun allTimeSelected(position: Int, holder: AllTimeAdapterViewHolder) {
        val typeFacebold = Typeface.createFromAsset(context.assets, "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(context.assets, "futura_std_condensed.otf")
        if (result[position].selected) {
            val colorInt = context.resources.getColor(R.color.red)
            holder.cv_taken_by_me.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
            holder.tv_child_title.setTextColor(context.getColor(R.color.black))
            holder.tv_child_title.typeface = typeFacebold
        } else {
            val colorInt = context.resources.getColor(R.color.mono_grey_5)
            holder.cv_taken_by_me.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
            holder.tv_child_title.setTextColor(context.getColor(R.color.white))
            holder.tv_child_title.typeface = typeFace
        }
    }
}

class AllTimeAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_child_title = view.findViewById(R.id.tv_child_title) as TextView
    val cv_taken_by_me = view.findViewById(R.id.cv_taken_by_me) as CardView
}