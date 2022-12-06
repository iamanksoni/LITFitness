package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ProgramsCoverScreen

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.GetProgramById.Clases

class ProgramChildAdapter(
    val result: List<Clases>,
    val context: Context,
) : RecyclerView.Adapter<ProgramChildAdapterViewHolder>() {
    var adapter: RecyclerView.Adapter<*>? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProgramChildAdapterViewHolder {
        adapter = this
        return ProgramChildAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_programs_cover_child,
                p0,
                false
            )
        )
    }

    interface ProgramChildAdapterListener {
        fun onItemClick(position: Int,code:String)
    }

    companion object {
        lateinit var programChildAdapterListener: ProgramChildAdapterListener

        fun setAdapterListener(programChildAdapterListener: ProgramChildAdapterListener) {
            this.programChildAdapterListener = programChildAdapterListener
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ProgramChildAdapterViewHolder, position: Int) {
       val item = result[position]
        holder.tv_video_intermediate.text = item.classDetiles.getLevelName
        holder.tv_video_time.text = item.classDetiles.getDurations
        Glide.with(context)
            .load(item.classDetiles.thumbnail)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(holder.iv_new2);
        setTextData(holder,item.classDetiles.getInstructor)
        holder.itemView.setOnClickListener {
            programChildAdapterListener.onItemClick(position,item.classDetiles.id)
        }
    }

    private fun setTextData(holder: ProgramChildAdapterViewHolder,instrutorName:String) {

        val firstWord = "Resistance Row "
        val secondWord = "with ${instrutorName}"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            ForegroundColorSpan(context.getResources().getColor(R.color.white)),
            0,
            firstWord!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.getResources().getColor(R.color.mono_grey_60)),
            firstWord!!.length,
            firstWord!!.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.tv_video_title.text = spannable
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class ProgramChildAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_video_title = view.findViewById(R.id.tv_video_title) as TextView
    val tv_video_intermediate = view.findViewById(R.id.tv_intermediate) as TextView
    val tv_video_time = view.findViewById(R.id.tv_video_time) as TextView


    val iv_new2 = view.findViewById(R.id.iv_new2) as ImageView

}