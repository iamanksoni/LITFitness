package com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.ProgramsTabScreen

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.GetProgram.Data3

class ProgramsTabAdapter(
    val result: ArrayList<Boolean>,
    val context: Context,
    val getProgramsList:List<Data3>
) : RecyclerView.Adapter<ProgramsTabAdapterViewHolder>() {
    lateinit var programsTabAdapterListener:ProgramsTabAdapterListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProgramsTabAdapterViewHolder {
        return ProgramsTabAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_video_programs_view,
                p0,
                false
            )
        )
    }

    interface ProgramsTabAdapterListener {
        fun onProgramsItemClick(position: Int,programId:String)
    }

    fun setAdapterListenerProgramsTabAdapter(programsTabAdapterListener:ProgramsTabAdapterListener) {
        this.programsTabAdapterListener = programsTabAdapterListener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ProgramsTabAdapterViewHolder, position: Int) {
        var item = getProgramsList[position]
        holder.tv_video_title.text =item.title
        holder.tvVideoTime.text = item.week
        holder.tv_catagory.text =item.category
        Log.d("the level","the leve is ${item.level}")
        when (item.level){
            "Intermediate" -> {
                holder.tvInterMediate.setTextColor(Color.parseColor("#f95a01"))
            }
            "Beginner" -> {
                holder.tvInterMediate.setTextColor(Color.parseColor("#52cfc5"))
            }
            "Advanced" -> {
                holder.tvInterMediate.setTextColor(Color.parseColor("#ed2124"))
            }

        }
        Glide.with(context)
            .load(item.image)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(holder.ivNew2);
         holder.tvInterMediate.text = item.level

        val firstWord = "${item.title} "
        val secondWord = "with ${item.instructorName}"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            ForegroundColorSpan(context.getResources().getColor(R.color.white)),
            0,
            firstWord!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(
                context.getResources().getColor(R.color.mono_grey_60)
            ),
            firstWord!!.length,
            firstWord!!.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.tv_video_title.text = spannable


        holder.itemView.setOnClickListener {
            programsTabAdapterListener.onProgramsItemClick(position,item.id)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class ProgramsTabAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_video_title: TextView = view.findViewById(R.id.tv_video_title)
    val ivNew2: ImageView = view.findViewById(R.id.iv_new2)
    val tvVideoTime: TextView = view.findViewById(R.id.tv_video_time)
    val tv_catagory: TextView = view.findViewById(R.id.tv_catagory)
    val tvInterMediate: TextView = view.findViewById(R.id.tv_intermediate)


}