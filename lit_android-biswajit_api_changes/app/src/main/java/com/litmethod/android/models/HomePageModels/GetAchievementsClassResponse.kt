package com.litmethod.android.models.HomePageModels

data class GetAchievementsClassResponse(
    val result: ResultGetAchievementsClass,
    val serverResponse: ServerResponse
)

data class ResultGetAchievementsClass(
    val `data`: Data
)

data class ServerResponse(
    val message: String,
    val statusCode: Int,
    val success: Boolean
)

data class Data(
    val classMilestone: ArrayList<ClassMilestone>
)

data class ClassMilestone(
    val isComplete: Boolean,
    val isNotify: Boolean,
    val key: String,
    val msg: String,
    val number: String
)