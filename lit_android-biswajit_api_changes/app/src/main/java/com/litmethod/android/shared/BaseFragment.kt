package com.litmethod.android.shared

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

abstract class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object{
        fun newIntent(thisActivity: Context, cls: Class<*>): Intent {
            val intent = Intent(thisActivity, cls::class.java)
            return intent
        }
    }

    fun toastMessageShow(msg:String){
        val toast = Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

    internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

}