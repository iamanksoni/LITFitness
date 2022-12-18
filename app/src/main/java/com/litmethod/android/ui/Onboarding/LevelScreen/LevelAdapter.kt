package com.litmethod.android.ui.Onboarding.LevelScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.alpha
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.GetLevel.Data


class LevelAdapter(
    val result: ArrayList<LevelData>,
    val context: Context,
    val levelData:List<Data>
) : RecyclerView.Adapter<LevelAdapterViewHolder>() {
    lateinit var levelAdapterListener:LevelAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LevelAdapterViewHolder {
        return LevelAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_level_view,
                p0,
                false
            )
        )
    }

    interface LevelAdapterListener {
        fun onItemClick(position: Int,code:String)
    }
    fun setAdapterListener(levelAdapterListener: LevelAdapterListener) {
        this.levelAdapterListener = levelAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: LevelAdapterViewHolder, position: Int) {
        var item = levelData[position]
        holder.tv_header1.text =item.title
        holder.tv_sub_header.text = item.description
        Log.d("thecolortext","color text is ${item.hexcode}")
//        holder.tv_header1.setTextColor(Color.parseColor("#f95a01"))

//        var a = Color.parseColor("FFFFFF")
//        a.alpha = 1*255/

        if (result[position].oneItemSelected){
            if(result[position].selectedItem){
                val colorInt1 = context.getColor(R.color.yellow)
                holder.iv_level.imageTintList = ColorStateList.valueOf(colorInt1)
                holder.tv_header1.setTextColor(colorInt1)
                holder.rl_level_second.strokeWidth = 2.0f
                val colorInt =  context.resources.getColor(R.color.yellow)
                holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
                holder.rl_level_second.alpha = 1f
            }else{
                val colorInt1 =  Color.parseColor(result[position].colorItem)
                holder.iv_level.imageTintList = ColorStateList.valueOf(Color.parseColor(item.hexcode))
                holder.tv_header1.setTextColor(Color.parseColor(item.hexcode))
                holder.rl_level_second.strokeWidth = 0.0f
                val colorInt =  context.resources.getColor(R.color.mono_slate_10)
                holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
                holder.rl_level_second.alpha = 0.6f
            }
        }else{
            val colorInt1 = Color.parseColor(result[position].colorItem)
            holder.iv_level.imageTintList = ColorStateList.valueOf(Color.parseColor(item.hexcode))
            holder.tv_header1.setTextColor(Color.parseColor(item.hexcode))
            holder.rl_level_second.strokeWidth = 0.0f
            val colorInt =  context.resources.getColor(R.color.mono_slate_10)
            holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
            holder.rl_level_second.alpha = 1f
        }
        holder.itemView.setOnClickListener {
            levelAdapterListener.onItemClick(position,item.id)

        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

}

class LevelAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val iv_level: ImageView = view.findViewById(R.id.iv_level)
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val rl_level_second: carbon.widget.RelativeLayout = view.findViewById(R.id.rl_level_second)
    val tv_sub_header:TextView = view.findViewById(R.id.tv_sub_header)

}