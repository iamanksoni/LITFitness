package com.litmethod.android.models.GetProgramById

data class ClassDetiles(
    val awsUrl: String,
    val content: String,
    val date: String,
    val equipment: List<Equipment>,
    val getDurations: String,
    val getInstructor: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val id: String,
    val isEnroll: Boolean,
    val isLive: Boolean,
    val isSave: Boolean,
    val isViewed: Boolean,
    val radioStation: String,
    val show_feed_fm: Boolean,
    val thumbnail: String,
    val title: String,
    val videoCategory: List<VideoCategory>,
    val videoUrl: String
)