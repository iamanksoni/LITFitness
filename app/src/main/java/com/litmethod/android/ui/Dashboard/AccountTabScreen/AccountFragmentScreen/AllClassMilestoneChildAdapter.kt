package com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.ClassMilestone
import com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen.Util.TransparencyLevel
import java.security.AccessController.getContext


class AllClassMilestoneChildAdapter(
    val list: MutableList<ClassMilestone>,
    var mContext: Context,
    var checkAdater:String,
    var getHexValueList:MutableList<String>
) : RecyclerView.Adapter<AllClassMilestoneChildAdapterViewHolder>() {
    val tra = TransparencyLevel()
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): AllClassMilestoneChildAdapterViewHolder {
        return AllClassMilestoneChildAdapterViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_home_achievements,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: AllClassMilestoneChildAdapterViewHolder, position: Int) {
        val item = list[position]
        holder.tv_rate_value.text = item.number
        Log.d("theFilterData","the filter data is $getHexValueList")
        when(checkAdater){
            "class" ->{
                val hexvalueListSize =getHexValueList.size-1
                if (hexvalueListSize>=position){
                    Log.d("Demovalueis","the item running pos $position")
                    val trans =getHexValueList[position]
                    val transInDec = trans.toDouble()*100
                            val demo =tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}F9B801"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CCF9B801"))
                   holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#F9B801"))

//                    if (trans!=""){
////                    Log.d("Demovalueis","the item value is ${item}")
//
//                        if (item.isComplete==true){
//
//                            val transInDec = trans.toDouble()*100
//                            val demo =tra.toTransparentColor(transInDec.toInt())
//                            Log.d("Demovalueis","the item value is ${item} and posirton is $position")
//                            Log.d("Demovalueis","the adaptter running")
//                            holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}F9B801"))
//                        }
//
//
//                    }
                }else{
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.grey))
                    Log.d("Demovalueis","the item not running pos $position")
                }


                if (item.number=="1"){
                    holder.tv_rate_value_next_line.text = "CLASS"
                } else{
                    holder.tv_rate_value_next_line.text = "CLASSES"
                }
            }
            "day" ->  {

                val hexvalueListSize =getHexValueList.size-1
                if (hexvalueListSize>=position){
                    Log.d("Demovalueis","the item running pos $position")
                    val trans =getHexValueList[position]
                    val transInDec = trans.toDouble()*100
                    val demo =tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}01CCF9"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CC01CCF9"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#01CCF9"))

                }else{
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.grey))
                    Log.d("Demovalueis","the item not running pos $position")
                }


                if (item.number=="1"){
                    holder.tv_rate_value_next_line.text = "DAY"
                } else{
                    holder.tv_rate_value_next_line.text = "DAYS"
                }

            }
            "week" ->  {

                val hexvalueListSize =getHexValueList.size-1
                if (hexvalueListSize>=position){
                    Log.d("Demovalueis","the item running pos $position")
                    val trans =getHexValueList[position]
                    val transInDec = trans.toDouble()*100
                    val demo =tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}ED2124"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CCED2124"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#ED2124"))

                }else{
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.grey))
                    Log.d("Demovalueis","the item not running pos $position")
                }

                if (item.number=="1"){
                    holder.tv_rate_value_next_line.text = "WEEK"
                } else{
                    holder.tv_rate_value_next_line.text = "WEEKS"
                }

            }
            "calories" -> {
                val hexvalueListSize =getHexValueList.size-1
                if (hexvalueListSize>=position){
                    Log.d("Demovalueis","the item running pos $position")
                    val trans =getHexValueList[position]
                    val transInDec = trans.toDouble()*100
                    val demo =tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}F95A01"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CCF95A01"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#F95A01"))

                }else{
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.grey))
                    Log.d("Demovalueis","the item not running pos $position")
                }
                holder.tv_rate_value_next_line.text = "KCAL"
            }

            "lbs" ->  {

                val hexvalueListSize =getHexValueList.size-1
                if (hexvalueListSize>=position){
                    Log.d("Demovalueis","the item running pos $position")
                    val trans =getHexValueList[position]
                    val transInDec = trans.toDouble()*100
                    val demo =tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}5D5FEF"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CC5D5FEF"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#5D5FEF"))

                }else{
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(ContextCompat.getColor(mContext, R.color.grey))
                    Log.d("Demovalueis","the item not running pos $position")
                }

                holder.tv_rate_value_next_line.text = "LBS"
            }
        }

    }
}

class AllClassMilestoneChildAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val tv_rate_value_next_line = row.findViewById(R.id.tv_rate_value_next_line) as TextView
    val tv_rate_value = row.findViewById(R.id.tv_rate_value) as TextView
    val iv_achievements = row.findViewById(R.id.iv_achievements) as ImageView
    val rl_achievements = row.findViewById(R.id.rl_achievements) as RelativeLayout
}