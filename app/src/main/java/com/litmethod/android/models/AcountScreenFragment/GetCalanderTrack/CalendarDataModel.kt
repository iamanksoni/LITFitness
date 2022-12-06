package com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack

data class CalendarDataModel(
    @JvmField val Date: String,
    var completeTime: String,
    val date: String,
    val getDurations: Int,
    val getInstructor: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val id: String,
    val instructor_image: String,
    val isEnroll: Boolean,
    val isLive: Boolean,
    val isSave: Boolean,
    val startTime: String,
    val thumbnail: String,
    val title: String,
    val videoCategory: List<VideoCategoryModel>,
    val videoUrl: String
)
