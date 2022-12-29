package com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen

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
import com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen.HomePageVideosModel

class AllTabItemAdapter(
    val result: MutableList<HomePageVideosModel>,
    val context: Context,
    var rvAllTime: RecyclerView,
) : RecyclerView.Adapter<AllTabItemAdapterViewHolder>() {
    var adapter: RecyclerView.Adapter<*>? = null
    lateinit var allTabItemAdapterListener:AllTabItemAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AllTabItemAdapterViewHolder {
        adapter = this
        return AllTabItemAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_all_time,
                p0,
                false
            )
        )
    }

    interface AllTabItemAdapterListener {
        fun onItemClickTime(position: Int)
    }

    fun setAdapterListener(allTabItemAdapterListener:AllTabItemAdapterListener) {
        this.allTabItemAdapterListener = allTabItemAdapterListener
    }

    fun changeTheValuList(newPosition: Int) {
        adapter!!.notifyDataSetChanged()
        rvAllTime.layoutManager!!.scrollToPosition(newPosition)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: AllTabItemAdapterViewHolder, position: Int) {
        holder.tv_child_title.text = result[position].title
        holder.itemView.setOnClickListener {
            allTabItemAdapterListener.onItemClickTime(position)
            changeTheValuList(holder.position)
        }
        allTimeSelected(position, holder)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    private fun allTimeSelected(position: Int, holder: AllTabItemAdapterViewHolder) {
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

class AllTabItemAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_child_title = view.findViewById(R.id.tv_child_title) as TextView
    val cv_taken_by_me = view.findViewById(R.id.cv_taken_by_me) as CardView
}