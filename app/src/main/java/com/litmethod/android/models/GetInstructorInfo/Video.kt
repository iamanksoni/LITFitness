package com.litmethod.android.models.GetInstructorInfo

data class Video(
    val date: String,
    val getDurations: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val id: String,
    val isEnroll: Boolean,
    val isLive: Boolean,
    val isSave: Boolean,
    val isViewed: Boolean,
    val thumbnail: String,
    val title: String,
    val videoCategory: List<VideoCategory>,
    val videoUrl: String
)