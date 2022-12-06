package com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack

data class CalanderResponseModel(
    val data: ArrayList<CalendarDataModel>,
    val monthlyCount: Int,
    val dailyStreaks: Int
)
