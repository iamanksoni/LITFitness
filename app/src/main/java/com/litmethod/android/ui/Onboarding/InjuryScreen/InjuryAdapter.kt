package com.litmethod.android.ui.Onboarding.InjuryScreen

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.InjuryResponse.Data

class InjuryAdapter(
    val result: ArrayList<InjuryData>,
    val context: Context,
    val injurData:List<Data>
) : RecyclerView.Adapter<InjuryAdapterViewHolder>() {
    lateinit var injuryAdapterListener: InjuryAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InjuryAdapterViewHolder {
        return InjuryAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_injury_view,
                p0,
                false
            )
        )
    }
    interface InjuryAdapterListener {
        fun onItemClick(position: Int,data:String)
    }
    fun setAdapterListener(injuryAdapterListener: InjuryAdapterListener) {
        this.injuryAdapterListener = injuryAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: InjuryAdapterViewHolder, position: Int) {
   var item = injurData[position]
        holder.tv_injury_name.text =item.title
        Glide.with(context)
            .load(item.image)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(holder.iv_injury_image);

        if (result[position].clickableItem) {
            holder.rl_injury.isClickable = true
            holder.tv_injury_name.setTextColor(context.getColor(R.color.white))
        }else{
            holder.rl_injury.isClickable = false
            holder.tv_injury_name.setTextColor(context.getColor(R.color.mono_grey_60))
        }

        holder.itemView.setOnClickListener {
            if (result[position].clickableItem) {
                injuryAdapterListener.onItemClick(position,item.id)
            }
        }

        if(result[position].selectedItem){
            val colorInt = context.resources.getColor(R.color.red)
            holder.rl_injury_second.backgroundTintList = ColorStateList.valueOf(colorInt)
            val colorInt1 = context.resources.getColor(R.color.black)
            holder.iv_injury_image.imageTintList =ColorStateList.valueOf(colorInt1)
            holder.tv_injury_name.setTextColor(context.getColor(R.color.black))
        }else{
            val colorInt = context.resources.getColor(R.color.mono_grey_5)
            holder.rl_injury_second.backgroundTintList = ColorStateList.valueOf(colorInt)
            val colorInt1 = context.resources.getColor(R.color.mono_grey_60)
            holder.iv_injury_image.imageTintList =ColorStateList.valueOf(colorInt1)
            if (result[position].clickableItem) {
                holder.tv_injury_name.setTextColor(context.getColor(R.color.white))
            }else{
                holder.tv_injury_name.setTextColor(context.getColor(R.color.mono_grey_60))
            }
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class InjuryAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rl_injury: RelativeLayout = view.findViewById(R.id.rl_injury)
    val rl_injury_second: RelativeLayout = view.findViewById(R.id.rl_injury_second)
    val tv_injury_name: TextView = view.findViewById(R.id.tv_injury_name)
    val iv_injury_image: ImageView = view.findViewById(R.id.iv_injury_image)
}