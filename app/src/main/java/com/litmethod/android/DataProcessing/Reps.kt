package com.litmethod.android.DataProcessing

data class Reps(val value: Double, val position: String,val timeStamp:Long) {
}

object RepsConstant{
    const val POSITION_LEFT_BAND="left_band"
    const val POSITION_RIGHT_BAND="right_band"
}