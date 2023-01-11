package com.litmethod.android.utlis

import java.util.*

object AppUtils {

    fun formatNumber(input: String): String {
        return String.format("%,d", input.toInt())
    }

    fun fetchTime(value: Int): kotlin.IntArray? {
        val longVal: Long = value.toLong()
        val hours = longVal.toInt() / 3600
        var remainder = longVal.toInt() - hours * 3600
        val mins = remainder / 60
        remainder = remainder - mins * 60
        val secs = remainder
        return intArrayOf(hours, mins, secs)
    }

}