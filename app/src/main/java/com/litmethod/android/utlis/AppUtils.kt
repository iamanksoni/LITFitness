package com.litmethod.android.utlis

import java.text.NumberFormat
import java.util.*

object AppUtils {

    fun formatNumber(input: String): String {
        return String.format("%,d", input.toInt())
    }

}