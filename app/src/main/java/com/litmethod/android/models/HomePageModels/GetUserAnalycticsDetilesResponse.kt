package com.litmethod.android.models.HomePageModels

import android.icu.text.CaseMap
import com.google.gson.annotations.SerializedName

data class GetUserAnalycticsDetilesResponse(
    val result: ResultGetUserAnalycticsDetiles,
    val serverResponse: ServerResponse
)

data class ResultGetUserAnalycticsDetiles(
    val grapData: List<GrapData>,
    val total: Int,
    val videos: ArrayList<VideoGetUserAnalycticsDetiles>
)

data class GrapData(
    val date: String,
    val leftArm: Int,
    val rightArm: Int,
    val totalData: Int
)

data class VideoGetUserAnalycticsDetiles(
    val Date: String,
    val avgCal: String,
    val avgHr: String,
    val class_type: String,
    val completeTime: String,
    val dataId: Any,
    @SerializedName("date")
    val datenew: String,
    val dateUnix: Long,
    val getDurations: Int,
    val getInstructor: String,
    val getLevelName: String,
    val get_discussion_id: String,
    val getdateBytimeZone: String,
    val groupTime: String,
    val id: String,
    val instructor_image: String,
    val isEnroll: Boolean,
    val isLive: Boolean,
    val isSave: Boolean,
    val startTime: String,
    val startTimeUnix: Long,
    val statusUrl: String,
    val thumbnail: String,
    val videoCategory: List<VideoCategory>,
    val videoUrl: String,
    val title:String?
)

data class VideoCategory(
    val name: String,
    val tream_id: String
)
