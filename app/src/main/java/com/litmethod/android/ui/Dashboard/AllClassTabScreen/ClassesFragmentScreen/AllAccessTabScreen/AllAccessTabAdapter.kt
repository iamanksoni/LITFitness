package com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.AllAccessTabScreen

import android.content.Context
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
import com.litmethod.android.models.GetAllAccessCatagory.Data4

class AllAccessTabAdapter(
    val result: ArrayList<Boolean>,
    val context: Context,
    val getAllAccessCatagoryList :List<Data4>
) : RecyclerView.Adapter<AllAccessTabAdapterViewHolder>()  {
    lateinit var allAccessTabAdapterListener: AllAccessTabAdapterListener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AllAccessTabAdapterViewHolder {
        return AllAccessTabAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.itme_all_access_tab_view,
                p0,
                false
            )
        )
    }

    interface AllAccessTabAdapterListener {
        fun onAllAccessItemClick(position: Int,code:String)
    }
    fun setAdapterListenerAllAccess(allAccessTabAdapterListener: AllAccessTabAdapterListener) {
        this.allAccessTabAdapterListener = allAccessTabAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: AllAccessTabAdapterViewHolder, position: Int) {
        var item = getAllAccessCatagoryList[position]
        holder.tv_video_title.text = item.name
        Glide.with(context)
            .load(item.thumbnailImage)
            .into(holder.ivNew2);

        holder.itemView.setOnClickListener {
            allAccessTabAdapterListener.onAllAccessItemClick(position,item.id)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class AllAccessTabAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_video_title: TextView = view.findViewById(R.id.tv_video_title)
    val ivNew2: ImageView = view.findViewById(R.id.iv_new2)
}