
package com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ClassesTabScreen

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
import com.litmethod.android.models.GetCatagory.Data

class StackClassesAdapter(
    val result: ArrayList<StackClassesData>,
    val context: Context,
    val getCatagoryList:List<Data>
) : RecyclerView.Adapter<StackClassesAdapterViewHolder>() {
    lateinit var stackClassesAdapterListener: StackClassesAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StackClassesAdapterViewHolder {
        return StackClassesAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_stack_classes_view,
                p0,
                false
            )
        )
    }

    interface StackClassesAdapterListener {
        fun onItemClick(position: Int,code:String)
    }

    fun setAdapterListener(stackClassesAdapterListener: StackClassesAdapterListener) {
        this.stackClassesAdapterListener = stackClassesAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: StackClassesAdapterViewHolder, position: Int) {
        var item = getCatagoryList[position]

        holder.tv_stack_name.text =item.short_name
        Glide.with(context)
            .load(item.iconImage)
            .into(holder.iv_stack_image);

        holder.itemView.setOnClickListener {
            stackClassesAdapterListener.onItemClick(position,item.id)
        }

        if (result[position].selectedItem) {
            val colorInt = context.resources.getColor(R.color.red)
            holder.rl_stack_second.backgroundTintList = ColorStateList.valueOf(colorInt)
            val colorInt1 = context.resources.getColor(R.color.black)
            holder.iv_stack_image.imageTintList = ColorStateList.valueOf(colorInt1)
            holder.tv_stack_name.setTextColor(context.getColor(R.color.black))
        } else {
            val colorInt = context.resources.getColor(R.color.mono_grey_5)
            holder.rl_stack_second.backgroundTintList = ColorStateList.valueOf(colorInt)
            val colorInt1 = context.resources.getColor(R.color.white)
            holder.iv_stack_image.imageTintList = ColorStateList.valueOf(colorInt1)
            holder.tv_stack_name.setTextColor(context.getColor(R.color.white))
        }
    }



    override fun getItemCount(): Int {
        return result.size
    }
}

class StackClassesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rl_stack: RelativeLayout = view.findViewById(R.id.rl_stack)
    val rl_stack_second: RelativeLayout = view.findViewById(R.id.rl_stack_second)
    val tv_stack_name: TextView = view.findViewById(R.id.tv_stack_name)
    val iv_stack_image: ImageView = view.findViewById(R.id.iv_stack_image)
}