package com.litmethod.android.models.ClassHistoryList

data class Video(
    val class_time_raw: String,
    val class_type: String,
    val completeTime: String,
    val dataId: String,
    val date: String,
    val getDurations: Int,
    val getInstructor: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val id: String,
    val instructor_image: String,
    val isLive: Boolean,
    val statusUrl: String,
    val thumbnail: String,
    val title: String,
    val videoCategory: List<VideoCategory>,
    val videoUrl: String
)