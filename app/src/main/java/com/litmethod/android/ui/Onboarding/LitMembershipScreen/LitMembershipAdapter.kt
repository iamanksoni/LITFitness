package com.litmethod.android.ui.Onboarding.LitMembershipScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R

class LitMembershipAdapter(
    val result: ArrayList<LitMembershipData>,
    val context: Context,
) : RecyclerView.Adapter<LitMembershipAdapterViewHolder>() {
    lateinit var levelAdapterListener: LevelAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LitMembershipAdapterViewHolder {
        return LitMembershipAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_membership_view,
                p0,
                false
            )
        )
    }

    interface LevelAdapterListener {
        fun onItemClick(position: Int)
    }

    fun setAdapterListener(levelAdapterListener: LevelAdapterListener) {
        this.levelAdapterListener = levelAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: LitMembershipAdapterViewHolder, position: Int) {
        if (result[position].oneItemSelected) {
            if (result[position].selectedItem) {
                val colorInt2 = context.getColor(R.color.yellow)
                holder.rb_membership.buttonTintList = ColorStateList.valueOf(colorInt2)
                holder.rb_membership.isChecked = true
                val colorInt1 = context.getColor(R.color.white)
                holder.tv_header1.setTextColor(colorInt1)
                holder.rl_level_second.strokeWidth = 1.0f
                val colorInt = context.resources.getColor(R.color.yellow)
                holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
                holder.rl_level_second.alpha = 1f
            } else {
                holder.rb_membership.buttonTintList = null
                holder.rb_membership.isChecked = false
                holder.tv_header1.setTextColor(context.getColor(R.color.white))
                holder.rl_level_second.strokeWidth = 0.0f
                val colorInt = context.resources.getColor(R.color.mono_slate_10)
                holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
                holder.rl_level_second.alpha = 0.6f
            }
        } else {
            holder.rb_membership.buttonTintList = null
            holder.rb_membership.isChecked = false
            holder.tv_header1.setTextColor(context.getColor(R.color.white))
            holder.rl_level_second.strokeWidth = 0.0f
            val colorInt = context.resources.getColor(R.color.mono_slate_10)
            holder.rl_level_second.stroke = ColorStateList.valueOf(colorInt)
            holder.rl_level_second.alpha = 1f
        }

        if(position == 0){
            holder.tv_header1.text = "Monthly"
            holder.tv_subheader.text = "30 Days Free Trial"
            holder.tv_amount.text = "$0"
        }else{
            holder.tv_header1.text = "Buy Subscription"
            holder.tv_subheader.text = "Premium Membership"
        }

        holder.rb_membership.setOnClickListener{
            levelAdapterListener.onItemClick(position)
        }
        holder.itemView.setOnClickListener{
            levelAdapterListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

}

class LitMembershipAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_header1: TextView = view.findViewById(R.id.tv_header1)
    val tv_subheader: TextView = view.findViewById(R.id.tv_sub_header)
    val rl_level_second: carbon.widget.RelativeLayout = view.findViewById(R.id.rl_level_second)
    val rb_membership: RadioButton = view.findViewById(R.id.rb_membership)
    val tv_amount: TextView = view.findViewById(R.id.tv_amount)
}