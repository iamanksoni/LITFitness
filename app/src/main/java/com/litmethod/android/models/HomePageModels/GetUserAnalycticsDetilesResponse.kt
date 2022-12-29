package com.litmethod.android.models.HomePageModels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetUserAnalycticsDetilesResponse(
    val result: ResultGetUserAnalycticsDetiles,
    val serverResponse: ServerResponseAnalycticsDetiles
)

data class ResultGetUserAnalycticsDetiles(
    val grapData: ArrayList<GrapData>,
    val total: Int,
    val videos: ArrayList<VideoGetUserAnalycticsDetiles>
)

data class ServerResponseAnalycticsDetiles(
    val message: String,
    val statusCode: Int,
    val subscribtionStatus: SubscribtionStatus,
    val success: Boolean
)

data class GrapData(
    var date: String,
    val leftArm: Int,
    val rightArm: Int,
    val totalData: Int
)
@Parcelize
data class VideoGetUserAnalycticsDetiles(
    val Date: String,
    val avgCal: String,
    val avgHr: String,
    val class_type: String,
    val completeTime: String,

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
    val videoCategory: ArrayList<VideoCategory>,
    val videoUrl: String
):Parcelable

@Parcelize
data class VideoCategory(
    val name: String,
    val tream_id: String
):Parcelable

data class SubscribtionStatus(
    val has_subscription: Boolean
)

@Parcelize
data class GrapDataShort(
    var date: String,
    var totalData: Int,
    var startDate : String?=null,
    var endDate : String? =null
):Parcelable

