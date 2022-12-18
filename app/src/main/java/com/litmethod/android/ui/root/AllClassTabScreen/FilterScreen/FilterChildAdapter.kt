package com.litmethod.android.ui.root.AllClassTabScreen.FilterScreen

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.models.FilterList.Filter
import com.litmethod.android.models.FilterList.Parameter

class FilterChildAdapter(
    val result: MutableList<ChildData>,
    val context: Context,
    val title: String,
    val rvFilterChild: RecyclerView


) : RecyclerView.Adapter<FilterChildAdapterViewHolder>() {
    var adapter: RecyclerView.Adapter<*>? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FilterChildAdapterViewHolder {
        adapter = this
        return FilterChildAdapterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.filter_header_row,
                p0,
                false
            )
        )
    }

    interface FilterChildAdapterListener {
        fun onItemClick(position: Int,title : String,attribCode:String)
    }

    companion object{
        lateinit var filterChildAdapterListener: FilterChildAdapterListener

        fun setAdapterListener(filterChildAdapterListener: FilterChildAdapterListener) {
            this.filterChildAdapterListener = filterChildAdapterListener
        }


    }

    fun changeTheValuList(newPosition:Int){
       adapter!!.notifyDataSetChanged()
        rvFilterChild.layoutManager!!.scrollToPosition(newPosition)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: FilterChildAdapterViewHolder, position: Int) {
        if(title == "Muscle Group"){
            holder.cv_taken_by_me.visibility =View.GONE
            holder.rl_instructor.visibility =View.GONE
            holder.rl_injury.visibility =View.VISIBLE
            holder.tv_injury_name.text = result[position].childData.name
            Glide.with(context)
                .load(result[position].childData.image)
                .into(holder.iv_injury_image);
            holder.rl_injury.setOnClickListener {
                filterChildAdapterListener.onItemClick(position,title,result[position].childData.attributeCode.toString())
                changeTheValuList(holder.position)
            }
            muscleGroupSelected(position,holder)
        }else if(title == "Instructor"){
            holder.cv_taken_by_me.visibility =View.GONE
            holder.rl_injury.visibility =View.GONE
            holder.rl_instructor.visibility =View.VISIBLE
            holder.tv_instructor_title.text = result[position].childData.name
            Glide.with(context)
                .load(result[position].childData.instructorImage)
                .into(holder.iv_instructor_image);

            holder.rl_instructor.setOnClickListener {
                filterChildAdapterListener.onItemClick(position,title,result[position].childData.attributeCode.toString())
                changeTheValuList(holder.position)
            }
            instructorSelected(position,holder)
        }else{
            holder.cv_taken_by_me.visibility =View.VISIBLE
            holder.rl_injury.visibility =View.GONE
            holder.rl_instructor.visibility =View.GONE
            holder.tv_child_title!!.text = result[position].childData.name
            holder.cv_taken_by_me.setOnClickListener {
                filterChildAdapterListener.onItemClick(position,title,result[position].childData.attributeCode.toString())
                changeTheValuList(holder.position)
            }
            otherItemSelected(position,holder)
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

    private fun muscleGroupSelected(position: Int,holder: FilterChildAdapterViewHolder) {
        if(result[position].isSelected){
            val colorInt = context.resources.getColor(R.color.red)
            holder.rl_injury.backgroundTintList = ColorStateList.valueOf(colorInt)
            holder.tv_injury_name.setTextColor(context.getColor(R.color.black))
            val colorInt1 = context.resources.getColor(R.color.black)
            holder.iv_injury_image.imageTintList =ColorStateList.valueOf(colorInt1)
        }else{
            val colorInt = context.resources.getColor(R.color.mono_grey_5)
            holder.rl_injury.backgroundTintList = ColorStateList.valueOf(colorInt)
            holder.tv_injury_name.setTextColor(context.getColor(R.color.white))
            val colorInt1 = context.resources.getColor(R.color.mono_grey_60)
            holder.iv_injury_image.imageTintList =ColorStateList.valueOf(colorInt1)
        }
    }

    private fun otherItemSelected(position: Int,holder: FilterChildAdapterViewHolder){
        val typeFacebold = Typeface.createFromAsset(context.assets, "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(context.assets, "futura_std_condensed.otf")
        if(result[position].isSelected){
            val colorInt = context.resources.getColor(R.color.red)
            holder.cv_taken_by_me.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
            holder.tv_child_title.setTextColor(context.getColor(R.color.black))
            holder.tv_child_title.typeface = typeFacebold
        }else{
            val colorInt = context.resources.getColor(R.color.mono_grey_5)
            holder.cv_taken_by_me.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
            holder.tv_child_title.setTextColor(context.getColor(R.color.white))
            holder.tv_child_title.typeface = typeFace
        }
    }

    private fun instructorSelected(position: Int,holder: FilterChildAdapterViewHolder){
        val typeFacebold = Typeface.createFromAsset(context.assets, "futura_std_condensed_bold.otf")
        val typeFaceold = Typeface.createFromAsset(context.assets, "futura_std_condensed.otf")
        if(result[position].isSelected){
            holder.iv_instructor_image.colorFilter = null
            holder.tv_instructor_title.setTextColor(context.getColor(R.color.red))
            holder.tv_instructor_title.typeface = typeFacebold
        }else{
            holder.iv_instructor_image.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
            holder.tv_instructor_title.setTextColor(context.getColor(R.color.white))
            holder.tv_instructor_title.typeface = typeFaceold
        }
    }
}
class FilterChildAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_child_title = view.findViewById(R.id.tv_child_title) as TextView
    val cv_taken_by_me = view.findViewById(R.id.cv_taken_by_me) as CardView

    val rl_injury = view.findViewById(R.id.rl_injury) as RelativeLayout
    val tv_injury_name = view.findViewById(R.id.tv_injury_name) as TextView
    val iv_injury_image = view.findViewById(R.id.iv_injury_image) as ImageView

    val rl_instructor= view.findViewById(R.id.rl_instructor) as RelativeLayout
    val iv_instructor_image= view.findViewById(R.id.iv_instructor_image) as ImageView
    val tv_instructor_title= view.findViewById(R.id.tv_instructor_title) as TextView
}