package com.litmethod.android.models.Liveclass

data class Data(
    val accessories: List<Accessory>,
    val awsUrl: String,
    val classRatting: ClassRatting,
    val class_time_show: String,
    val class_type: String,
    val content: Any,
    val date: String,
    val devices: List<Device>,
    val difficultyRating: Int,
    val equipment: List<Equipment>,
    val equipment_video: List<Any>,
    val getDurations: Int,
    val getInstructor: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val id: String,
    val instructor_info: List<InstructorInfo>,
    val isLive: Boolean,
    val mediaid: String,
    val muscle_groups: String,
    val muscle_image: String,
    val radioStation: String,
    val show_feed_fm: Boolean,
    val thumbnail: String,
    val title: String,
    val videoCategory: List<VideoCategory>,
    val videoUrl: String
)





