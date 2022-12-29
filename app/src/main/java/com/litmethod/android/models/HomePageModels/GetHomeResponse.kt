package com.litmethod.android.models

import com.litmethod.android.models.HomePageModels.ServerResponse

data class GetHomeResponse(
    val result: ResultGetHome,
    val serverResponse: ServerResponse
)

data class ResultGetHome(
    val gettingstarted: Gettingstarted,
    val programsmadeforyou: Programsmadeforyou,
    val videos: ArrayList<VideoXX>
)

data class Gettingstarted(
    val headerTitle: String,
    val videos: ArrayList<Video>
)

data class Programsmadeforyou(
    val headerTitle: String,
    val ss: ArrayList<String>,
    val videos: ArrayList<VideoX>
)

data class VideoXX(
    val headerTitle: String,
    val tagId: String,
    val tagName: String,
    val video_count: Int,
    val videos: ArrayList<VideoXXX>
)

data class Video(
    val description: String,
    val title: String,
    val video_duration: String,
    val video_file: String,
    val video_level: String,
    val video_thumbnail: String,
    val video_title: String
)

data class VideoX(
    val category: String,
    val description: String,
    val id: String,
    val image: String,
    val instructorImage: String,
    val instructorName: String,
    val level: String,
    val title: String,
    val week: String
)

data class VideoXXX(
    val awsUrl: String,
    val classRatting: ClassRatting,
    val class_type: String,
    val content: String,
    val date: String,
    val difficultyRating: String,
    val equipment: ArrayList<Equipment>,
    val getDurations: String,
    val getInstructor: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val id: String,
    val isEnroll: Boolean,
    val isLive: Boolean,
    val isSave: Boolean,
    val isViewed: Boolean,
    val mediaid: String,
    val radioStation: String,
    val show_feed_fm: Boolean,
    val thumbnail: String,
    val title: String,
    val videoCategory: ArrayList<VideoCategory>,
    val videoUrl: String
)

data class ClassRatting(
    val totalCount: String,
    val totalLikepersentage: String
)

data class Equipment(
    val imgUrl: String,
    val name: String
)

data class VideoCategory(
    val name: String,
    val tream_id: String
)