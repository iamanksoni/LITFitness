package com.litmethod.android.ui.Onboarding.YourInterestScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.GetInterest.Data

class YourInterestAdapter(
    val result: ArrayList<YourInterestData>,
    val context: Context,
    val interestData:List<Data>
) : RecyclerView.Adapter<YourInterestAdapterViewHolder>() {
    lateinit var levelAdapterListener: LevelAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): YourInterestAdapterViewHolder {
        return YourInterestAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_level_view,
                p0,
                false
            )
        )
    }

    interface LevelAdapterListener {
        fun onItemClick(position: Int,data:String)
    }

    fun setAdapterListener(levelAdapterListener: LevelAdapterListener) {
        this.levelAdapterListener = levelAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: YourInterestAdapterViewHolder, position: Int) {
        var item: Data?=null
        if (position==result.size-1){
            holder.tv_header1.text ="All of the above"
            holder.tv_sub_header.text="I want the full Lit experience."

        } else{
            item = interestData[position]
            holder.tv_header1.text =item.title
            Glide.with(context)
                .load(item.image)
                .into(holder.iv_level);
            holder.tv_sub_header.text = item.description
        }


//        holder.tv_header1.text = "Pilates"
//        holder.iv_level.setImageDrawable(context.getDrawable(R.drawable.iv_recovery))
        if (result[position].oneItemSelected) {
            if (result[position].selectedItem) {
                val colorInt1 = context.getColor(R.color.yellow)
                holder.iv_level.imageTintList = ColorStateList.valueOf(colorInt1)
                holder.tv_header1.setTextColor(colorInt1)
                holder.rl_level_second.strokeWidth = 1.0f
                val colorInt = context.resources.getColor(R.color.yellow)
                holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
                holder.rl_level_second.alpha = 1f
            } else {
                val colorInt1 = Color.parseColor(result[position].colorItem)
                holder.iv_level.imageTintList = ColorStateList.valueOf(colorInt1)
                holder.tv_header1.setTextColor(colorInt1)
                holder.rl_level_second.strokeWidth = 0.0f
                val colorInt = context.resources.getColor(R.color.mono_slate_10)
                holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
                holder.rl_level_second.alpha = 0.6f
            }
        } else {
            val colorInt1 = Color.parseColor(result[position].colorItem)
            holder.iv_level.imageTintList = ColorStateList.valueOf(colorInt1)
            holder.tv_header1.setTextColor(colorInt1)
            holder.rl_level_second.strokeWidth = 0.0f
            val colorInt = context.resources.getColor(R.color.mono_slate_10)
            holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
            holder.rl_level_second.alpha = 1f
        }
        holder.itemView.setOnClickListener {
            if (position==result.size-1){
                levelAdapterListener.onItemClick(position,"999")
            }else{
                levelAdapterListener.onItemClick(position,item!!.id)
            }

        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

}

class YourInterestAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val iv_level: ImageView = view.findViewById(R.id.iv_level)
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val rl_level_second: carbon.widget.RelativeLayout = view.findViewById(R.id.rl_level_second)
    val tv_sub_header:TextView = view.findViewById(R.id.tv_sub_header)
}