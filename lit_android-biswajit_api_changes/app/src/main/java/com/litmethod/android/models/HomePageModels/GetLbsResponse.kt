package com.litmethod.android.models.HomePageModels

data class GetLbsResponse(
    val result: ResultGetLbs,
    val serverResponse: ServerResponse
)

data class ResultGetLbs(
    val `data`: DataGetLbs
)

data class DataGetLbs(
    val lbsachievements: ArrayList<Lbsachievement>
)

data class Lbsachievement(
    val isComplete: Boolean,
    val isNotify: Boolean,
    val key: String,
    val kg: String,
    val msg: String,
    val number: String
)