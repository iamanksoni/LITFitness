package com.litmethod.android.models.GetInstructorInfo

data class Data8(
    val description: String,
    val headerTitle: String,
    val id: String,
    val instructor_Image_videos: String,
    val instructor_image: String,
    val instructor_type: String,
    val name: String,
    val videos: List<Video>
)