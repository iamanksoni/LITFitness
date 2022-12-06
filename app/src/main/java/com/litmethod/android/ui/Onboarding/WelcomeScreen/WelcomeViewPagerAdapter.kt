package com.litmethod.android.ui.Onboarding.WelcomeScreen

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R

class WelcomeViewPagerAdapter(ctx: Context, var welcomeActivity: WelcomeActivity) :
    RecyclerView.Adapter<WelcomeViewPagerAdapter.ViewHolder>() {

    private val images = intArrayOf(
        R.drawable.ic_welcome_one,
        R.drawable.ic_welcome_two,
        R.drawable.ic_welcome_three,
    )
    private val imagesTablet = intArrayOf(
        R.drawable.ic_welcome_one,
        R.drawable.ic_welcome_two,
        R.drawable.ic_welcome_four,
    )
    private val ctx: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(ctx).inflate(R.layout.welcome_images_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (welcomeActivity.isTablet(ctx)) {
            holder.images.setImageResource(imagesTablet[position])
        } else {
            holder.images.setImageResource(images[position])
        }
        if (position == 2) {
            holder.images.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        when (position) {
            0 -> {
                holder.tv_low_impact.text = "Low impact training"
                val typeFace = Typeface.createFromAsset(ctx.assets, "futura_std_condensed_extra_bd.otf")
                holder.tv_low_impact.typeface = typeFace
                holder.tv_we_deliver.text = "We deliver results not injuries"
                val typeFace2= Typeface.createFromAsset(ctx.assets, "futura_std_medium.otf")
                holder.tv_we_deliver.typeface = typeFace2
                holder.tv_we_deliver2.visibility = View.INVISIBLE
            }
            1 -> {
                holder.tv_low_impact.text = "WITH 8 DIFFERENT CLASS CATEGORIES \nAND CUSTOM PROGRAMS OUR PLATFORM \nIS FOR EVERYONE."
                if (welcomeActivity.isTablet(ctx)) {
                    holder.tv_low_impact.textSize = 32f
                }else{
                    holder.tv_low_impact.textSize = 19f
                }
                val typeFace = Typeface.createFromAsset(ctx.assets, "futura_std_condensed_extra_bd.otf")
                holder.tv_low_impact.typeface = typeFace
                holder.tv_we_deliver.visibility = View.VISIBLE
                holder.tv_we_deliver2.visibility = View.INVISIBLE
                holder.tv_we_deliver2.textSize = 0f
            }
            2 -> {
                holder.tv_low_impact.text = "FOUND BY HUSBAND AND WIFE DUO"
                if (welcomeActivity.isTablet(ctx)) {
                    holder.tv_low_impact.textSize = 34f
                }else{
                    holder.tv_low_impact.textSize = 21f
                }
                val typeFace = Typeface.createFromAsset(ctx.assets, "futura_std_condensed_extra_bd.otf")
                holder.tv_low_impact.typeface = typeFace

                holder.tv_we_deliver.visibility = View.VISIBLE
                holder.tv_we_deliver.text = "JUSTIN AND TAYLOR NORRIS."
                if (welcomeActivity.isTablet(ctx)) {
                    holder.tv_we_deliver.textSize = 44f
                }else{
                    holder.tv_we_deliver.textSize = 27f
                }
                holder.tv_we_deliver.typeface = typeFace
                holder.tv_we_deliver.setTextColor(ctx.getColor(R.color.white))

                holder.tv_we_deliver2.visibility = View.VISIBLE
                holder.tv_we_deliver2.text = "WE'RE COMMITTED TO HELP YOU \nREACH YOUR GOALS."
                if (welcomeActivity.isTablet(ctx)) {
                    holder.tv_we_deliver2.textSize = 34f
                }else{
                    holder.tv_we_deliver2.textSize = 21f
                }
                holder.tv_we_deliver2.typeface = typeFace
            }
        }

    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView
        var tv_low_impact: TextView
        var tv_we_deliver: TextView
        var tv_we_deliver2: TextView

        init {
            images = itemView.findViewById(R.id.iv_image_view)
            tv_low_impact = itemView.findViewById(R.id.tv_low_impact)
            tv_we_deliver = itemView.findViewById(R.id.tv_we_deliver)
            tv_we_deliver2 = itemView.findViewById(R.id.tv_we_deliver2)
        }
    }

    init {
        this.ctx = ctx
    }
}