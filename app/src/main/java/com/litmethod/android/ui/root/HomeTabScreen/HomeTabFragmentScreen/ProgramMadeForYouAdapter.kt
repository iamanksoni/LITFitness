package com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.litmethod.android.R
import com.litmethod.android.models.VideoX
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentAdapter

class ProgramMadeForYouAdapter(
    val result: MutableList<VideoX>,
    val context: Context,
) : RecyclerView.Adapter<ProgramMadeForYouAdapterViewHolder>() {
    lateinit var programMadeForYouAdapterrListener: ProgramMadeForYouAdapterrListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProgramMadeForYouAdapterViewHolder {
        return ProgramMadeForYouAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_program_made_for_you,
                p0,
                false
            )
        )
    }

    interface ProgramMadeForYouAdapterrListener {
        fun onItemClick(position: Int,data:String)
    }

    fun setAdapterListener(programMadeForYouAdapterrListener: ProgramMadeForYouAdapterrListener) {
        this.programMadeForYouAdapterrListener = programMadeForYouAdapterrListener
    }

    override fun onBindViewHolder(holder: ProgramMadeForYouAdapterViewHolder, position: Int) {
        val item = result[position]
        Glide.with(context)
            .load(result[position].image)
            .placeholder(R.drawable.ic_welcome_one)
            .centerCrop()
            .into(holder.iv_thumbline)
        holder.tv_category.text = result[position].category
        holder.tv_video_type.text = result[position].title
        holder.tv_sub_min.text = result[position].week
        holder.tv_sub_level.text = result[position].level
        when (item.level){
            "Intermediate" -> {
                holder.tv_sub_level.setTextColor(Color.parseColor("#f95a01"))
            }
            "Beginner" -> {
                holder.tv_sub_level.setTextColor(Color.parseColor("#52cfc5"))
            }
            "Advanced" -> {
                holder.tv_sub_level.setTextColor(Color.parseColor("#ed2124"))
            }

        }

        holder.itemView.setOnClickListener {
            programMadeForYouAdapterrListener.onItemClick(position,item.id)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}
class ProgramMadeForYouAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rl_level_second: RelativeLayout = view.findViewById(R.id.rl_level_second)
    val tv_category: TextView = view.findViewById(R.id.tv_category)
    val tv_lit: TextView = view.findViewById(R.id.tv_lit)
    val tv_video_type: TextView = view.findViewById(R.id.tv_video_type)
    val tv_sub_min: TextView = view.findViewById(R.id.tv_sub_min)
    val tv_sub_level: TextView = view.findViewById(R.id.tv_sub_level)
    val iv_thumbline: ImageView = view.findViewById(R.id.iv_thumbline)
}