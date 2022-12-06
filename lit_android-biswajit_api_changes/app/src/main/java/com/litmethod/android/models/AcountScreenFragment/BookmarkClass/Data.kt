package com.litmethod.android.models.AcountScreenFragment.BookmarkClass

data class Data(
    val class_type: String,
    val completeTime: String,
    val date: String,
    val getDurations: String,
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
    val videoCategory: List<VideoCategory>,
    val videoUrl: String
)