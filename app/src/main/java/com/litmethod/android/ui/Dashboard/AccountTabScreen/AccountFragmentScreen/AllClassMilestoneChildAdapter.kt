package com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.ClassMilestone
import com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen.Util.TransparencyLevel


class AllClassMilestoneChildAdapter(
    val list: MutableList<ClassMilestone>,
    var mContext: Context,
    var checkAdater: String,
    var getHexValueList: MutableList<String>
) : RecyclerView.Adapter<AllClassMilestoneChildAdapter.AllClassMilestoneChildAdapterViewHolder>() {
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
        if (item.number.toInt() < 10) {
            holder.tv_rate_value.text = "0" + item.number

        } else {
            holder.tv_rate_value.text = item.number
        }
        when (checkAdater) {
            "class" -> {
                holder.iv_achievements.setImageResource(R.drawable.ic_classes_pic_2)

                val hexvalueListSize = getHexValueList.size - 1
                holder.rl_achievements.strokeWidth = 0.0f
                if (hexvalueListSize >= position) {
                    Log.d("Demovalueis", "the item running pos $position")
                    val trans = getHexValueList[position]
                    val transInDec = trans.toDouble() * 100
                    val demo = tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}F9B801"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CCF9B801"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.white
                        )
                    )
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#F9B801"))
                } else {
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.grey
                        )
                    )
                    Log.d("Demovalueis", "the item not running pos $position")
                }


                if (item.number == "1") {
                    holder.tv_rate_value_next_line.text = "CLASS"
                } else {
                    holder.tv_rate_value_next_line.text = "CLASSES"
                }
            }
            "day" -> {
                holder.iv_achievements.setImageResource(R.drawable.ic_day_streak)

                val hexvalueListSize = getHexValueList.size - 1
                if (hexvalueListSize >= position) {
                    Log.d("Demovalueis", "the item running pos $position")
                    val trans = getHexValueList[position]
                    val transInDec = trans.toDouble() * 100
                    val demo = tra.toTransparentColor(transInDec.toInt())
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#${demo}01CCF9"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#CC01CCF9"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.white
                        )
                    )
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#01CCF9"))

                } else {
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.grey
                        )
                    )
                    Log.d("Demovalueis", "the item not running pos $position")
                }


                if (item.number == "1") {
                    holder.tv_rate_value_next_line.text = "DAY"
                } else {
                    holder.tv_rate_value_next_line.text = "DAYS"
                }

            }
            "week" -> {
                holder.iv_achievements.setImageResource(R.drawable.ic_week_streak)

                holder.tv_rate_value_next_line.text = "KCAL"
                if (list.get(position).isComplete) {
                    holder.rl_achievements.strokeWidth = 0.0f
                    holder.tv_rate_value.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.calories_value
                        )
                    )
                    holder.tv_rate_value_next_line.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.calories_unit
                        )
                    )
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.white
                        )
                    )
                } else {
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.grey
                        )
                    )
                    Log.d("Demovalueis", "the item not running pos $position")

                }


                if (item.number == "1") {
                    holder.tv_rate_value_next_line.text = "WEEK"
                } else {
                    holder.tv_rate_value_next_line.text = "WEEKS"
                }

            }
            "calories" -> {
                holder.iv_achievements.setImageResource(R.drawable.ic_classes_pic_2)

                holder.tv_rate_value_next_line.text = "KCAL"
                if (list.get(position).isComplete) {
                    holder.rl_achievements.strokeWidth = 0.0f
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.calories_value
                        )
                    )
                    holder.tv_rate_value_next_line.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.calories_unit
                        )
                    )

                    holder.iv_achievements.setColorFilter(
                        PorterDuffColorFilter(
                            R.color.white,
                            PorterDuff.Mode.DARKEN
                        )
                    )

                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.white
                        )
                    )
                } else {
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.grey
                        )
                    )
//                    Log.d("Demovalueis", "the item not running pos $position")

                }
            }

            "lbs" -> {

                holder.iv_achievements.setImageResource(R.drawable.ic_classes_pic_2)
                if (list.get(position).isComplete) {
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.rl_achievements.strokeWidth = 0.0f
                    holder.tv_rate_value.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.lbs_value_color
                        )
                    )
                    holder.tv_rate_value_next_line.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.lbs_unit_color
                        )
                    )
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.white
                        )
                    )
                } else {
                    holder.rl_achievements.setBackgroundColor(Color.parseColor("#00000000"))
                    holder.tv_rate_value.setTextColor(Color.parseColor("#668F92A1"))
                    holder.tv_rate_value_next_line.setTextColor(Color.parseColor("#668F92A1"))
                    holder.iv_achievements.setColorFilter(
                        ContextCompat.getColor(
                            mContext,
                            R.color.grey
                        )
                    )
                }
                holder.tv_rate_value_next_line.text = "LBS"
            }

        }
    }

    class AllClassMilestoneChildAdapterViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val tv_rate_value_next_line = row.findViewById(R.id.tv_rate_value_next_line) as TextView
        val tv_rate_value = row.findViewById(R.id.tv_rate_value) as TextView
        val iv_achievements = row.findViewById(R.id.iv_achievements) as ImageView
        val rl_achievements: carbon.widget.RelativeLayout = row.findViewById(R.id.rl_achievements)
    }
}