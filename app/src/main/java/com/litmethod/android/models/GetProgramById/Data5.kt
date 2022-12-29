package com.litmethod.android.models.GetProgramById

data class Data5(
    val category: String,
    val id: String,
    val image: String,
    val instructorImage: String,
    val instructorName: String,
    val level: String,
    val title: String,
    val week: String,
    val weekVideos: List<WeekVideo>
)