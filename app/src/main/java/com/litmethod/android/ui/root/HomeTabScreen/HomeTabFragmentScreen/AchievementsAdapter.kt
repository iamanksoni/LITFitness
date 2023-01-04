package com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.litmethod.android.R

class AchievementsAdapter(
    val result: MutableList<AchivementsViewModel>,
    val context: Context,
    var rvAchievements: RecyclerView
) : RecyclerView.Adapter<AchievementsAdapterViewHolder>() {
    var adapter: RecyclerView.Adapter<*>? = null
    lateinit var achievementsAdapterListener: AchievementsAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AchievementsAdapterViewHolder {
        adapter = this
        return AchievementsAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_home_achievements,
                p0,
                false
            )
        )
    }

    interface AchievementsAdapterListener {
        fun onItemClickAchievements(position: Int)
    }

    fun setAdapterListener(achievementsAdapterListener: AchievementsAdapterListener) {
        this.achievementsAdapterListener = achievementsAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: AchievementsAdapterViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            achievementsAdapterListener.onItemClickAchievements(position)
        }
        achievementSelected(position, holder)
    }

    private fun achievementSelected(position: Int, holder: AchievementsAdapterViewHolder) {
        when (position) {
            0 -> {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.ic_pro_lit))
                    .into(holder.iv_achievements)
            }
            1 -> {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.ic_classes_pic))
                    .into(holder.iv_achievements)
            }
            2 -> {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.ic_day_streak))
                    .into(holder.iv_achievements)
            }
            3 -> {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.ic_week_streak))
                    .into(holder.iv_achievements)
            }
            4 -> {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.ic_classes_pic))
                    .into(holder.iv_achievements)
            }
            5 -> {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.ic_classes_pic))
                    .into(holder.iv_achievements)
            }
        }

        if (result[position].selected) {
            holder.tv_rate_value.text = result[position].title
            holder.tv_rate_value_next_line.text = result[position].secondLineTitle
            when (position) {
                0 -> {
                    holder.tv_rate_value.setTextColor(context.getColor(R.color.yellow))
                    holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.light_yellow))
                    val colorInt1 = context.getColor(R.color.white)
                    holder.iv_achievements.imageTintMode = PorterDuff.Mode.SRC_ATOP
                    holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
                    val colorInt = context.getColor(R.color.yellow_new)
//                    holder.rl_achievements.backgroundTintList = null
                    holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
                    holder.rl_achievements.stroke = null
                    holder.rl_achievements.background=ContextCompat.getDrawable(context,R.drawable.border_item_outer)
                }
                1 -> {
                    holder.tv_rate_value.setTextColor(context.getColor(R.color.yellow))
                    holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.light_yellow))
                    val colorInt1 = context.getColor(R.color.white)
                    holder.iv_achievements.imageTintMode = PorterDuff.Mode.SRC_ATOP
                    holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
                    val colorInt = context.getColor(R.color.yellow_new)
                    holder.rl_achievements.backgroundTintList = null
                    holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
                    holder.rl_achievements.stroke = null
                }
                2 -> {
                    holder.tv_rate_value.setTextColor(context.getColor(R.color.blue))
                    holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.light_blue))
                    val colorInt1 = context.getColor(R.color.white)
                    holder.iv_achievements.imageTintMode = PorterDuff.Mode.SRC_ATOP
                    holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
                    val colorInt = context.getColor(R.color.blue_new)
                    holder.rl_achievements.backgroundTintList = null
                    holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
                    holder.rl_achievements.stroke = null
                }
                3 -> {
                    holder.tv_rate_value.setTextColor(context.getColor(R.color.red))
                    holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.light_red))
                    val colorInt1 = context.getColor(R.color.white)
                    holder.iv_achievements.imageTintMode = PorterDuff.Mode.SRC_ATOP
                    holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
                    val colorInt = context.getColor(R.color.red_new)
                    holder.rl_achievements.backgroundTintList = null
                    holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
                    holder.rl_achievements.stroke = null
                }
                4 -> {
                    holder.tv_rate_value.setTextColor(context.getColor(R.color.orange))
                    holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.light_orange))
                    val colorInt1 = context.getColor(R.color.white)
                    holder.iv_achievements.imageTintMode = PorterDuff.Mode.SRC_ATOP
                    holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
                    val colorInt = context.getColor(R.color.orange_new)
                    holder.rl_achievements.backgroundTintList = null
                    holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
                    holder.rl_achievements.stroke = null
                }
                5 -> {
                    holder.tv_rate_value.setTextColor(context.getColor(R.color.purple_new_100))
                    holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.light_purple))
                    val colorInt1 = context.getColor(R.color.white)
                    holder.iv_achievements.imageTintMode = PorterDuff.Mode.SRC_ATOP
                    holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
                    val colorInt = context.getColor(R.color.purple_new_100_new)
                    holder.rl_achievements.backgroundTintList = null
                    holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
                    holder.rl_achievements.stroke = null
                }
            }

        } else {
            holder.tv_rate_value.text = result[position].title
            holder.tv_rate_value_next_line.text = result[position].secondLineTitle
            holder.tv_rate_value.setTextColor(context.getColor(R.color.mono_grey_60))
            holder.tv_rate_value_next_line.setTextColor(context.getColor(R.color.mono_grey_60))
            val colorInt1 = context.getColor(R.color.mono_grey_60)
            holder.iv_achievements.imageTintList = ColorStateList.valueOf(colorInt1)
            val colorInt = context.getColor(R.color.mono_dark_60)
            holder.rl_achievements.backgroundTintList = ColorStateList.valueOf(colorInt)
            val colorInt3 = context.getColor(R.color.white_light)
            holder.rl_achievements.stroke = ColorStateList.valueOf(colorInt3)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class AchievementsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_rate_value = view.findViewById(R.id.tv_rate_value) as TextView
    val tv_rate_value_next_line = view.findViewById(R.id.tv_rate_value_next_line) as TextView
    val iv_achievements = view.findViewById(R.id.iv_achievements) as ImageView
    val rl_achievements = view.findViewById(R.id.rl_achievements) as RelativeLayout
}